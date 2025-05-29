FROM openjdk:21-ea-jdk-oracle
EXPOSE 8080
COPY target/countries-aws-sb.jar countries-aws-sb.jar
ENTRYPOINT ["java","-jar","countries-aws-sb.jar"]