# Estágio 1: Build
FROM node:20-alpine as build

WORKDIR /app

# Copia os arquivos de configuração
COPY package*.json pnpm-lock.yaml ./

# --- A CORREÇÃO ESTÁ AQUI ---
# Adicionamos --legacy-peer-deps para ignorar o conflito de versão do Vite
RUN npm install --legacy-peer-deps

# Copia o código fonte
COPY . .

# Builda o projeto
RUN npm run build

# Estágio 2: Nginx para servir
FROM nginx:alpine

# Copia o build do estágio anterior
COPY --from=build /app/dist /usr/share/nginx/html

# Configuração do Nginx para React Router
RUN echo 'server { \
    listen 80; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html index.htm; \
        try_files $uri $uri/ /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
