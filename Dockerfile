FROM public.ecr.aws/docker/library/openjdk:17-ea-jdk-alpine
EXPOSE 8080
COPY target/countries-aws-sb.jar countries-aws-sb.jar
ENTRYPOINT ["java","-jar","countries-aws-sb.jar"]