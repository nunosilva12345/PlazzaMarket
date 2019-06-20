pipeline {
    agent any
    tools {
        jdk 'jdk8'
        maven 'mvn3'
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
    }
}