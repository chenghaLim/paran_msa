pipeline {
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64/'
        repository = "cheonghalim/paranmanzang"  // docker hub id와 repository 이름
        DOCKERHUB_CREDENTIALS = credentials('docker-hub') // jenkins에 등록해 놓은 docker hub credentials 이름
        KUBECONFIG = "/var/lib/jenkins/.kube/config"
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
                checkout([$class           : 'GitSCM',
                          branches         : [[name: '*/master']],
                          userRemoteConfigs: [[
                                                      url          : 'git@github.com:paranmanzang/paran_msa.git',
                                                      credentialsId: 'ssh-key'
                                              ]],
                          extensions       : [[$class: 'SubmoduleOption', recursiveSubmodules: true, parentCredentials: true]]
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
                     "config" : "/var/lib/jenkins/workspace/paranmanzang/server/config-server",
                            "eureka" : "/var/lib/jenkins/workspace/paranmanzang/server/eureka-server",
//                             "user"   : "/var/lib/jenkins/workspace/paranmanzang/service/user-service",
//                             "group"  : "/var/lib/jenkins/workspace/paranmanzang/service/group-service",
//                             "chat"   : "/var/lib/jenkins/workspace/paranmanzang/service/chat-service",
//                             "file"   : "/var/lib/jenkins/workspace/paranmanzang/service/file-service",
//                             "room"   : "/var/lib/jenkins/workspace/paranmanzang/service/room-service",
//                             "comment": "/var/lib/jenkins/workspace/paranmanzang/service/comment-service",
                            "gateway": "/var/lib/jenkins/workspace/paranmanzang/server/gateway-server"
                    ]

                    for (module in modulePaths.keySet()) {
                        echo "Building Docker image for ${module}"
                        sh """
                            docker build --no-cache -t ${REPOSITORY}:${module} ${modulePaths[module]}
                        """
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    def modulePaths = [
                    "config" : "/var/lib/jenkins/workspace/paranmanzang/server/config-server",
                            "eureka" : "/var/lib/jenkins/workspace/paranmanzang/server/eureka-server",
//                             "user"   : "/var/lib/jenkins/workspace/paranmanzang/service/user-service",
//                             "group"  : "/var/lib/jenkins/workspace/paranmanzang/service/group-service",
//                             "chat"   : "/var/lib/jenkins/workspace/paranmanzang/service/chat-service",
//                             "file"   : "/var/lib/jenkins/workspace/paranmanzang/service/file-service",
//                             "room"   : "/var/lib/jenkins/workspace/paranmanzang/service/room-service",
//                             "comment": "/var/lib/jenkins/workspace/paranmanzang/service/comment-service",
                            "gateway": "/var/lib/jenkins/workspace/paranmanzang/server/gateway-server"
                    ]

                    for (module in modulePaths.keySet()) {
                        echo "Pushing Docker image for ${module} to Docker Hub"
                        sh """
                            docker push ${REPOSITORY}:${module}
                        """
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    def modulePaths = [
                            "config" : "/var/lib/jenkins/workspace/paranmanzang/server/config-server/config.yaml",
"eureka" : "/var/lib/jenkins/workspace/paranmanzang/server/eureka-server/eureka.yaml",
"gateway": "/var/lib/jenkins/workspace/paranmanzang/server/gateway-server/gateway.yaml",
// "chat"   : "/var/lib/jenkins/workspace/paranmanzang/service/chat-service/chat.yaml",
// "user"   : "/var/lib/jenkins/workspace/paranmanzang/service/user-service/user.yaml",
// "group"  : "/var/lib/jenkins/workspace/paranmanzang/service/group-service/group.yaml",
// "file"   : "/var/lib/jenkins/workspace/paranmanzang/service/file-service/file.yaml",
// "room"   : "/var/lib/jenkins/workspace/paranmanzang/service/room-service/room.yaml",
// "comment": "/var/lib/jenkins/workspace/paranmanzang/service/comment-service/comment.yaml"
                    ]

//                     Config Server 배포
                    stage('Deploy Config Server') {
                        def yamlPath = modulePaths["config"]
                        echo "Applying Kubernetes deployment for Config Server using YAML file: ${yamlPath}"
                        sh "kubectl replace -f ${yamlPath}"
                    }
//
//                     // Eureka Server 배포
                    stage('Deploy Eureka Server') {
                        def yamlPath = modulePaths["eureka"]
                        echo "Applying Kubernetes deployment for Eureka Server using YAML file: ${yamlPath}"
                        sh "kubectl replace -f ${yamlPath}"
                    }

//                     Gateway Server 배포
                    stage('Deploy Gateway Server') {
                        def yamlPath = modulePaths["gateway"]
                        echo "Applying Kubernetes deployment for Gateway Server using YAML file: ${yamlPath}"
                        sh "kubectl replace -f ${yamlPath}"
                    }

                    // 나머지 서비스 배포
//                     for (module in modulePaths.keySet()) {
//                         if (module != "config" && module != "eureka" && module != "gateway") {
//                             stage("Deploy ${module.capitalize()} Service") {
//                                 def yamlPath = modulePaths[module]
//                                 echo "Applying Kubernetes deployment for ${module} using YAML file: ${yamlPath}"
//                                 sh "kubectl replace -f ${yamlPath}"
//                             }
//                         }
//                     }
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