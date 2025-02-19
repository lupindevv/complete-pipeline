pipeline {
    agent any
    tools {
        maven "maven-build"
    }

    stages {
        stage('build jar file') {
            steps {
                script {
                    echo 'building jar file'
                    sh 'mvn package'
            }
        }
        }
        stage('build docker image') {
            steps {
                script {
                    echo 'building docker image'
                    withCredentials([usernamePassword(credentialsID: 'dockerhubs', passwordVarialbe: 'PASS', usernameVarialbe: 'USER')]{
                        sh 'docker build -t alexthm1/demo-app:jma-2.0 .'
                        sh 'docker login - u $USER -p $PASS'
                        sh 'docker push alexthm1/demo-app:jma-2.0'
                    })
            }
        }
        }
        stage('deploy') {
            steps {
                echo 'deploying'
            }
        }
    }
}
