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
        stage('Version Management') {
            steps {
                script {
                    // Read POM file
                    def pomContent = readFile('pom.xml')
                    
                    // Extract current version
                    def versionMatch = (pomContent =~ '<version>(\\d+)\\.(\\d+)\\.(\\d+)</version>')
                    def major = 1
                    def minor = 0
                    def patch = 0
                    
                    if (versionMatch.find()) {
                        major = versionMatch.group(1).toInteger()
                        minor = versionMatch.group(2).toInteger()
                        patch = versionMatch.group(3).toInteger()
                        versionMatch = null // Clear matcher
                    }
                    
                    echo "Current version: ${major}.${minor}.${patch}"
                    
                    // Increment major version
                    major++
                    minor = 0
                    patch = 0
                    
                    def newVersion = "${major}.${minor}.${patch}"
                    echo "New version: ${newVersion}"
                    
                    // Update POM file - use replaceFirst to only update the project version, not dependency versions
                    def updatedContent = pomContent.replaceFirst("<version>\\d+\\.\\d+\\.\\d+</version>", "<version>${newVersion}</version>")
                    writeFile file: 'pom.xml', text: updatedContent
                    
                    // Extract project name
                    def projectNameMatch = (pomContent =~ '<artifactId>(.*?)</artifactId>')
                    def projectName = 'java-maven-app' // Default
                    
                    if (projectNameMatch.find()) {
                        projectName = projectNameMatch.group(1)
                        projectNameMatch = null // Clear matcher
                    }
                    
                    // Store as environment variables
                    env.APP_VERSION = newVersion
                    env.PROJECT_NAME = projectName
                    
                    echo "Updated to version ${newVersion} for project ${projectName}"
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
        stage('Create JAR Reference') {
            steps {
                script {
                    // Create reference to the JAR file
                    sh "echo target/${env.PROJECT_NAME}-${env.APP_VERSION}.jar > target/jarReference.txt"
                    echo "Created reference to JAR: ${env.PROJECT_NAME}-${env.APP_VERSION}.jar"
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