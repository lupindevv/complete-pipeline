@Library('jenkins-sl') _

def gv = null  // Initialize gv
pipeline {
    agent any
    tools {
        maven "maven-build"
    }
    stages {
        stage('Init - Testing Webhooks') {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage('Build JAR File') {
            steps {
                script {
                    gv.buildJar()
                }
            }
        }
        stage('Version Management') {
            steps {
                script {
                    // Extract project name from pom.xml safely
                    def pomContent = readFile('pom.xml')
                    def projectName = ''
                    def projectNameMatch = (pomContent =~ '<artifactId>(.*?)</artifactId>')
                    if (projectNameMatch.find()) {
                        projectName = projectNameMatch.group(1)
                        // Ensure matcher is not stored in a variable that persists
                        projectNameMatch = null
                    } else {
                        error "Could not find artifactId in pom.xml"
                    }
                    
                    // Use the shared library to increment version
                    def newVersion = incrementVersion(
                        versionFile: 'pom.xml',
                        versionPattern: '<version>(\\d+)\\.(\\d+)\\.(\\d+)</version>',
                        incrementType: 'major'
                    )
                    
                    // Create reference to the JAR file
                    jarReference(
                        jarBaseName: projectName,
                        version: newVersion,
                        targetPath: 'target',
                        artifactPath: 'target'
                    )
                    
                    // Store version and project name for later use
                    env.APP_VERSION = newVersion
                    env.PROJECT_NAME = projectName
                }
            }
        }
        stage('Build Docker Image & Push to Docker Hub') {
            steps {
                script {
                    gv.buildDockerImage(env.PROJECT_NAME, env.APP_VERSION)
                }
            }
        }
    }
    post {
        success {
            echo "Pipeline executed successfully!"
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        failure {
            echo "Pipeline execution failed!"
        }
    }
}