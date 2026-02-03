CREATE TABLE artista (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL
);

CREATE TABLE album (
                       id SERIAL PRIMARY KEY,
                       titulo VARCHAR(150) NOT NULL,
                       artista_id INTEGER NOT NULL,
                       CONSTRAINT fk_artista FOREIGN KEY (artista_id) REFERENCES artista(id)
);

INSERT INTO artista (nome) VALUES ('Serj Tankian'), ('Mike Shinoda'), ('Michel Teló'), ('Guns N Roses');