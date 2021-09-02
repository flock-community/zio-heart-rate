FROM openjdk:14-alpine
WORKDIR /app
COPY target/scala-2.13/scala-demo.jar /app
ENV server_port=8080
EXPOSE 8080

CMD java -jar scala-demo.jar
