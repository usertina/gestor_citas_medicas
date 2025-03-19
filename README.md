# Introducción
El Sistema de Gestión de Citas Médicas es una aplicación diseñada para la
administración eficiente de médicos, pacientes y citas médicas. Utiliza Java para la
lógica del programa, Swing para la interfaz gráfica, y MySQL para el manejo de datos. La
base de datos estárealizada con php myAdmin y levantado con Docker.

# Árbol de Navegación
El proyecto está organizado de la siguiente manera:
src/
├── modelo/
│ ├── MedicoDAO.java
│ ├── PacienteDAO.java
│ ├── UsuarioDAO.java
│ ├── CitaDAO.java
│ ├── FechaUtil.java
│ └── Conexion.java
├── vista/
│ ├── LoginPanel.java
│ ├── PanelBienvenida.java
│ ├── PanelCitas.java
│ ├── PanelMedicos.java
│ ├── PanelPacientes.java
│ └── VentanaPrincipal.java
└── conexión sesión.sql

# Descripción de Clases

## Clases en modelo

### MedicoDAO: Clase encargada de realizar operaciones CRUD (Crear, Leer, Actualizar y
Eliminar) sobre los médicos.

● insertarMedico: Agrega un médico a la base de datos.
● eliminarMedico: Elimina un médico según nombre y apellido.
● consultarMedicos: Recupera una lista de todos los médicos registrados.
● modificarMedico: Modifica los datos de un médico existente.

### PacienteDAO: Maneja las operaciones relacionadas con pacientes.

● insertarPaciente: Registra un paciente en la base de datos y en un archivo de
texto.
● obtenerUltimoPaciente: Recupera el último paciente registrado.
● obtenerEspecialidades: Obtiene la lista de especialidades disponibles.

### UsuarioDAO: Gestiona la autenticación de usuarios.

● verificarUsuario: Comprueba si un usuario y contraseña son válidos.

### CitaDAO: Se encarga de manejar las citas médicas.

● obtenerCitasOcupadas: Recupera horarios ocupados de un médico en una fecha
especı́fica.
● generarHorariosDisponibles: Crea una lista de horarios disponibles.
● insertarCita: Registra una nueva cita en la base de datos.

### FechaUtil: Proporciona utilidades relacionadas con fechas y horarios.

● generarFechasLaborales: Genera una lista de fechas laborales.
● generarTodosLosHorarios: Crea horarios en un rango definido.

### Conexion: Gestiona la conexión a la base de datos MySQL.

● conectar: Establece la conexión.
● cerrarConexion: Cierra la conexión abierta.
● verificarLogin: Valida credenciales de usuario.

## Clases en vista

### LoginPanel: Panel para el inicio de sesión que interactúa con la clase Conexion.

● Campos: usuarioField, contrasenaField, mostrarContrasenaButton.
● Acciones: Validación de credenciales y cambio de panel a la pantalla de bienvenida..

### PanelBienvenida: Panel que muestra un mensaje de bienvenida con un texto animado.

### PanelCitas: Interfaz gráfica para la gestión de citas médicas.

● Selección de pacientes, especialidades, médicos, fechas y horarios
● Llama a métodos de CitaDAO y PacienteDAO.

### PanelMedicos: Permite gestionar a los médicos.

#### Funciones:

● Agregar médicos
● eliminar médicos
● modificar médicos
● consultar médicos

### PanelPacientes: Panel para añadir pacientes y actualizar dinámicamente la lista en PanelCitas.

### VentanaPrincipal: Ventana principal del sistema que administra los paneles.

● Modo oscuro.
● Navegación entre paneles.
● Salida segura.

## Flujo de Ejecución

El sistema inicia con la clase VentanaPrincipal, que muestra el LoginPanel.
El usuario se autentica a través de Conexion.
Dependiendo de las opciones del menú, se despliegan los paneles: PanelMedicos,
PanelPacientes o PanelCitas.
Los paneles interactúan con las clases DAO para gestionar datos.
Los datos se almacenan y consultan desde la base de datos MySQL.

Manejo de logs.

## Conclusión y Mejoras

### Conclusión

El Sistema de Gestión de Citas Médicas ofrece una solución funcional para la gestión de un
consultorio médico. Combina una interfaz gráfica intuitiva con acceso eficiente a una base de
datos.

### Mejoras Futuras

● Implementar validaciones más estrictas para entradas de usuario.
● Añadir un sistema de roles para limitar accesos según tipo de usuario.
● Migrar a una interfaz web para mayor accesibilidad.
● Soporte para múltiples idiomas.
