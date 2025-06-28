# Usa uma imagem base Java 21 LTS para o build
FROM eclipse-temurin:21-jdk-jammy as builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo build.gradle e settings.gradle
COPY build.gradle settings.gradle ./

# Copia o diretório src
COPY src ./src

# Copia o gradlew e o wrapper
COPY gradlew .
COPY gradle ./gradle

# Dá permissão de execução ao gradlew
RUN chmod +x gradlew

# Baixa as dependências e constrói o JAR
# Usa --no-daemon para garantir que o Gradle não rode em segundo plano após o build
# Usa --stacktrace para mais detalhes em caso de erro
RUN ./gradlew bootJar --no-daemon --stacktrace

# Usa uma imagem base menor para o runtime, apenas com a JRE
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR construído da fase de build para o container final
COPY --from=builder /app/build/libs/*.jar app.jar

# Expõe a porta que sua aplicação Spring Boot usa (padrão é 8080)
EXPOSE 8080

# Comando para rodar a aplicação quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]