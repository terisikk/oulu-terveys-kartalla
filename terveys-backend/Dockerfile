FROM clojure:latest

WORKDIR /app/
COPY ./ /app

RUN lein ring uberjar
CMD java -jar target/terveys-backend.jar
