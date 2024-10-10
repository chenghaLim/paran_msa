pipeline {
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64/'
        repository = "cheonghalim/paranmanzang"  //docker hub id와 repository 이름
        DOCKERHUB_CREDENTIALS = credentials('docker-hub') // jenkins에 등록해 놓은 docker hub credentials 이름
    }
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', credentialsId: 'ssh-key', url: 'https://github.com/paranmanzang/paran_msa.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    sh '''#!/bin/bash
                    set -e
                    export JAVA_HOME="$JAVA_HOME"

                    chmod +x ./gradlew

                    all_modules=("server:gateway-server" "server:config-server" "server:eureka-server"
                                 "service:user-service" "service:group-service" "service:chat-service"
                                 "service:file-service" "service:room-service" "service:comment-service")

                    echo "Cleaning..."
                    ./gradlew clean

                    for module in "${all_modules[@]}"
                    do
                      echo "Building BootJar for $module"
                      ./gradlew :$module:bootJar
                    done
                    '''
                }
            }
        }
        stage('Login to Docker Hub') {
            steps {
                sh '''
                echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                '''
            }
        }
        stage('Build Docker Images') {
            steps {
                script {
                    def modules = ["config", "eureka", "user", "group", "chat", "file", "room", "comment", "gateway"]

                    for (module in modules) {
                        sh '''
                        docker build -t ${repository}/${module}:latest ./path/to/${module}
                        '''
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    def modules = ["config", "eureka", "user", "group", "chat", "file", "room", "comment", "gateway"]

                    for (module in modules) {
                        def imageTag = "${repository}/${module}:${env.BUILD_ID}"

                        // 태그 추가 및 Docker Hub에 푸시
                        sh '''
                        docker tag ${repository}/${module}:latest ${imageTag}
                        docker push ${imageTag}
                        '''
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    def modules = ["gateway", "config", "eureka", "user", "group", "chat", "file", "room", "comment"]

                    for (module in modules) {
                        def imageTag = "${repository}/${module}:${env.BUILD_ID}"
                        sh '''
                        kubectl set image deployment/${module} ${module}=${imageTag}
                        '''
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}
