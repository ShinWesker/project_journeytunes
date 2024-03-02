#!/bin/bash

echo "starting start script"
docker volume create project_journeytunes_mysql-db
docker run --rm -v project_journeytunes_mysql-db:/ziel -v "$(pwd)"/default_database_volume/project_journeytunes_mysql-db.tar.gz:/quelle alpine tar -xzvf /quelle -C /ziel
docker-compose up --build