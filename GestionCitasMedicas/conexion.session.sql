 CREATE TABLE medicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    especialidad VARCHAR(50),
    sala VARCHAR(10)
);
 

  CREATE TABLE pacientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    email VARCHAR(150) NOT NULL
);
 

 CREATE TABLE citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente VARCHAR(100),
    medico VARCHAR(100),
    especialidad VARCHAR(50),
    sala VARCHAR(50),
    fecha DATE,
    hora TIME,
    UNIQUE (medico, fecha, hora) -- Para evitar citas duplicadas
);


    CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contraseña VARCHAR(100) NOT NULL
);

-- Insertar un usuario de prueba
INSERT INTO usuarios (usuario, contraseña) VALUES ('admin', '1234');
 



INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Juan', 'Perez', 'Cardiología', 'A100');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Ana', 'Gomez', 'Pediatría', 'B101');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Carlos', 'Lopez', 'Cardiología', 'A102');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Luis', 'Martinez', 'Neurología', 'C103');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Marta', 'Fernandez', 'Ginecología', 'B104');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Pedro', 'Diaz', 'Dermatología', 'D105');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Laura', 'Sanchez', 'Neurología', 'C106');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Raul', 'Garcia', 'Pediatría', 'B107');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Eva', 'Rodriguez', 'Cardiología', 'A108');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Sofia', 'Vazquez', 'Ginecología', 'B109');
INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES ('Javier', 'Martinez', 'Dermatología', 'D110');

 


