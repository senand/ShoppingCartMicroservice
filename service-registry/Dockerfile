#We need jdk 11 as a base image to be used
FROM openjdk:11
#We need to get jar file from target folder
ARG JAR_FILE=target/*.jar
#We are copying the jar file inside the image and renaming it
COPY ${JAR_FILE} serviceregistry.jar
#To start the jar file need to pass command java -jar serviceregistry.jar
#and that command need to run when the container start
ENTRYPOINT ["java", "-jar", "/serviceregistry.jar"]

#Exposing on which port so my host can access that port
EXPOSE 8761

