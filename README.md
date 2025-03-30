# Sistema de Gestión de Usuarios y Roles – Arquitectura Serverless con Azure Functions

Este proyecto corresponde al desarrollo de un sistema backend utilizando arquitectura **Serverless** con **Azure Functions en Java**, conectado a una base de datos **Oracle con Wallet**, en el contexto del ramo **Desarrollo Cloud Native II (DSY2207)**.

## 🧠 Objetivo del proyecto

Implementar un sistema de backend desacoplado que permita realizar operaciones básicas de gestión de usuarios y asignación de roles mediante funciones **serverless** desarrolladas en **Java**, cumpliendo con los principios de arquitectura moderna y buenas prácticas cloud.

## 📁 Estructura del proyecto

```bash
azf-user/
├── src/
│   └── main/
│       └── java/com/function/
│           ├── CreateUserFunction.java
│           ├── AssignRoleFunction.java
│           ├── Function.java (test conexión)
│           ├── OracleDBConnection.java
│           └── HttpResponseMessageMock.java (para testing)
├── local.settings.json
├── pom.xml
└── wallet/ (carpeta con archivos del Oracle Wallet)
```

## ⚙️ Funcionalidades

Este sistema implementa las siguientes funciones serverless:

| Función           | Método | Descripción                                      |
|------------------|--------|--------------------------------------------------|
| `CreateUser`     | POST   | Crea un usuario nuevo en la tabla `usuarios`     |
| `AssignRole`     | POST   | Asigna un rol a un usuario (`usuario_roles`)     |
| `testOracleConnection` | GET    | Verifica conectividad con Oracle vía Wallet      |

## 🧪 Pruebas locales

1. Configura las variables en `local.settings.json`:

```json
{
  "IsEncrypted": false,
  "Values": {
    "FUNCTIONS_WORKER_RUNTIME": "java",
    "ORACLE_USER": "back_vet",
    "ORACLE_PASSWORD": "**********",
    "ORACLE_TNS_NAME": "**********",
    "ORACLE_WALLET_PATH": "C:/ruta/completa/al/wallet"
  }
}
```

2. Ejecuta el siguiente comando:

```bash
mvn clean package
mvn azure-functions:run
```

3. Prueba con Postman:

- **POST /api/CreateUser**  
  Cuerpo: `Juan Perez,juan@email.com`

- **POST /api/AssignRole**  
  Query: `?userId=1&rolId=2` o Cuerpo: `1,2`

- **GET /api/testOracleConnection**

## ☁️ Despliegue en Azure

El despliegue se realiza con el plugin de Maven incluido en el `pom.xml`. Ejecuta:

```bash
az login
mvn azure-functions:deploy
```

Las funciones estarán disponibles en:
```
https://azf-createuser.azurewebsites.net/api/CreateUser?
https://azf-createuser.azurewebsites.net/api/AssignRole?
```

## 🧾 Dependencias clave

- `azure-functions-java-library`
- `ojdbc11`, `oraclepki`, `osdt_core`, `osdt_cert`
- `JUnit` y `Mockito` para pruebas

## 🧰 Buenas prácticas aplicadas

- Uso de variables de entorno seguras para conexión.
- Conexión a Oracle mediante Wallet.
- Funciones pequeñas, especializadas y sin estado (`stateless`).
- Pruebas de conexión y logs claros para observabilidad.
- Organización modular por responsabilidades.

## 🎓 Requisitos cumplidos según pauta de evaluación

✅ Mínimo 2 funciones serverless implementadas  
✅ Microservicio con lógica desacoplada y pruebas exitosas  
✅ Conexión a Oracle con wallet correctamente configurada  
✅ Uso de GIT y control de versiones  
✅ Docker y Azure listos para despliegue

## 👨‍🏫 Proyecto para evaluación de la Semana 3 (DSY2207)

> Este desarrollo corresponde a la Actividad Sumativa N°1 del ramo *Desarrollo Cloud Native II*, Semana 3. El sistema será presentado en un video demostrativo en conjunto con el código fuente.

---

## 🎥 Video de presentación

🔗 [Pega aquí el enlace de la grabación de Teams]

## 🔗 Repositorio del proyecto

🔗 https://github.com/PedroRiveraDuoc/azf-user.git

---
