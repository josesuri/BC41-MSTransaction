FROM openjdk:8-jdk-alpine
ADD target/*.jar /usr/share/ms-transaction.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/usr/share/ms-transaction.jar"]