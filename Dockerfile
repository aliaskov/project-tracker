FROM openjdk:8
ADD target/project-tracker.jar project-tracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "project-tracker.jar"]
