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
                    buildImage 'alexthm1/demo-app:jma-3.1'  // Ensure correct function name
                }
            }
        }
    }
}
