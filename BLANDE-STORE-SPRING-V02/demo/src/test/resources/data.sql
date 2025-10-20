-- Insert test data
INSERT INTO tipo_ropa (id_tipo, nombre_tipo) VALUES (1, 'Polo');
INSERT INTO tipo_ropa (id_tipo, nombre_tipo) VALUES (2, 'Pantalon');

INSERT INTO categoria (id_categoria, nombre_categoria, id_tipo) VALUES (1, 'Algodon', 1);
INSERT INTO categoria (id_categoria, nombre_categoria, id_tipo) VALUES (2, 'Jean', 2);

INSERT INTO producto (codigo_barras, id_tipo, id_categoria, talla, color, precio, vendido) 
VALUES ('PROD001', 1, 1, 'M', 'Azul', 25.50, FALSE);

INSERT INTO producto (codigo_barras, id_tipo, id_categoria, talla, color, precio, vendido) 
VALUES ('PROD002', 2, 2, '32', 'Negro', 75.00, FALSE);

INSERT INTO usuario (id, username, password, rol)
VALUES (1, 'testadmin', 'password', 'ADMIN');

INSERT INTO usuario (id, username, password, rol)
VALUES (2, 'testcajero', 'password', 'cajero');
