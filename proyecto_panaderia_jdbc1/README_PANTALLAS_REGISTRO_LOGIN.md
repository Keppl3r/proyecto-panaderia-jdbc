# Pantallas de Registro e Inicio de Sesión - Pantojarte Panadería

## 📋 Descripción General

Se han implementado dos pantallas adicionales para el sistema de Pantojarte Panadería:

1. **Pantalla de Registro**: Para nuevos usuarios que desean crear una cuenta
2. **Pantalla de Inicio de Sesión**: Para usuarios existentes que desean acceder al sistema

## 🎨 Características de Diseño

### Elementos Comunes en Ambas Pantallas

- **Banner Lateral Izquierdo**: Imagen grande de canastas con panes que ocupa todo el lado izquierdo
- **Panel Derecho**: Formulario con fondo degradado en tonos crema
- **Logo Superior**: Logo de Pantojarte con el nombre estilizado
- **Tarjeta Amarilla**: Formulario contenido en una tarjeta dorada (#f4c430)
- **Decoración**: Pequeña imagen de pan en la tarjeta
- **Botón "Volver"**: Para regresar a la pantalla principal

## 📝 Pantalla de Registro (registro.fxml)

### Campos del Formulario:

1. **Nombres** (obligatorio)
2. **Apellido paterno** (obligatorio)
3. **Apellido materno**
4. **Domicilio** (obligatorio)
5. **Fecha de nacimiento** (obligatorio) - DatePicker con formato dd/mm/aaaa
6. **Apellido paterno** (segunda fila)
7. **Apellido materno** (segunda fila)

### Botones:

- **Entrar**: Botón marrón oscuro (#6b5644) que procesa el registro
- **← Volver**: Link para regresar a la pantalla principal

### Validaciones Implementadas:

- Verifica que los campos obligatorios no estén vacíos
- Muestra alertas informativas para guiar al usuario
- Muestra mensaje de éxito al completar el registro
- Retorna automáticamente a la pantalla principal tras el registro exitoso

### Tamaño de Ventana:
- **Ancho**: 800px
- **Alto**: 600px

---

## 🔐 Pantalla de Inicio de Sesión (login.fxml)

### Campos del Formulario:

1. **Nombre** (obligatorio) - Campo de texto para usuario
2. **Contraseña** (obligatorio) - Campo de contraseña oculta

### Botones:

- **Entrar**: Botón naranja (#f39c12) que procesa el inicio de sesión
- **← Volver al inicio**: Link para regresar a la pantalla principal

### Funcionalidades Implementadas:

- Validación de campos vacíos
- Campo de contraseña con caracteres ocultos (PasswordField)
- Navegación con tecla Enter entre campos
- Presionar Enter en el campo de contraseña ejecuta el login
- Mensaje de error si las credenciales son incorrectas
- Limpia el campo de contraseña tras un intento fallido
- **Validación temporal**: Acepta cualquier usuario con contraseña "123456"

### Tamaño de Ventana:
- **Ancho**: 750px
- **Alto**: 550px

---

## 🔧 Controladores Implementados

### RegistroController.java

**Métodos principales:**

- `handleEntrar()`: Procesa el registro del nuevo usuario
  - Valida campos obligatorios
  - Muestra información en consola (preparado para integración con BD)
  - Muestra mensaje de éxito
  - Navega de vuelta a la pantalla principal

- `handleVolver()`: Regresa a la pantalla principal

- `initialize()`: Configura efectos hover y formato de fecha

- `addHoverEffect()`: Agrega efectos visuales al pasar el mouse sobre botones

**Campos capturados:**
- Nombres, apellidos (paterno/materno), domicilio, fecha de nacimiento

---

### LoginController.java

**Métodos principales:**

- `handleEntrar()`: Procesa el inicio de sesión
  - Valida campos vacíos
  - Llama a método de validación de credenciales
  - Muestra mensaje de éxito o error según corresponda
  - Limpia campo de contraseña si falla

- `validarCredenciales()`: **Método temporal** para validar usuario/contraseña
  - **NOTA**: Actualmente acepta cualquier usuario con contraseña "123456"
  - **DEBE SER REEMPLAZADO** con consulta a base de datos

- `handleVolver()`: Regresa a la pantalla principal

- `initialize()`: 
  - Configura efectos hover
  - Configura navegación con Enter entre campos

- `addHoverEffect()`: Agrega efectos visuales al pasar el mouse sobre botones

---

## 🔄 Navegación entre Pantallas

### Flujo de Navegación:

```
Pantalla Principal (main_panaderia.fxml)
    │
    ├─→ Botón "Iniciar Sesión" → login.fxml
    │                              │
    │                              └─→ Botón "Volver" → Regresa a main_panaderia.fxml
    │
    └─→ Botón "Registrarse" → registro.fxml
                                │
                                └─→ Botón "Volver" → Regresa a main_panaderia.fxml
```

### Actualización en MainPanaderiaController:

Se modificaron los métodos:

- `handleIniciarSesion()`: Ahora navega a `login.fxml`
- `handleRegistrarse()`: Ahora navega a `registro.fxml`

---

## 📁 Archivos Creados

### Recursos FXML:
1. `src/main/resources/com/mycompany/proyecto_panaderia_jdbc1/registro.fxml`
2. `src/main/resources/com/mycompany/proyecto_panaderia_jdbc1/login.fxml`

### Controladores Java:
1. `src/main/java/com/mycompany/proyecto_panaderia_jdbc1/RegistroController.java`
2. `src/main/java/com/mycompany/proyecto_panaderia_jdbc1/LoginController.java`

### Imágenes:
1. `src/main/resources/com/mycompany/proyecto_panaderia_jdbc1/banner.png`
2. `src/main/resources/com/mycompany/proyecto_panaderia_jdbc1/logo_2.png`

---

## 🚀 Cómo Ejecutar

### Ejecutar la aplicación:

```bash
cd C:\Users\devor\Proyectos\Programacion\Java\BasesDeDatosAvanzadas2026\proyecto-panaderia-jdbc\proyecto_panaderia_jdbc1
mvn clean javafx:run
```

### Probar el Inicio de Sesión (temporal):

- **Usuario**: Cualquier nombre
- **Contraseña**: `123456`

---

## 🎯 Próximos Pasos para Integración con Base de Datos

### 1. Pantalla de Registro:

**En RegistroController.handleEntrar():**

```java
// Crear objeto Usuario
Usuario nuevoUsuario = new Usuario();
nuevoUsuario.setNombres(txtNombres.getText());
nuevoUsuario.setApellidoPaterno(txtApellidoP.getText());
nuevoUsuario.setApellidoMaterno(txtApellidoM.getText());
nuevoUsuario.setDomicilio(txtDomicilio.getText());
nuevoUsuario.setFechaNacimiento(dateFechaNacimiento.getValue());

// Guardar en BD
UsuarioDAO dao = new UsuarioDAO();
boolean exito = dao.insertarUsuario(nuevoUsuario);

if (exito) {
    // Mostrar mensaje de éxito y continuar
} else {
    // Mostrar mensaje de error
}
```

### 2. Pantalla de Login:

**En LoginController.validarCredenciales():**

```java
private boolean validarCredenciales(String nombre, String password) {
    UsuarioDAO dao = new UsuarioDAO();
    Usuario usuario = dao.buscarPorNombre(nombre);
    
    if (usuario != null) {
        // Verificar contraseña (idealmente hasheada)
        return usuario.getPassword().equals(hashPassword(password));
    }
    return false;
}
```

### 3. Crear Clases DAO:

- `UsuarioDAO.java`: Para operaciones CRUD de usuarios
- `ConexionBD.java`: Para gestionar conexión a base de datos
- `Usuario.java`: Modelo de datos para usuarios

---

## 💡 Mejoras Sugeridas

### Seguridad:
1. **Hashear contraseñas**: Usar BCrypt o similar para almacenar contraseñas
2. **Validación de campos**: Agregar validación de formato (email, teléfono, etc.)
3. **Sesiones**: Implementar manejo de sesiones de usuario

### Funcionalidad:
1. **Recuperación de contraseña**: Agregar opción "¿Olvidaste tu contraseña?"
2. **Validación de duplicados**: Verificar que el usuario no exista antes de registrar
3. **Campos adicionales**: Email, teléfono, confirmación de contraseña

### UX/UI:
1. **Indicadores de fortaleza de contraseña**
2. **Mostrar/ocultar contraseña** con botón de ojo
3. **Autocompletado** para campos de dirección
4. **Mensajes de error en tiempo real** mientras el usuario escribe

---

## 🛠️ Tecnologías Utilizadas

- **JavaFX 13**: Framework de interfaz gráfica
- **FXML**: Diseño declarativo de interfaces
- **CSS inline**: Estilos personalizados
- **Maven**: Gestión de dependencias y compilación

---

**Estado**: ✅ Compilado exitosamente  
**Fecha**: Febrero 2026  
**Proyecto**: Sistema de Gestión para Panadería - Bases de Datos Avanzadas


