#!/usr/bin/env groovy

node { //Can pass in a String arg that specifies which label to build on, ex. node("unix") {
    def gradleExec = isUnix() ? "./gradlew" : "/.gradlew.bat"
    def gradleOptions = "--info --stacktrace -Dhttps.protocols=TLSv1.1,TLSv1.2"

    // /Define settings for build
    def tmpDir = "/tmp"
    //TODO Jenkinsfiles don't appear to support multiple assignments
    def proxyHost = env.HTTPS_PROXY ? env.HTTPS_PROXY.split(":")[0] : ""
    def proxyPort = env.HTTPS_PROXY ? env.HTTPS_PROXY.split(":")[1] : ""
    def merchantId = env.SDK_MERCHANT_ID ?: "1000-xmlMerch"
    def username = env.SDK_USERNAME ?: "SDKUSR"
    def password = env.SDK_PASSWORD ?: "SDKPWD"
    def useSftp = env.SDK_SFTP_USERNAME && env.SDK_SFTP_PASSWORD //Avoid running SFTP tests if there are no credentials set
    def sftpUsername = env.SDK_SFTP_USERNAME ?: "SDKSFTPUSR"
    def sftpPassword = env.SDK_SFTP_PASSWORD ?: "SDKSFTPPWD"

    //Set ENV properties
    env.LITLE_CONFIG_DIR = "${tmpDir}"
    env.LITLE_CONFIG_FILE = "${env.LITLE_CONFIG_DIR}/.litle_SDK_config.properties"

    def createConfigFile = """
echo "username=${username}" > $env.LITLE_CONFIG_FILE
echo "password=${password}" >> $env.LITLE_CONFIG_FILE
echo "merchantId=${merchantId}" >> $env.LITLE_CONFIG_FILE
echo "url=https://www.testlitle.com/sandbox/communicator/online" >> $env.LITLE_CONFIG_FILE
echo "proxyHost=${proxyHost}" >> $env.LITLE_CONFIG_FILE
echo "proxyPort=${proxyPort}" >> $env.LITLE_CONFIG_FILE
echo "timeout=500" >> $env.LITLE_CONFIG_FILE
echo "readTimeout=60000" >> $env.LITLE_CONFIG_FILE
echo "printxml=true" >> $env.LITLE_CONFIG_FILE
echo "reportGroup=Default Report Group" >> $env.LITLE_CONFIG_FILE
echo "maxAllowedTransactionsPerFile=500000" >> $env.LITLE_CONFIG_FILE
echo "maxTransactionsPerBatch=100000" >> $env.LITLE_CONFIG_FILE
echo "batchRequestFolder=${tmpDir}/javaBatchRequests" >> $env.LITLE_CONFIG_FILE
echo "batchResponseFolder=${tmpDir}/javaBatchResponses" >> $env.LITLE_CONFIG_FILE
echo "batchTcpTimeout=7200000" >> $env.LITLE_CONFIG_FILE
echo "sftpTimeout=400000" >> $env.LITLE_CONFIG_FILE
echo "batchPort=15000" >> $env.LITLE_CONFIG_FILE
echo "batchUseSSL=true" >> $env.LITLE_CONFIG_FILE
echo "batchHost=prelive.litle.com" >> $env.LITLE_CONFIG_FILE
echo "sftpUsername=${sftpUsername}" >> $env.LITLE_CONFIG_FILE
echo "sftpPassword=${sftpPassword}" >> $env.LITLE_CONFIG_FILE
"""

    //---
    stage "Workspace Preparation"

    deleteDir()
    sh "ls -lah"
    sh "env | sort | tee build.env"

    //---
    stage "Checkout"

    // Checkout code from repository
    checkout scm

    //---
    stage "Build"

    //branch name from Jenkins environment variables
    echo "My branch is: ${env.BRANCH_NAME}"

    sh createConfigFile
    sh "$gradleExec clean assemble $gradleOptions"

    //---
    stage "Test"

    def exclusions = useSftp ? "" : "-x certificationTest"

    sh "$gradleExec jacocoTestReport $exclusions $gradleOptions "

    //---
    stage "Create Artifacts"

    sh "$gradleExec kit -x check $gradleOptions"

    //TODO Additional steps to save archive results/publish can be added here
}
