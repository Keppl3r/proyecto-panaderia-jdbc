# Pantalla Principal - Pantojarte Panadería

## 📋 Descripción

Esta es la pantalla principal de la aplicación **Pantojarte Panadería**, desarrollada con JavaFX. La interfaz presenta dos opciones principales para los usuarios:

1. **Pedido Express**: Para productos listos para recoger de inmediato
2. **Pedido Programado**: Para clientes registrados que desean programar sus pedidos

## 🎨 Características de Diseño

### Elementos Visuales

- **Logo**: Imagen decorativa de pan en la parte superior
- **Título Estilizado**: "Pantojarte Panadería" con fuentes elegantes
- **Tarjetas Interactivas**: Dos tarjetas con diseño moderno y efectos de sombra
- **Colores Cálidos**: Paleta de colores amarillos/dorados que evocan el pan recién horneado
- **Fondo Degradado**: Efecto visual suave con tonos crema

### Funcionalidades

#### Tarjeta "Pedido Express"
- **Botón "Ver Catálogo"**: Muestra los productos disponibles para entrega inmediata
- **Tiempo de Espera**: Indica que los productos estarán listos en 20 minutos
- **Icono Decorativo**: Pequeña imagen de pan con transparencia

#### Tarjeta "Pedido Programado"
- **Botón "Iniciar Sesión"**: Para clientes ya registrados (con icono de candado 🔒)
- **Botón "Registrarse"**: Para nuevos usuarios (con icono de lápiz ✏)
- **Icono Decorativo**: Pequeña imagen de pan con transparencia

## 🚀 Cómo Ejecutar

### Opción 1: Desde la línea de comandos
```bash
cd C:\Users\devor\Proyectos\Programacion\Java\BasesDeDatosAvanzadas2026\proyecto-panaderia-jdbc\proyecto_panaderia_jdbc1
mvn clean javafx:run
```

### Opción 2: Desde NetBeans
1. Abrir el proyecto en NetBeans
2. Hacer clic derecho en el proyecto
3. Seleccionar "Run"

## 📁 Archivos Creados

### Interfaz FXML
- **Ubicación**: `src/main/resources/com/mycompany/proyecto_panaderia_jdbc1/main_panaderia.fxml`
- **Descripción**: Define la estructura visual de la pantalla principal

### Controlador Java
- **Ubicación**: `src/main/java/com/mycompany/proyecto_panaderia_jdbc1/MainPanaderiaController.java`
- **Descripción**: Maneja los eventos de los botones y la lógica de la interfaz

### Recursos
- **Logo**: `src/main/resources/com/mycompany/proyecto_panaderia_jdbc1/pan.png`
- **Estilos CSS**: `src/main/resources/com/mycompany/proyecto_panaderia_jdbc1/styles.css`

## 🔧 Configuración Modificada

### App.java
Se modificó el archivo principal para cargar la nueva pantalla:
- Tamaño de ventana: 900x700 píxeles
- Título: "Pantojarte Panadería"
- Ventana no redimensionable para mantener el diseño

## 💡 Funcionalidades Implementadas

1. **Efectos Hover**: Los botones tienen efectos visuales al pasar el mouse sobre ellos
2. **Alertas de Información**: Al presionar los botones, se muestran alertas informativas
3. **Diseño Responsivo**: Los elementos se ajustan correctamente en la ventana
4. **Animaciones Suaves**: Transiciones visuales en los botones

## 📝 Próximos Pasos

Para completar la aplicación, puedes crear:

1. **Pantalla de Catálogo**: Para mostrar los productos disponibles
2. **Pantalla de Login**: Para que los usuarios inicien sesión
3. **Pantalla de Registro**: Para que nuevos usuarios se registren
4. **Conexión a Base de Datos**: Para almacenar y recuperar información de productos y usuarios

## 🎯 Métodos del Controlador

### `handleVerCatalogo()`
Se ejecuta al presionar "Ver Catálogo". Aquí puedes implementar la navegación a la pantalla del catálogo de productos.

### `handleIniciarSesion()`
Se ejecuta al presionar "Iniciar Sesión". Aquí puedes implementar la navegación a la pantalla de login.

### `handleRegistrarse()`
Se ejecuta al presionar "Registrarse". Aquí puedes implementar la navegación a la pantalla de registro.

### `initialize()`
Se ejecuta automáticamente al cargar la pantalla. Configura los efectos hover de los botones.

## 🛠️ Tecnologías Utilizadas

- **JavaFX 13**: Framework para interfaces gráficas
- **FXML**: Para el diseño declarativo de la interfaz
- **CSS**: Para estilos personalizados
- **Maven**: Para la gestión de dependencias

---

**Desarrollado para**: Proyecto de Bases de Datos Avanzadas 2026  
**Tema**: Sistema de Gestión para Panadería


