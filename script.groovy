def buildJar() {
    sh 'mvn package'
}

def buildDockerImage() {
    echo 'Building Docker image...'
                    withCredentials([usernamePassword(credentialsId: 'dockerhubs', passwordVariable: 'PASS', usernameVariable: 'USER')]) 
                        sh 'docker build -t alexthm1/demo-app:jma-2.2 .'
                        sh 'docker login -u $USER -p $PASS'
                        sh 'docker push alexthm1/demo-app:jma-2.2'
}
return this