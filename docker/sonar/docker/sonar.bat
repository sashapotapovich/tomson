cd application
mvn clean install -P release sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=3a79ef06e3db33faf4a94cdfd36bf60d7da7b226  -Dsonar.branch=master
pause
