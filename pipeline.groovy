pipeline {
	agent any
	stages {
		stage ('code-pull') {
			steps {
				git branch: 'dev', url: 'https://github.com/Vanshit-2011/Van-project-frontend.git'
			}
		}
		stage('code-build') {
			steps {
				sh '''
					docker build . -t vanshit967/angular-frontend:latest
					docker push vanshit967/angular-frontend:latest
					npm install
					ng build
				'''
			}
		}
		stage('code-deploy') {
			steps {
				withCredentials([aws(accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws-creds', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
	sh '''
		aws s3 cp --recursive dist/angular-frontend s3://vanshit-project-bucket/
	'''
}
				
				
			}
		}
	}
}
