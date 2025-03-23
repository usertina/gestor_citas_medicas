# Sistema de Gestión de Citas Médicas

El **Sistema de Gestión de Citas Médicas** es una aplicación diseñada para la administración eficiente de médicos, pacientes y citas médicas. Utiliza **Java** para la lógica del programa, **Swing** para la interfaz gráfica, y **MySQL** para el manejo de datos. La base de datos se gestiona a través de **phpMyAdmin** y se ejecuta en un entorno **Docker**.

![Java](https://img.shields.io/badge/Java-%2317044b.svg?style=flat&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-%2300f.svg?style=flat&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-%23007B7F.svg?style=flat&logo=docker&logoColor=white)
![phpMyAdmin](https://img.shields.io/badge/phpMyAdmin-%232C3E50.svg?style=flat&logo=phpmyadmin&logoColor=white)

## Flujo de Ejecución

1. **Inicio**: El sistema comienza con la clase `VentanaPrincipal`, que muestra el `LoginPanel`.
2. **Autenticación**: El usuario se autentica a través de la clase `Conexion`.
3. **Menú de opciones**: Dependiendo de las opciones seleccionadas, se despliegan los siguientes paneles:
   - `PanelMedicos`
   - `PanelPacientes`
   - `PanelCitas`
4. **Gestión de datos**: Los paneles interactúan con las clases DAO para realizar operaciones CRUD sobre la base de datos.
5. **Base de datos**: Todos los datos (médicos, pacientes, citas) se almacenan y consultan desde la base de datos MySQL.
6. **Manejo de logs**: El sistema registra las acciones realizadas para facilitar la depuración y auditoría.

## Conclusión y Mejoras

### Conclusión

El Sistema de Gestión de Citas Médicas ofrece una solución funcional para la gestión de un consultorio médico. Combina una interfaz gráfica intuitiva con acceso eficiente a una base de datos.

### Mejoras Futuras

1. **Validaciones más estrictas** para entradas de usuario.
2. **Sistema de roles** para limitar accesos según el tipo de usuario.
3. **Migración a una interfaz web** para mayor accesibilidad.
4. **Soporte para múltiples idiomas**.
5. **Integración de notificaciones por correo electrónico** para confirmar citas médicas y recordatorios.

## Clonar el Repositorio

Para clonar el repositorio en tu máquina local, utiliza el siguiente comando de Git:

```bash
git clone https://github.com/usertina/gestor_citas_medicas.git
```
## Requisitos

Antes de ejecutar el programa, asegúrate de tener instalados los siguientes programas:

Java 8 o superior: El programa está desarrollado en Java, por lo que es necesario tener el JDK instalado en tu sistema.

Docker: Para ejecutar la base de datos MySQL en un contenedor Docker, asegúrate de tener Docker instalado.

MySQL: El sistema utiliza MySQL para el manejo de la base de datos.

phpMyAdmin (opcional, pero recomendado): Para gestionar la base de datos de manera sencilla.

## Ejecutar el Programa

Paso 1: Configurar la Base de Datos

1. En la raíz del proyecto, encontrarás el archivo conexión_sesion.sql. Este archivo contiene el script para crear la base de datos y las tablas necesarias.


2. Asegúrate de tener Docker instalado y ejecuta el siguiente comando para levantar un contenedor de MySQL:

```bash
docker run --name mysql-citas -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=citas_medicas -p 3306:3306 -d mysql:latest
```
3. Una vez que el contenedor esté ejecutándose, puedes conectarte a la base de datos con un cliente como phpMyAdmin o MySQL Workbench usando las siguientes credenciales:

Host: localhost

Puerto: 3306

Usuario: root

Contraseña: root

4. Ejecuta el script conexión_sesion.sql para crear las tablas necesarias en la base de datos.

## Ejecutar el Proyecto con un IDE

Si prefieres usar un IDE para ejecutar el proyecto, sigue estos pasos:

1. Importa el Proyecto en tu IDE:

Abre tu IDE preferido (Eclipse, IntelliJ IDEA, NetBeans, etc.).

Importa el proyecto como un proyecto Java existente.

2. Compila y Ejecuta:

Asegúrate de tener configurada la conexión a la base de datos correctamente.

Compila y ejecuta la clase VentanaPrincipal.java. Esta clase iniciará la aplicación y mostrará la interfaz gráfica.

En Eclipse o IntelliJ IDEA, puedes hacer clic derecho sobre la clase VentanaPrincipal.java y seleccionar "Ejecutar" o "Run".



