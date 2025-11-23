# 1. Usamos uma imagem base com Java 21 (igual ao seu pom.xml)
FROM eclipse-temurin:21-jdk-alpine

# 2. Criamos uma pasta de trabalho dentro do container
WORKDIR /app

# 3. Copiamos o arquivo .jar que o Maven gerou para dentro do container
# (Vamos gerar esse arquivo jajá com o comando mvn package)
COPY target/marketplace_project-0.0.1-SNAPSHOT.jar app.jar

# 4. Expomos a porta 8080 (padrão do Spring)
EXPOSE 8080

# 5. Comando para rodar a aplicação quando o container subir
ENTRYPOINT ["java", "-jar", "app.jar"]