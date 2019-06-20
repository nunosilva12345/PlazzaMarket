pipeline {
    agent any
    tools {
        jdk 'jdk8'
        maven 'mvn3'
    }
    stages {
        stage('test java installation') {
            steps {
                sh 'java -version'
            }
        }
        stage('test maven installation') {
            steps {
                sh 'mvn --version'
            }
        }
        stage('build') { 
            steps {
                withSonarQubeEnv('mySonar') {
                    withMaven(maven: 'mvn3') {
                        sh 'mvn clean package sonar:sonar' 
                    }
                }
            }
        }
    }
    post {
  	    success {
  		    slackSend (color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}) - ${GIT_BRANCH}")
  	    }
  	    failure {
  		    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}) - ${GIT_BRANCH}")
  	    }
    }
}