# ⚙️ Configuración del proyecto en Android Studio (LEE ESTO PRIMERO)

Sin este paso, ningún código del repo te va a compilar. Hazlo una sola vez por proyecto.

## 1. Crear el proyecto

`File > New > New Project > Empty Activity`

⚠️ Asegúrate de elegir la plantilla que trae **Jetpack Compose** (en Android Studio moderno,
"Empty Activity" ya es la de Compose; NO elijas "Empty Views Activity", esa es la de XML).

## 2. Dónde vive tu código

- Tu proyecto tiene una carpeta tipo `app/src/main/java/com/tunombre/tuapp/`. Ahí está `MainActivity.kt`.
- Todo lo que pegues de este repo debe quedar **en esa misma carpeta/paquete**, al lado de `MainActivity.kt`.

## 3. Cómo pegar el código de este repo (2 formas)

**Opción A — Todo en un solo archivo (más simple para el examen):**
Pega las funciones `@Composable` completas **directamente dentro de `MainActivity.kt`**,
por FUERA de la clase `MainActivity` (al final del archivo, a nivel de archivo). Luego, dentro de
`setContent { }` en la clase `MainActivity`, llama a la función principal, por ejemplo:
```kotlin
setContent {
    MiAppTheme {
        Ejercicio1App() // <- la función que armamos en ejemplos/Ejercicio1.kt
    }
}
```

**Opción B — Archivo separado (más ordenado):**
Clic derecho sobre la carpeta de tu paquete (donde está `MainActivity.kt`) → `New > Kotlin Class/File`
→ ponle el mismo nombre del archivo del repo (ej. `Ejercicio1`) → pega **todo el contenido tal cual**,
incluidos los imports de arriba. Debe quedar en el mismo paquete que `MainActivity.kt`.

## 3.1 ⚠️ SIEMPRE debes tocar MainActivity.kt (sin importar la Opción A o B)

Pegar el archivo (Opción B) **no lo muestra en pantalla por sí solo**. Compose necesita que
alguien "llame" a esa función desde algún lado — y ese punto de partida siempre es
`MainActivity.kt`, dentro de `setContent { }`. Así se ve por defecto un proyecto nuevo:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TuAppTheme { // este nombre lo pone Android Studio solo, no lo cambies
                // AQUÍ es donde llamas a la función principal del archivo que pegaste
                Ejercicio1App()
            }
        }
    }
}
```

Reemplaza `Ejercicio1App()` por la función que corresponda según qué archivo pegaste — usa esta tabla:

| Archivo que pegaste | Qué escribir dentro de `setContent { TuAppTheme { ... } }` |
|---|---|
| `ejemplos/Ejercicio1.kt` | `Ejercicio1App()` |
| `ejemplos/Ejercicio2.kt` | `Ejercicio2App()` |
| `ejemplos/Ejercicio3.kt` | `Ejercicio3App()` |
| `ejemplos/Ejercicio4.kt` | `Ejercicio4App()` |
| `codigo/03_Navegacion.kt` | `AppNavigation()` |
| `codigo/04_RestApiKtor.kt` | `PantallaDeUsuarios()` |
| `codigo/05_ViewModel.kt` | `PantallaContador()` |
| `codigo/07_Permisos.kt` | `PantallaConCamara()` (o `PantallaConUbicacion()`) |
| `codigo/10_EjercicioResuelto_PantallaPrincipal.kt` (+ debes pegar también `codigo/08_DropdownMenuYUrl.kt` en el mismo paquete, porque el 10 usa una función del 08) | `AppNavigationEjercicio1()` |
| `codigo/01_ComposeBasico.kt` | No es una app completa — son piezas sueltas. Llama la que necesites, ej. `PantallaEjemplo()` |
| `codigo/02_ListasYGrids.kt`, `06_Dialogs.kt`, `08_DropdownMenuYUrl.kt`, `09_ValidacionRangoYCalculo.kt` | No son pantallas completas por sí solas — son **piezas para usar DENTRO de otro composable tuyo** (reciben parámetros). No las llames solas desde `setContent`. |

Si Android Studio te subraya en rojo el nombre de la función dentro de `setContent`, es porque no
está en el mismo paquete que `MainActivity.kt` — revisa el punto 2 de esta guía.

## 4. Dependencias necesarias — `build.gradle.kts (Module :app)`

Abre ese archivo y agrega esto dentro del bloque `dependencies { }` que ya existe (no borres lo que
ya trae el proyecto por defecto, como `compose-bom` o `material3`):

```kotlin
dependencies {
    // ... lo que ya trae tu proyecto por defecto, no lo borres ...

    // Navigation 3 (carpetas 02, ejemplos)
    implementation("androidx.navigation3:navigation3-runtime:1.0.1")
    implementation("androidx.navigation3:navigation3-ui:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // ViewModel en Compose (carpeta 03)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Íconos completos de Material (Person, MoreVert, ArrowDropDown, Check...)
    implementation("androidx.compose.material:material-icons-extended")

    // Ktor - consumir REST APIs (codigo/04)
    implementation("io.ktor:ktor-client-core:3.0.3")
    implementation("io.ktor:ktor-client-okhttp:3.0.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.3")
    implementation("io.ktor:ktor-client-logging:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")

    // Coil - cargar imágenes desde una URL (codigo/02)
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")

    // Google Accompanist - permisos en Compose (carpeta 04, codigo/07)
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
}
```

También agrega el plugin de serialización (necesario para `@Serializable` de Navigation 3), arriba
del todo del mismo archivo, dentro del bloque `plugins { }`:

```kotlin
plugins {
    // ... lo que ya esté aquí, no lo borres ...
    kotlin("plugin.serialization") version "2.0.21"
}
```
⚠️ La versión de ese plugin debe ser **la misma versión de Kotlin que ya use tu proyecto** — revísala
en `Project Structure` (Ctrl+Alt+Shift+S) o en el `build.gradle.kts` de la raíz del proyecto.

Después de editar el `build.gradle.kts`, dale clic en **"Sync Now"** en la barra amarilla que aparece
arriba del editor.

## 5. Permisos — `AndroidManifest.xml`

Agrega SOLO los que vayas a usar, dentro de `<manifest>` y ANTES de `<application>`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

## 6. Sobre las imágenes (`R.drawable...`)

Donde el ejercicio dice "una imagen de tu preferencia", el código usa un ícono de Android que **ya
viene incluido** (`android.R.drawable...`) para que compile sin que agregues nada. Si quieres tu
propia imagen: arrastra el archivo `.png`/`.jpg` a `app/src/main/res/drawable/`, y donde diga
`painterResource(id = android.R.drawable.algo)` cámbialo por `painterResource(id = R.drawable.tu_imagen)`
(sin `android.` al inicio).

## 7. Si algo sale subrayado en rojo (import faltante)

Pon el cursor sobre la palabra en rojo y presiona **Alt + Enter** (Windows/Linux) o **Option + Enter**
(Mac), y elige la opción de **Import**. Esto es lo más confiable — especialmente para **Navigation 3**,
que es una librería muy nueva y el nombre exacto de import puede variar un poco entre versiones.
Todos los archivos de este repo ya traen sus imports en la parte de arriba, pero si tu versión de la
librería difiere un poco, Alt+Enter siempre resuelve el nombre correcto.
