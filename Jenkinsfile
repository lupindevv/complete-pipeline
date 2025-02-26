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
                    // Extract project name from pom.xml manually
                    def pomContent = readFile('pom.xml')
                    def projectNameMatcher = pomContent =~ '<artifactId>(.*?)</artifactId>'
                    def projectName = ''
                    if (projectNameMatcher.find()) {
                        projectName = projectNameMatcher[0][1]
                    } else {
                        error "Could not find artifactId in pom.xml"
                    }
                    
                    // Use the shared library to increment version
                    def newVersion = incrementVersion(
                        versionFile: 'pom.xml',
                        versionPattern: '<version>(\\d+)\\.(\\d+)\\.(\\d+)</version>',
                        incrementType: 'patch'
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