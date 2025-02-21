@Library('jenkins-sl')_


pipeline {
    agent any

    tools {
        maven "maven-build"
    }

    stages {
        

        stage('Build JAR File') {
            steps {
                script {
                    buildJar()
                }
            }
        }

        stage('Build Docker Image & Push to Docker Hub') {
            steps {
                script {
                    buildImage()  // Ensure correct function name
                }
            }
        }
    }
}
