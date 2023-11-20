#The docker image we want to start
FROM openjdk:17

COPY /build/libs/dockercustomerconsumptiontime-1.0-SNAPSHOT-all.jar /myapp.jar

# Set the entry point: This commamd will run when the cntainer starts
ENTRYPOINT ["java", "-jar", "myapp.jar"]