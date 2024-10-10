pipeline {
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64/'
        repository = "cheonghalim/paranmanzang"  // docker hub id와 repository 이름
        DOCKERHUB_CREDENTIALS = credentials('docker-hub') // jenkins에 등록해 놓은 docker hub credentials 이름
    }
    agent any
    stages {
        stage('Cleanup') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
              checkout([$class: 'GitSCM',
                  branches: [[name: '*/master']],
                  userRemoteConfigs: [[
                      url: 'git@github.com:paranmanzang/paran_msa.git',
                      credentialsId: 'ssh-key'
                  ]],
                  extensions: [[$class: 'SubmoduleOption', recursiveSubmodules: true, parentCredentials: true]]
              ])

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
                      ./gradlew :$module:bootJar --warning-mode all
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
                           def modulePaths = [
                               "config": "/var/lib/jenkins/workspace/paranmanzang/server/config-server",
                               "eureka": "/var/lib/jenkins/workspace/paranmanzang/server/eureka-server",
                               "user": "/var/lib/jenkins/workspace/paranmanzang/service/user-service",
                               "group": "/var/lib/jenkins/workspace/paranmanzang/service/group-service",
                               "chat": "/var/lib/jenkins/workspace/paranmanzang/service/chat-service",
                               "file": "/var/lib/jenkins/workspace/paranmanzang/service/file-service",
                               "room": "/var/lib/jenkins/workspace/paranmanzang/service/room-service",
                               "comment": "/var/lib/jenkins/workspace/paranmanzang/service/comment-service",
                               "gateway": "/var/lib/jenkins/workspace/paranmanzang/server/gateway-server"
                           ]

                           for (module in modulePaths.keySet()) {
                               echo "Building Docker image for ${module}"
                               sh """
                               docker build -t ${repository}/${module}:latest ${modulePaths[module]}
                               """
                           }
               }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    def modulePaths = [
                        "config": "/var/lib/jenkins/workspace/paranmanzang/server/config-server",
                        "eureka": "/var/lib/jenkins/workspace/paranmanzang/server/eureka-server",
                        "user": "/var/lib/jenkins/workspace/paranmanzang/service/user-service",
                        "group": "/var/lib/jenkins/workspace/paranmanzang/service/group-service",
                        "chat": "/var/lib/jenkins/workspace/paranmanzang/service/chat-service",
                        "file": "/var/lib/jenkins/workspace/paranmanzang/service/file-service",
                        "room": "/var/lib/jenkins/workspace/paranmanzang/service/room-service",
                        "comment": "/var/lib/jenkins/workspace/paranmanzang/service/comment-service",
                        "gateway": "/var/lib/jenkins/workspace/paranmanzang/server/gateway-server"
                    ]

                    for (module in modulePaths.keySet()) {
                        def imageTag = "${repository}/${module}:${env.BUILD_ID}"
                        echo "Tagging and pushing Docker image for ${module}"
                        sh """
                        docker tag ${repository}/${module}:latest ${imageTag}
                        docker push ${imageTag}
                        """
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
                       echo "Updating Kubernetes deployment for ${module}"
                       sh """
                       kubectl set image deployment/${module} ${module}=${imageTag}
                       """
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
