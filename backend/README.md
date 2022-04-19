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