FROM openjdk:8
ADD target/project-tracker.jar project-tracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "project-tracker.jar"]