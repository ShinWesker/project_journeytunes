# Basisimage
FROM node:latest

# Arbeitsverzeichnis im Container
WORKDIR /app

# Kopieren von package.json und package-lock.json
COPY package.json .

# Abhängigkeiten installieren
RUN npm install

# Kopieren des Projektinhalts
COPY . .
EXPOSE 3000
# Startbefehl
CMD ["npm", "run", "dev"]
