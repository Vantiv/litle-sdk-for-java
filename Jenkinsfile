//Define settings for build
def tmpDir = "/tmp"
def proxyHost, proxyPort = env.HTTPS_PROXY?.split(":") ?: ["", ""]
def merchantId = env.SDK_MERCHANT_ID ?: "1000-xmlMerch"
def username = env.SDK_USERNAME ?: "SDKUSR"
def password = env.SDK_PASSWORD ?: "SDKPWD"
def useSftp = env.SDK_SFTP_USERNAME && env.SDK_SFTP_PASSWORD //Avoid running SFTP tests if there are no credentials set
def sftpUsername = env.SDK_SFTP_USERNAME ?: "SDKSFTPUSR"
def sftpPassword = env.SDK_SFTP_PASSWORD ?: "SDKSFTPPWD"

//Set ENV properties
env.LITLE_CONFIG_DIR="${tmpDir}"
env.LITLE_CONFIG_FILE="${env.LITLE_CONFIG_DIR}/.litle_SDK_config.properties"

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

node { //Can pass in a String arg that specifies which label to build on, ex. node("unix") {
    //---
    // Checkout code from repository and update any submodules
    stage 'Checkout'

    // Checkout code from repository and update any submodules
    checkout scm
    sh 'git submodule update --init'

    //---
    stage 'Build'

    //branch name from Jenkins environment variables
    echo "My branch is: ${env.BRANCH_NAME}"

    if(isUnix()){
        sh createConfigFile
        sh './gradlew clean assemble --info'
    }
    else{
        bat createConfigFile //TODO Confirm this works on Windows
        bat './gradlew.bat clean assemble --info'
    }

    //---
    stage 'Test'

    def exclusions = useSftp ? "" : "-x certificationTest"


    if(isUnix()){
        sh './gradlew jacocoTestReport $useSftp --info -Dhttps.protocols=TLSv1.1,TLSv1.2'
    }
    else{
        bat './gradlew.bat jacocoTestReport $useSftp --info -Dhttps.protocols=TLSv1.1,TLSv1.2'
    }

    //---
    stage 'Create Artifacts'

    if(isUnix()){
        sh './gradlew kit -x check --info'
    }
    else{
        bat './gradlew.bat kit -x check --info'
    }

    //TODO Additional steps to save archive results/publish can be added here
}
