# Sistema de Gestión de Usuarios y Auditoría – Azure Functions + Oracle

Este proyecto implementa un backend **serverless** en **Java** sobre **Azure Functions**, con persistencia en **Oracle** (usando Wallet) y auditoría de eventos siguiendo **Clean Architecture**.

---

## 🚀 Características principales

- **CRUD de usuarios**: Crear, actualizar, eliminar y consultar usuarios.
- **Auditoría de eventos**: Cada operación relevante genera un evento y se persiste en la tabla `eventos_auditoria` de Oracle.
- **Publicación de eventos**: Los eventos se publican en Azure Event Grid para integración y trazabilidad.
- **Arquitectura limpia**: Separación clara de capas (dominio, aplicación, infraestructura, función).
- **Conexión segura**: Uso de Oracle Wallet y variables de entorno para credenciales.

---

## 📁 Estructura del proyecto

```
azf-user/
├── src/
│   └── main/
│       └── java/com/function/
│           ├── application/usecase/      # Casos de uso (lógica de negocio)
│           ├── domain/model/             # Modelos de dominio y eventos
│           ├── infrastructure/           # Repositorios, EventGrid, etc.
│           ├── function/                 # Azure Functions (entrypoints)
│           └── util/                     # Utilidades y mappers
├── wallet/                               # Archivos Oracle Wallet
├── local.settings.json                    # Configuración local y secrets
├── pom.xml
└── README.md
```

---

## ⚙️ Funcionalidades Serverless

| Función                  | Método | Descripción                                                        |
|--------------------------|--------|--------------------------------------------------------------------|
| `CreateUser`             | POST   | Crea un usuario y audita la operación                              |
| `UpdateUser`             | PUT    | Actualiza un usuario y audita la operación                         |
| `DeleteUser`             | DELETE | Elimina un usuario y audita la operación                           |
| `GetUserById`            | GET    | Consulta un usuario por ID                                         |
| `GetAllUsers`            | GET    | Lista todos los usuarios                                           |
| `ProcessEventGridEvent`  | Event  | Persiste en auditoría cada evento recibido desde Event Grid        |
| `testOracleConnection`   | GET    | Prueba la conectividad a Oracle usando Wallet                      |

---

## 🧾 Auditoría de eventos

Cada vez que se realiza una operación CRUD relevante, se genera un evento con un tipo explícito (`user.created`, `user.updated`, `user.deleted`, `user.queried`) y se persiste en la tabla `eventos_auditoria` de Oracle:

| Campo         | Descripción                                 |
|---------------|---------------------------------------------|
| id_evento     | UUID del evento                             |
| tipo_evento   | Tipo de operación (`user.created`, etc.)    |
| fecha_evento  | Fecha y hora del evento (TIMESTAMP)         |
| datos         | Datos relevantes del evento (JSON)          |

---

## 🛠️ Configuración y ejecución local

1. **Configura las variables en `local.settings.json`:**

```json
{
  "IsEncrypted": false,
  "Values": {
    "FUNCTIONS_WORKER_RUNTIME": "java",
    "ORACLE_USER": "back_vet",
    "ORACLE_PASSWORD": "**********",
    "ORACLE_TNS_NAME": "**********",
    "ORACLE_WALLET_PATH": "C:/ruta/completa/al/wallet",
    "EVENT_GRID_TOPIC_ENDPOINT": "...",
    "EVENT_GRID_TOPIC_KEY": "..."
  }
}
```

2. **Compila y ejecuta localmente:**

```bash
mvn clean package
mvn azure-functions:run
```

3. **Prueba las funciones con Postman o curl:**

- **POST /api/CreateUser**
- **PUT /api/UpdateUser/{id}**
- **DELETE /api/DeleteUser/{id}**
- **GET /api/GetUserById/{id}**
- **GET /api/GetAllUsers**
- **GET /api/testOracleConnection**

---

## ☁️ Despliegue en Azure

1. Inicia sesión en Azure:
   ```bash
   az login
   ```
2. Despliega con Maven:
   ```bash
   mvn azure-functions:deploy
   ```

---

## 🧰 Principales dependencias

- `azure-functions-java-library`
- `azure-messaging-eventgrid`
- `ojdbc11` y librerías Oracle Wallet
- `JUnit` y `Mockito` para pruebas

---

## 🏆 Buenas prácticas y arquitectura

- **Clean Architecture**: Separación de dominio, aplicación, infraestructura y función.
- **Logs claros y profesionales**: Solo logs clave para monitoreo y troubleshooting.
- **Variables de entorno**: Seguridad y portabilidad.
- **Documentación y comentarios Javadoc** en todo el código relevante.
- **Pruebas unitarias y de integración**.

---

## 📚 Recursos útiles

- [Documentación Azure Functions Java](https://learn.microsoft.com/es-es/azure/azure-functions/functions-reference-java)
- [Documentación Oracle Wallet](https://docs.oracle.com/en/database/oracle/oracle-database/19/jjdev/using-oracle-wallets.html)
- [Event Grid Java SDK](https://learn.microsoft.com/en-us/java/api/overview/azure/messaging-eventgrid-readme?view=azure-java-stable)

---

## 👨‍💻 Autor y contacto

Pedro Rivera  
[GitHub: PedroRiveraDuoc](https://github.com/PedroRiveraDuoc/azf-user.git)

---