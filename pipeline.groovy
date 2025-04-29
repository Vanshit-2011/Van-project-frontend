pipeline {
	agent any
	stages {
		stage ('code-pull') {
			steps {
				git branch: 'dev', url: 'https://github.com/srngx/project-frontend.git'
			}
		}
		stage('code-build') {
			steps {
				sh '''
					docker build . -t archsarangx/angular-frontend:latest
					docker push archsarangx/angular-frontend:latest
					npm install
					ng build
				'''
			}
		}
		stage('code-deploy') {
			steps {
				withCredentials([aws(accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws-creds', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
	sh '''
		aws s3 cp --recursive dist/angular-frontend s3://cbz-2025-batch-latest-frontend-project-bux/
	'''
}
				
				
			}
		}
	}
}
