FROM openjdk:8
#FROM openjdk:9
ADD target/project-tracker.jar project-tracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "project-tracker.jar"]
#ENTRYPOINT ["java", "-jar", "--add-modules=java.se.ee", "project-tracker.jar"]
