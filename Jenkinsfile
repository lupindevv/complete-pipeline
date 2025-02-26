// script.groovy

def buildJar() {
    echo "Building the application..."
    sh 'mvn clean package'
    
    // Increment version after successful build
    def versionFile = "pom.xml"
    def newVersion = incrementVersion(versionFile)
    
    // Create reference file to the JAR
    createJarReference(newVersion)
    
    echo "Building the JAR file completed with new version: ${newVersion}"
}

def buildDockerImage() {
    echo "Building the docker image..."
    
    // Read the current version from pom.xml
    def pom = readFile('pom.xml')
    def matcher = pom =~ '<version>(.*?)</version>'
    matcher.find()
    def version = matcher[0][1]
    
    // Read the JAR reference file to get the path
    def jarRefPath = readFile('target/jarReference.txt').trim()
    
    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t alexthm1/demo-app:jma-${version} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push alexthm1/demo-app:jma-${version}"
    }
    
    echo "Docker Image Push Completed"
}

// Helper function to increment version
def incrementVersion(String versionFile) {
    echo "Incrementing version in ${versionFile}..."
    
    def pom = readFile(versionFile)
    def pattern = '<version>(\\d+)\\.(\\d+)\\.(\\d+)</version>'
    def matcher = pom =~ pattern
    
    if (!matcher.find()) {
        error "Could not find version pattern in file: ${versionFile}"
    }
    
    def major = matcher.group(1).toInteger()
    def minor = matcher.group(2).toInteger()
    def patch = matcher.group(3).toInteger()
    
    echo "Current version: ${major}.${minor}.${patch}"
    
    // Increment patch version
    patch++
    def newVersion = "${major}.${minor}.${patch}"
    echo "New version: ${newVersion}"
    
    // Replace version in file
    def newContent = pom.replaceFirst(pattern, "<version>${newVersion}</version>")
    writeFile(file: versionFile, text: newContent)
    
    // Optionally commit the change
    // sh """
    //     git config user.email "jenkins@example.com"
    //     git config user.name "Jenkins"
    //     git add ${versionFile}
    //     git commit -m "Bump version to ${newVersion} [ci skip]"
    //     git push origin HEAD:\${env.BRANCH_NAME}
    // """
    
    return newVersion
}

// Helper function to create JAR reference file
def createJarReference(String version) {
    echo "Creating JAR reference file..."
    
    def jarBaseName = readMavenPom().getArtifactId()
    def jarFileName = "${jarBaseName}-${version}.jar"
    def jarPath = "target/${jarFileName}"
    def referenceFilePath = "target/jarReference.txt"
    
    // Verify the JAR exists
    if (!fileExists(jarPath)) {
        error "JAR file not found: ${jarPath}"
    }
    
    // Create or update the reference file
    writeFile(file: referenceFilePath, text: jarPath)
    echo "Created reference file at ${referenceFilePath} pointing to ${jarPath}"
    
    return jarPath
}

return this