# Artifact Repository

## Introduction
This is a simple artifact repository that can be used to store and retrieve artifacts. It is a simple REST API that can be used to upload and download files.

## API
Upload file curl example

````shell
curl -i -X POST -H "Content-Type: multipart/form-data" -F "file=@file.zip"  -F "group=org.awesome.application;version=latest" http://localhost:8085/api/v1/file/upload/org.awesome.application.latest
````

Download file curl example
````shell
curl http://localhost:8085/api/v1/file/download/org.awesome.application.latest -o file.zip
````

## Swagger Docs
The API documentation can be found at http://localhost:8085/swagger-ui.html