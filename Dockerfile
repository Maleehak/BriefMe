FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y \
    python3 \
    wget \
    curl \
    ffmpeg \
    && rm -rf /var/lib/apt/lists/*

RUN wget https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -O /usr/local/bin/yt-dlp && \
    chmod a+rx /usr/local/bin/yt-dlp

WORKDIR /app
COPY . /app

COPY backend/target/briefme-0.0.1-SNAPSHOT.jar briefme-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","briefme-0.0.1-SNAPSHOT.jar"]