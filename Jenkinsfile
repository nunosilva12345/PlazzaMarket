pipeline {
    agent any
    tools {
        jdk 'jdk8'
        maven 'mvn3'
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('build, test and static analysis') { 
            steps {
                withSonarQubeEnv('mySonar') {
                    withMaven(maven: 'mvn3') {
                        sh 'mvn clean package sonar:sonar' 
                    }
                }
            }
        }
        stage('deliver') {
            steps {
                sh 'chmod +x ./jenkins/scripts/deliver.sh'
                sshagent([ 'tqs-machine' ]) {
                    sh './jenkins/scripts/deliver.sh'
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