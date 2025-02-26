// script.groovy

def buildJar() {
    echo "Building the application..."
    sh 'mvn clean package'
    echo "Building the JAR file completed"
}

def buildDockerImage(String projectName = null, String version = null) {
    echo "Building the docker image..."
    
    // If project name wasn't passed, try to read it from pom.xml
    if (!projectName) {
        def pom = readFile('pom.xml')
        def projectNameMatch = (pom =~ '<artifactId>(.*?)</artifactId>')
        if (projectNameMatch.find()) {
            projectName = projectNameMatch.group(1)
            // Ensure matcher is not stored in a variable that persists
            projectNameMatch = null
        } else {
            error "Could not find artifactId in pom.xml"
        }
    }
    
    // If version wasn't passed, try to read it from pom.xml
    if (!version) {
        def pom = readFile('pom.xml')
        def versionMatch = (pom =~ '<version>(.*?)</version>')
        if (versionMatch.find()) {
            version = versionMatch.group(1)
            // Ensure matcher is not stored in a variable that persists
            versionMatch = null
        } else {
            error "Could not find version in pom.xml"
        }
    }
    
    // Read the reference file if it exists
    def jarPath = "target/${projectName}-${version}.jar"
    if (fileExists('target/jarReference.txt')) {
        jarPath = readFile('target/jarReference.txt').trim()
    }
    
    echo "Building Docker image for project: ${projectName} with JAR: ${jarPath} and version: ${version}"
    
    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t alexthm1/demo-app:jma-${version} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push alexthm1/demo-app:jma-${version}"
    }
    
    echo "Docker Image Push Completed"
}

return this