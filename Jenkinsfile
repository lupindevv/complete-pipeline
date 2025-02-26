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
                    // Use the shared library to increment version
                    def newVersion = incrementVersion(
                        versionFile: 'pom.xml',
                        versionPattern: '<version>(\\d+)\\.(\\d+)\\.(\\d+)</version>',
                        incrementType: 'patch'
                    )
                    
                    // Create reference to the JAR file
                    jarReference(
                        jarBaseName: readMavenPom().getArtifactId(),
                        version: newVersion,
                        targetPath: 'target',
                        artifactPath: 'target'
                    )
                    
                    // Store version for later use
                    env.APP_VERSION = newVersion
                }
            }
        }
        stage('Build Docker Image & Push to Docker Hub') {
            steps {
                script {
                    gv.buildDockerImage(env.APP_VERSION)
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