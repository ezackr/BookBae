# Bookbae Backend

## Introduction

This is the backend software that drives the BookBae app. 
It is a 'Jakarta RESTful Web Services' application that can be deployed to any standards-compliant Java EE web server. 
It exposes a REST API that clients will use to read and modify data.

## Building

The application is built with 'gradle build' (or possibly './gradlew build' or '.\gradlew build')
The above command builds a file 'build/libs/api.war' which can be deployed to a Jakarta EE server like TomEE

## Running

To run this application, you will need a Jakarta EE server. The server will come with instructions that will tell you how to deploy the api.war file built by this application

The application can often be deployed by uploading api.war to the server's builtin web management interface - both TomEE and Glassfish support this. 

## User Authentication

As written, the application uses JWT - the client gets a JWT token from the /login endpoint and places it in the Authorization header for all subsequent API calls.
