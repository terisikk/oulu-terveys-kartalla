FROM clojure:latest AS builder

RUN curl -sL https://deb.nodesource.com/setup_16.x  | bash -
RUN apt-get -y install nodejs

WORKDIR /app/
COPY ./ /app
RUN npm config set user 0 && npm config set unsafe-perm true && npm install
RUN npx shadow-cljs release terveys-frontend

FROM nginx:alpine

WORKDIR /usr/share/nginx/html/
COPY --from=builder /app/public/ ./
