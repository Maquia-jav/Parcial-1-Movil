# Sesión 4 — Listas dinámicas, REST API y Navegación

## 1. Listas: LazyColumn y LazyRow

Muestran listas de elementos de forma eficiente (solo renderizan lo visible en pantalla).

```kotlin
LazyColumn {
    // Un solo elemento
    item { Text(text = "First item") }

    // Varios elementos por cantidad
    items(5) { index -> Text(text = "Item: $index") }

    // Otro elemento suelto
    item { Text(text = "Last item") }
}
```

Para iterar sobre una lista real de datos se usa `items(lista) { elemento -> ... }`:

```kotlin
LazyColumn {
    items(messages) { message -> MessageRow(message) }
}
```

Si necesitas el índice del elemento: `itemsIndexed(lista) { index, elemento -> ... }`.

Parámetros útiles de `LazyColumn`/`LazyRow`: `verticalArrangement`, `horizontalArrangement`, `modifier`,
`contentPadding`, `reverseLayout`.

## 2. Grid: LazyVerticalGrid / LazyHorizontalGrid

```kotlin
LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 128.dp)
) {
    items(photos) { photo -> PhotoItem(photo) }
}
```

**Sticky headers** (experimental) — encabezados que quedan fijos mientras se hace scroll:

```kotlin
LazyColumn {
    stickyHeader { Header() }
    items(items) { item -> ItemRow(item) }
}
```

## 3. ListItem (Material 3)

Componente ya armado para mostrar un elemento de lista con secciones predefinidas:

```kotlin
ListItem(
    leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
    headlineContent = { Text("John Doe") },
    supportingContent = { Text("3 minutes ago") },
    trailingContent = { Icon(Icons.Default.MoreVert, contentDescription = null) }
)
```

| Parámetro | Qué muestra |
|---|---|
| `headlineContent` | contenido principal |
| `overlineContent` | encima del contenido principal |
| `supportingContent` | contenido secundario (debajo) |
| `leadingContent` | a la izquierda (ícono, foto) |
| `trailingContent` | a la derecha (ícono, flecha) |

## 4. REST API

Un REST API es un servicio web que usa métodos HTTP para operar sobre recursos:

| Método | Acción |
|---|---|
| GET | Obtiene un recurso |
| POST | Crea un recurso |
| PUT | Actualiza un recurso completo |
| PATCH | Actualiza parcialmente |
| DELETE | Elimina un recurso |

### Ktor Client (librería usada en el curso)

Dependencias (`build.gradle`):
```gradle
implementation("io.ktor:ktor-client-core:$ktor_version")
implementation("io.ktor:ktor-client-okhttp:$ktor_version")
implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
implementation("io.ktor:ktor-client-logging:$ktor_version")
```

Crear el cliente:
```kotlin
private val client = HttpClient(OkHttp) {
    defaultRequest { url("https://miapi.com/api/") }
    install(Logging) { logger = Logger.SIMPLE }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}
```

Hacer una petición (nota el `suspend`, que hace la función asíncrona — corre en otro hilo, no el principal):
```kotlin
suspend fun getUsers(): UsersList {
    return client.get("users").body()
}
```

⚠️ **Muy importante**: agregar el permiso de internet en `AndroidManifest.xml` (por defecto una app NO lo tiene):
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Consumir el API desde un Composable usando `remember` + `LaunchedEffect` (para que la petición se haga solo una vez):
```kotlin
val apiClient = KtorClient()
var users by remember { mutableStateOf(listOf<User>()) }

LaunchedEffect(key1 = null) {
    users = apiClient.getUsers().results
}
```

## 5. Imágenes e íconos

```kotlin
Image(
    painter = painterResource(R.drawable.image),
    contentDescription = "Image",
    contentScale = ContentScale.Crop
)

Icon(Icons.Default.Person, contentDescription = "Person")
```

`Image` no maneja caché ni errores al cargar desde una URL. Para eso se usa **Coil** (u otras como Glide/Picasso):

```gradle
implementation("io.coil-kt.coil3:coil-compose:3.1.0")
implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
```

```kotlin
AsyncImage(
    model = "https://example.com/image.jpg",
    contentDescription = null
)
```

## 6. Navegación entre pantallas

### Compose es "Single Activity"

- **Forma tradicional (XML)**: cada pantalla = una `Activity`. Se navega creando un `Intent` y llamando
  `startActivity()`. Comunicación entre Activities limitada (solo key-value en el Intent).
- **Compose**: no se usan Activities para cada pantalla, sino **@Composables**. La navegación se maneja
  con la librería **Navigation** (en el curso, **Navigation 3**).

### Conceptos clave de Navigation 3

- **NavKey**: objeto `@Serializable` que representa una ruta/pantalla y los datos que viajan con ella.
- **NavBackStack**: pila que define qué pantalla se muestra (se navega agregando/quitando de la pila).
- **NavDisplay**: composable principal, muestra la pantalla correspondiente a la ruta actual.
- **entryProvider**: mapea cada `NavKey` a su composable correspondiente.
- Las rutas son **type-safe**: los datos que viajan entre pantallas se verifican en compilación.

Dependencias:
```gradle
plugins {
    kotlin("plugin.serialization") version "<version-de-kotlin>"
}
dependencies {
    implementation("androidx.navigation3:navigation3-runtime:1.0.1")
    implementation("androidx.navigation3:navigation3-ui:1.0.1")
}
```

Definir rutas:
```kotlin
@Serializable
data object HomeRoute : NavKey

@Serializable
data class DetailsRoute(val userId: String) : NavKey
```

Armar la navegación:
```kotlin
val backstack = rememberNavBackStack(HomeRoute)

NavDisplay(
    modifier = modifier,
    backStack = backstack,
    onBack = { backstack.removeLastOrNull() },
    entryProvider = { key ->
        when (key) {
            HomeRoute -> NavEntry(key) { HomeScreen() }
            is DetailsRoute -> NavEntry(key) { DetailsScreen() }
            is SettingsRoute -> NavEntry(key) { SettingsScreen() }
            else -> NavEntry(key) { NotFoundScreen() }
        }
    }
)
```

Navegar a otra pantalla (agregar a la pila):
```kotlin
backstack.add(DetailsRoute(userId = "123"))
```

También se puede quitar elementos de la pila o borrarla completa/por secciones.
