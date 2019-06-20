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
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
    }
}