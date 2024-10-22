pipeline {
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64/'
        REPOSITORY = "cheonghalim/paranmanzang"  // Docker Hub 레포지토리 이름
        DOCKERHUB_CREDENTIALS = credentials('docker-hub') // Jenkins에 등록된 Docker Hub 자격 증명
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

        stage('Build Java Projects') {
            steps {
                script {
                    sh '''#!/bin/bash
                    set -e
                    export JAVA_HOME="$JAVA_HOME"

                    chmod +x ./gradlew

                    all_modules=("server:gateway-server" "server:config-server" "server:eureka-server"
                                 "service:user-service" "service:group-service" "service:chat-service"
                                 "service:file-service" "service:room-service" "service:comment-service")

                    echo "Cleaning projects..."
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

        stage('Build and Push Docker Images') {
            steps {
                script {
                    def modulePaths = [
                        "eureka": "/var/lib/jenkins/workspace/paranmanzang/server/eureka-server",
                        "user"  : "/var/lib/jenkins/workspace/paranmanzang/service/user-service",
                        "group" : "/var/lib/jenkins/workspace/paranmanzang/service/group-service",
                        "chat"  : "/var/lib/jenkins/workspace/paranmanzang/service/chat-service",
                        "file"  : "/var/lib/jenkins/workspace/paranmanzang/service/file-service",
                        "room"  : "/var/lib/jenkins/workspace/paranmanzang/service/room-service",
                        "comment": "/var/lib/jenkins/workspace/paranmanzang/service/comment-service",
                        "gateway": "/var/lib/jenkins/workspace/paranmanzang/server/gateway-server"
                    ]

                    for (module in modulePaths.keySet()) {
                        echo "Building Docker image for ${module}"
                        sh """
                            docker build --no-cache -t ${REPOSITORY}:${module} ${modulePaths[module]}
                        """

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
                    def moduleYAMLs = [
                        "eureka": "/var/lib/jenkins/workspace/paranmanzang/server/eureka-server/eureka.yaml",
                        "gateway": "/var/lib/jenkins/workspace/paranmanzang/server/gateway-server/gateway.yaml",
                        "chat"  : "/var/lib/jenkins/workspace/paranmanzang/service/chat-service/chat.yaml",
                        "user"  : "/var/lib/jenkins/workspace/paranmanzang/service/user-service/user.yaml",
                        "group" : "/var/lib/jenkins/workspace/paranmanzang/service/group-service/group.yaml",
                        "file"  : "/var/lib/jenkins/workspace/paranmanzang/service/file-service/file.yaml",
                        "room"  : "/var/lib/jenkins/workspace/paranmanzang/service/room-service/room.yaml",
                        "comment": "/var/lib/jenkins/workspace/paranmanzang/service/comment-service/comment.yaml"
                    ]

                    for (module in moduleYAMLs.keySet()) {
                        stage("Deploy ${module.capitalize()}") {
                            def yamlPath = moduleYAMLs[module]
                            echo "Applying Kubernetes deployment for ${module} using YAML file: ${yamlPath}"
                            sh "kubectl apply -f ${yamlPath}"

                            echo "Checking rollout status for deployment ${module}"
                            sh "kubectl rollout status deployment/${module}"
                        }
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
