FROM eclipse-temurin:17-jre-alpine

ENV TZ=Asia/Seoul

COPY build/libs/ddakdae-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
