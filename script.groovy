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
        def matcher = pom =~ '<artifactId>(.*?)</artifactId>'
        if (matcher.find()) {
            projectName = matcher[0][1]
        } else {
            error "Could not find artifactId in pom.xml"
        }
    }
    
    // If version wasn't passed, try to read it from pom.xml
    if (!version) {
        def pom = readFile('pom.xml')
        def matcher = pom =~ '<version>(.*?)</version>'
        if (matcher.find()) {
            version = matcher[0][1]
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