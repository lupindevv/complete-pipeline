pipeline {
    agent any

    tools {
        maven "maven-build"
    }

    stages {
        stage('Build JAR File') {
            steps {
                script {
                    echo 'Building JAR file...'
                    sh 'mvn package'
                }
            }
        }

        stage('Build Docker Image & Push to Docker Hub') {
            steps {
                script {
                    echo 'Building Docker image...'
                    withCredentials([usernamePassword(credentialsId: 'dockerhubs', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh 'docker build -t alexthm1/demo-app:jma-2.0 .'
                        sh 'docker login -u $USER -p $PASS'
                        sh 'docker push alexthm1/demo-app:jma-2.0'
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
