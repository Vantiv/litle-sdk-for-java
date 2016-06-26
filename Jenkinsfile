
node { //Can pass in a String arg that specifies which label to build on, ex. node("unix") {

    // Checkout code from repository and update any submodules
    stage 'Checkout'

    // Checkout code from repository and update any submodules
    checkout scm
    sh 'git submodule update --init'

    stage 'Build'

    //branch name from Jenkins environment variables
    echo "My branch is: ${env.BRANCH_NAME}"

    if(isUnix()){
        sh './gradlew clean assemble --info'

    }
    else{
        bat './gradlew clean assemble --info'
    }

    stage 'Test'

    if(isUnix()){
        sh './gradlew check --info'

    }
    else{
        bat './gradlew check assemble --info'
    }

    //TODO Additional steps to archive/publish can be added here
}
