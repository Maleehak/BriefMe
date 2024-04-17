FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y \
    python3 \
    wget \
    curl \
    ffmpeg \
    && rm -rf /var/lib/apt/lists/*

RUN wget https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -O /usr/local/bin/yt-dlp && \
    chmod a+rx /usr/local/bin/yt-dlp

COPY backend/target/briefme-0.0.1-SNAPSHOT.jar /app/briefme-0.0.1-SNAPSHOT.jar
COPY google-api-credentials.json.enc  /app/google-api-credentials.json.enc

RUN openssl enc -d -aes-256-cbc -in /app/google-api-credentials.json.enc -out /app/google-api-credentials.json -pass pass:'It is raining 99'

WORKDIR /app

EXPOSE 8080
ENTRYPOINT ["java","-jar","briefme-0.0.1-SNAPSHOT.jar"]