#!/bin/bash

mvn compile
mvn package
java -jar target/street-fighter-0.1.0.jar $1