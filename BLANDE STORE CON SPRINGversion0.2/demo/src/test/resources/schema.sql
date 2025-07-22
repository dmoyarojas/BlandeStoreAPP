-- Drop tables if they exist to ensure a clean state
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS tipo_ropa;
DROP TABLE IF EXISTS usuario;

-- Create tables
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(255) NOT NULL
);

CREATE TABLE tipo_ropa (
    id_tipo BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(50) NOT NULL
);

CREATE TABLE categoria (
    id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(50) NOT NULL,
    id_tipo BIGINT NOT NULL,
    FOREIGN KEY (id_tipo) REFERENCES tipo_ropa(id_tipo)
);

CREATE TABLE producto (
    codigo_barras VARCHAR(50) PRIMARY KEY,
    id_tipo BIGINT NOT NULL,
    id_categoria BIGINT NOT NULL,
    talla VARCHAR(10),
    color VARCHAR(30),
    precio DECIMAL(10, 2) NOT NULL,
    vendido BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (id_tipo) REFERENCES tipo_ropa(id_tipo),
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);
