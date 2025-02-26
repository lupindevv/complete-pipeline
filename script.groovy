// script.groovy

def buildJar() {
    echo "Building the application..."
    sh 'mvn clean package'
    echo "Building the JAR file completed"
}

def buildDockerImage(String version = null) {
    echo "Building the docker image..."
    
    // If version wasn't passed, try to read it from pom.xml
    if (!version) {
        def pom = readFile('pom.xml')
        def matcher = pom =~ '<version>(.*?)</version>'
        matcher.find()
        version = matcher[0][1]
    }
    
    // Read the reference file if it exists
    def jarPath = "target/${readMavenPom().getArtifactId()}-${version}.jar"
    if (fileExists('target/jarReference.txt')) {
        jarPath = readFile('target/jarReference.txt').trim()
    }
    
    echo "Building Docker image with JAR: ${jarPath} and version: ${version}"
    
    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t alexthm1/demo-app:jma-${version} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push alexthm1/demo-app:jma-${version}"
    }
    
    echo "Docker Image Push Completed"
}

return this