CREATE TABLE ReadEase.Usuarios (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo NVARCHAR(20) DEFAULT 'Usu�rio' NOT NULL,
    telemovel INT NOT NULL,
    data_de_nascimento DATE NOT NULL
);

CREATE TABLE ReadEase.Livros (
    id INT IDENTITY(1,1) PRIMARY KEY,
    t�tulo NVARCHAR(255) NOT NULL,
    autor NVARCHAR(255) NOT NULL,
    isbn NVARCHAR(20) NOT NULL,
	editora NVARCHAR(255) NOT NULL,
    ano_publica��o INT,
    g�nero NVARCHAR(100),
    descri��o NVARCHAR(MAX),
);


CREATE TABLE ReadEase.Salas (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(100) NOT NULL,
    capacidade INT NOT NULL,
    disponibilidade BIT NOT NULL,
);

CREATE TABLE ReadEase.favoritos (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_livro INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES ReadEase.Usuarios(id),
    FOREIGN KEY (id_livro) REFERENCES ReadEase.Livros(id)
);

CREATE TABLE ReadEase.Ebooks (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_livro INT NOT NULL,
    formato NVARCHAR(20),
    tamanho INT,
    arquivo NVARCHAR(255) NOT NULL,
    FOREIGN KEY (id_livro) REFERENCES ReadEase.Livros(id)
);

CREATE TABLE ReadEase.Reviews (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_livro INT NOT NULL,
    comentario NVARCHAR(MAX),
    classificaçao INT,
    FOREIGN KEY (id_usuario) REFERENCES ReadEase.Usuarios(id),
    FOREIGN KEY (id_livro) REFERENCES ReadEase.Livros(id)
);

CREATE TABLE ReadEase.ReservasSalas (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_sala INT NOT NULL,
    data_reserva DATE NOT NULL,
    data_fim_reserva DATE NOT NULL,
    sala_status BIT NOT NULL,
	n_pessoas int,
    FOREIGN KEY (id_usuario) REFERENCES ReadEase.Usuarios(id),
    FOREIGN KEY (id_sala) REFERENCES ReadEase.Salas(id)
    FOREIGN KEY (sala_status) REFERENCES ReadEase.Salas(disponibilidade)
);

CREATE TABLE ReadEase.ReservasEbooks (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_ebook INT NOT NULL,
    data_reserva DATE NOT NULL,
    data_fim_reserva DATE NOT NULL,
	status NVARCHAR(20) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES ReadEase.Usuarios(id),
    FOREIGN KEY (id_ebook) REFERENCES ReadEase.Ebooks(id)
);

CREATE TABLE ReadEase.ReservasLivros (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_livro INT NOT NULL,
    data_reserva DATE NOT NULL,
    data_fim_reserva DATE NOT NULL,
    status NVARCHAR(20) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES ReadEase.Usuarios(id),
    FOREIGN KEY (id_livro) REFERENCES ReadEase.Livros(id)
);

CREATE TABLE ReadEase.Eventos (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(255) NOT NULL,
    data_inicio DATETIME NOT NULL,
    data_fim DATETIME NOT NULL,
    descricao NVARCHAR(MAX),
	organizador NVARCHAR(255) NOT NULL
);


CREATE TABLE ReadEase.Sala_notes (
    id INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_reservasala INT NOT NULL,
    note VARCHAR(MAX),
    FOREIGN KEY (id_usuario) REFERENCES ReadEase.Usuarios(id),
    FOREIGN KEY (id_reservasala) REFERENCES ReadEase.ReservasSalas(id)
);


CREATE TABLE ReadEase.alert (
    reservationId INT NOT NULL,
    alertTime DATETIME NOT NULL,
    alertType NVARCHAR(255) NOT NULL,
);

CREATE TABLE ReadEase.extension_request_alert (
    id INT IDENTITY(1,1) PRIMARY KEY,
    reservation_id INT NOT NULL,
    requestDate DATETIME NOT NULL,
    status NVARCHAR(255) NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES ReadEase.book_reserves(id)
);


CREATE TABLE ReadEase.notification (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    message NVARCHAR(MAX) NOT NULL,
    timestamp DATETIME NOT NULL,
    bookId NVARCHAR(255) NOT NULL,
    isread BIT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES ReadEase.Usuarios(id)
);
