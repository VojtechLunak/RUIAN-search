#!/bin/bash

getContainerHealth () {
  docker inspect --format "{{json .State.Health.Status }}" $1
}

waitContainer () {
  while STATUS=$(getContainerHealth $1); [ "$STATUS" != "\"healthy\"" ]; do
    if [ "$STATUS" = "\"unhealthy\"" ]; then
      echo "Failed!"
      exit 1
    fi
    sleep 1
  done
  echo "$1 is ready"
}

waitContainers () {
  waitContainer solr
  waitContainer backend
  #waitContainer frontend
}

if [ "$1" == "build" ]; then
	docker-compose up --build -d
else
	docker-compose up -d
fi
echo "Docker-compose is running"
waitContainers
echo "Application is ready"