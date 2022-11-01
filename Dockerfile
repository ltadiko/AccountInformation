# Start with a base image containing Java runtime
FROM fundingsocietiesdocker/openjdk19-alpine


# Add maintainer
LABEL maintainer="Account Information APIS (tlaxman88@gmail.com)"

# Create exploded jar in the image
COPY target/dependency/BOOT-INF/classes /app
COPY target/dependency/BOOT-INF/lib /app/lib
COPY target/dependency/META-INF /app/META-INF
WORKDIR /app

# Run the application on startup
ENTRYPOINT ["java","-cp",".:lib/*","nl.rabo.accountinformation.AccountInformationApplication"]

# Only for documentation purposes
EXPOSE 8080
