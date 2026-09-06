# Sesión 6 — Permisos en Android

## 1. ¿Qué son los permisos?

En Android, cada app corre aislada de las demás dentro de un **Sandbox**. Para acceder a recursos
fuera de su sandbox (cámara, contactos, internet, ubicación, etc.), la app debe pedir permisos explícitos
llamados **"permissions"**.

- Según qué tan sensible sea el recurso, Android **autoriza automáticamente** o **pregunta al usuario**.
- ⚠️ **Una aplicación NO tiene ningún permiso por defecto.**

## 2. Historia: antes y después de Android 6 (API 23)

| Hasta Android 5.1 (API 22) | Desde Android 6 (API 23) |
|---|---|
| Los permisos se piden **al instalar** la app (o en actualizaciones) | Los permisos se piden **en tiempo de ejecución (runtime)** |
| Se autorizan **todos o ninguno** (en grupo) | Se manejan **de forma individual**: unos se pueden aceptar y otros rechazar |
| La única forma de quitar un permiso es **desinstalar** la app | El usuario puede **quitar el permiso en cualquier momento** |
| — | La app debe **verificar el permiso cada vez que se ejecuta** (pudo cambiar) |

## 3. Tipos de permisos

### Normales
Poco riesgo para la privacidad/funcionamiento. **Android los autoriza automáticamente.**

> Internet, bluetooth, NFC, alarmas, zona horaria, vibración, wallpaper, audio, etc.

### Runtime / con riesgo
Involucran información privada, modifican datos guardados, o afectan otras apps. **El usuario debe
darlos explícitamente.**

> Calendario, cámara, contactos, localización fina y gruesa, grabar audio, registro de llamadas, SMS,
> leer/escribir en almacenamiento externo.

## 4. Flujo oficial de solicitud de permisos (Android)

1. Declarar el permiso en el `manifest`.
2. Diseñar la UI para que sea autoexplicativa.
3. Esperar a que el usuario solicite la acción específica.
4. ¿El permiso ya fue concedido? → si **sí**, acceder al recurso directamente.
5. Si **no** → decidir si mostrar un **rationale** (explicación de por qué se necesita el permiso).
6. Solicitar el permiso (se muestra el diálogo del sistema).
7. ¿El usuario lo concede?
   - **Sí** → acceder al recurso protegido.
   - **No** → degradar la experiencia de la app con gracia (no romper la app).

## 5. Definir permisos en el Manifest

Se hace en `AndroidManifest.xml`, fuera de la etiqueta `<application>`:

```xml
<!-- Use Internet -->
<uses-permission android:name="android.permission.INTERNET"/>
<!-- Use contactos para lectura -->
<uses-permission android:name="android.permission.READ_CONTACTS"/>
<!-- Use Camara -->
<uses-permission android:name="android.permission.CAMERA"/>
<!-- Use Almacenamiento para lectura -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<!-- Use Almacenamiento para escritura -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

Esto define qué permisos **necesita** la app para funcionar (declararlos no significa que ya estén
concedidos — los "de riesgo" igual hay que pedirlos en tiempo de ejecución).

## 6. Google Accompanist (permisos en Compose)

Librería de Jetpack Compose que **simplifica** la integración, solicitud y manejo de permisos.

```gradle
dependencies {
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
}
```

⚠️ Es un flujo **experimental**: hay que anotar el composable que lo usa con:
```kotlin
@OptIn(ExperimentalPermissionsApi::class)
```

## 7. Flujo de uso de permisos con Accompanist (paso a paso)

**Paso 1** — Crear una variable de estado para el permiso:
```kotlin
val permissionState = rememberPermissionState(
    android.Manifest.permission.CAMERA
)
```
(`Manifest.permission` es la clase de Android con todos los permisos disponibles).

**Paso 2** — Verificar si ya fue concedido:
```kotlin
if (permissionState.status.isGranted) {
    // Permission is granted
} else {
    // Permission is not granted
}
```

**Paso 3** — Si no fue concedido, revisar si se debe mostrar una explicación (rationale):
```kotlin
if (permissionState.status.shouldShowRationale) {
    // Show a rationale
}
```

**Paso 4** — Si no aplica ninguna de las anteriores, el usuario aún no lo ha concedido: solicitarlo
(desde un `onClick` o un `LaunchedEffect`):
```kotlin
permissionState.launchPermissionRequest()
```

### Ejemplo completo

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PantallaConCamara() {
    val permissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    when {
        permissionState.status.isGranted -> {
            Text("¡Permiso concedido! Aquí iría la cámara.")
        }
        permissionState.status.shouldShowRationale -> {
            Text("Necesitamos la cámara para tomar la foto de tu perfil.")
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Dar permiso")
            }
        }
        else -> {
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }
        }
    }
}
```

Este flujo con Accompanist es **mucho más simple** que el flujo tradicional en XML, donde los permisos
se solicitan en el `onCreate()` de la Activity y se manejan en `onRequestPermissionsResult()`.
También se pueden solicitar **permisos en grupo** con Accompanist.
