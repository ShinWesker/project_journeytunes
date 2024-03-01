@echo "starting start skript"
docker volume create project_journeytunes_mysql-db
docker run --rm -v project_journeytunes_mysql-db:/ziel -v %~dp0default_database_volume\journeytunes_mysql-db.tar.gz:/quelle alpine tar -xzvf /quelle -C /ziel
docker-compose up --build