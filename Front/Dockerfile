# --- Estágio 1: Build da Aplicação ---
FROM node:20-alpine AS build

WORKDIR /app

# 1. Copia dependências
COPY package.json pnpm-lock.yaml* package-lock.json* ./

# 2. Copia patches (necessário para o pnpm install funcionar)
COPY patches ./patches

# 3. Instala dependências
RUN if [ -f pnpm-lock.yaml ]; then \
        npm install -g pnpm && pnpm install; \
    else \
        npm ci; \
    fi

# 4. Copia código fonte
COPY . .

# 5. Build (Gera a pasta dist/public para o frontend)
RUN npm run build

# --- Estágio 2: Servidor Nginx (Produção) ---
FROM nginx:alpine

# [CORREÇÃO AQUI] Aponta para dist/public em vez de apenas dist
COPY --from=build /app/dist/public /usr/share/nginx/html

# Copia conf do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
