FROM openjdk:11-jre-slim
COPY target/*SNAPSHOT.jar admin.jar
EXPOSE 8082
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/admin.jar",,"-Dspring.profiles.active=dev", "--spring.profiles.active=dev"]
