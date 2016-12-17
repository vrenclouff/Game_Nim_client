#!/bin/bash

mvn clean package

java -jar target/nimClient.jar "$@"
