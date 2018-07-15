String version = "0.1.0.${env.BUILD_NUMBER}"

node {
    try {
        stage('Checkout') {
            cleanWs()
            checkout scm
        }
        stage('Build') {
            echo "My branch is: ${env.BRANCH_NAME}"
            withSonarQubeEnv('localhostSonarQube') {
                bat "gradlew.bat --info clean build sonarqube -Dinoks.java.monads.version=${version}"
            }
            currentBuild.description = version
        }
        stage('Archive') {
            archiveArtifacts artifacts: 'build/**/*.jar'
        }
    } catch (Throwable t) {
        throw t
    }
}
