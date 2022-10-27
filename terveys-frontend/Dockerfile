FROM clojure:latest AS builder

RUN curl -sL https://deb.nodesource.com/setup_16.x  | bash -
RUN apt-get -y install nodejs

WORKDIR /app/
COPY ./ /app

RUN lein ring uberjar
CMD java -jar target/uberjar/terveys-backend.jar
