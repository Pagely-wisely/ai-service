FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app
ARG GPR_USER
ARG GPR_KEY
ENV GPR_USER=$GPR_USER
ENV GPR_KEY=$GPR_KEY
COPY . .
RUN gradle clean bootJar -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar ai-service.jar
ENTRYPOINT ["java", "-jar", "ai-service.jar", "--spring.profiles.active=prod"]