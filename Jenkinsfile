def gv

pipeline {
    agent any

    tools {
        maven "maven-build"
    }

    stages {
        stage('init') {
            steps {
                script {
                    gv =load "script.groovy"
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
                     gv.buildDockerimage()
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying the application...'
            }
        }
    }
}
