FROM java:8
MAINTAINER  Andrei Groza, andrei.groza@servustech.com

VOLUME /tmp
ADD build/libs/Employee.jar app.jar
EXPOSE 8080
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongodb/micros", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]