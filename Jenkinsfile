def gv = null  // Initialize gv

pipeline {
    agent any

    tools {
        maven "maven-build"
    }

    stages {
        stage('Init') {
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

        stage('Build Docker Image & Push to Docker Hub') {
            steps {
                script {
                    gv.buildDockerImage()  // Ensure correct function name
                }
            }
        }
    }
}
