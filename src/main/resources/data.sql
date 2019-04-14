-- CADASTRANDO ROLES
INSERT INTO AUTHORITY (ID, ROLE) VALUES (1, 'ROLE_COMUM');
INSERT INTO AUTHORITY (ID, ROLE) VALUES (2, 'ROLE_ADMIN');

-- CADASTRANDO USUÁRIOS
INSERT INTO USER(ID, EMAIL, PASSWORD, ENABLED) VALUES (1, 'admin@sector7.com', '$2a$10$AENAKs83RNwJjbYZ/KORPOY4KtPdY2jqITc2eydHVwZ/4HmhOPJ.e', true);
INSERT INTO USER(ID, EMAIL, PASSWORD, ENABLED) VALUES (2, 'guest@sector7.com', '$2a$10$F340vLyEPt/SotNkVt.w5uNJtUXZp5ukT8BtffOgjXrH.k3qv32p.', true);

-- ATRIBUINDO ROLES AO USUÁRIOS
INSERT INTO USER_AUTHORITIES (ID_USER, ID_AUTHORITY) VALUES (1, 2);
INSERT INTO USER_AUTHORITIES (ID_USER, ID_AUTHORITY) VALUES (2, 1);