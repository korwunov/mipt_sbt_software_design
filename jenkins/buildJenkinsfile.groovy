pipeline {
    agent any

    parameters {
        string(name: 'branchName', defaultValue: 'hw5', description: 'Branch name to build from')
        string(name: 'serviceName', defaultValue: 'currencyRateProvider', description: 'Service name')
    }
    
    tools {
        jdk 'jdk17'  
    }

    stages {
        stage('Environment Check') {
            steps {
                sh """
                    echo "Java version:"
                    java -version
                    echo "Maven version:"
                    mvn -version
                    echo "Current directory:"
                    pwd
                    echo "JAVA_HOME: ${JAVA_HOME}"
                """
            }
        }

        stage('git clone') {
            steps {
                git branch: params.branchName, url: 'https://github.com/korwunov/mipt_sbt_software_design.git'
            }
        }

        stage('Build service') {
            steps {
                sh "mvn -B -DskipTests -pl ${params.serviceName} clean package"
            }
        }
        
        stage('Upload to Nexus') {
            environment {
                NEXUS_PROTOCOL = 'http'
                NEXUS_URL = 'nexus:8081'
                NEXUS_CREDENTIALS_ID = 'nexus_user_password'
                GROUP_ID = "com.$serviceName"
                NEXUS_REPO = 'maven-releases'
            }
            steps {
                script {
                    def jarName = ''
                    if (params.serviceName == 'currencyRateProvider') {jarName = 'currency-rate-provider'}
                    if (params.serviceName == 'currencyRatePrinter') {jarName = 'currency-rate-printer'}

                    def artifactVersion = "1.0.${env.BUILD_NUMBER}"
                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: env.NEXUS_PROTOCOL,
                        nexusUrl: env.NEXUS_URL,
                        groupId: env.GROUP_ID,
                        version: artifactVersion,
                        credentialsId: env.NEXUS_CREDENTIALS_ID,
                        repository: env.NEXUS_REPO,
                        artifacts: [
                            [
                                artifactId: params.serviceName, classifier: '', file: "${params.serviceName}/target/${jarName}-0.0.1-SNAPSHOT.jar", type: 'jar'
                            ]
                        ]
                    )

                    def groupIdUrl = env.GROUP_ID.replace(".", "/")
                    def distUrl = "${env.NEXUS_PROTOCOL}://${NEXUS_URL}/repository/${env.NEXUS_REPO}/${groupIdUrl}/${params.serviceName}/${artifactVersion}/${params.serviceName}-${artifactVersion}.jar"

                    print(
                        """
                            ==========================
                            distr url: \n${distUrl}
                            ==========================
                        """
                    )
                }
            }
        }
    }

    post {
        always {
            script {
                sh "ls"
                deleteDir()
                sh "ls"
            }
        }
    }
}