FROM amazoncorretto:21-alpine-jdk

RUN apk update
RUN apk upgrade --available
RUN apk add --no-cache htop
RUN apk add --no-cache bash
RUN apk add --no-cache ffmpeg


RUN mkdir -p /out
COPY target/*.jar /out/app.jar
COPY docker/entrypoint.sh .
RUN (cd /out ; jar -xf app.jar)
RUN mkdir /app
RUN ls /out/
RUN cp -r /out/BOOT-INF/lib /app/lib
RUN cp -r /out/META-INF /app/META-INF
RUN cp -r /out/BOOT-INF/classes/* /app

RUN ls -R /app
RUN mkdir /gui
RUN chmod +x entrypoint.sh
ENTRYPOINT ./entrypoint.sh
