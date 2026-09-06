# 🧠 Cheatsheet Rápido — Todo en una página

## 1. Composable básico + modificadores
```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hola $name!",
        modifier = modifier
            .background(Color.Blue)
            .padding(16.dp)
            .clickable { /* acción */ }
    )
}
```
Modificadores comunes: `padding`, `background`, `clickable`, `fillMaxWidth`, `fillMaxHeight`, `align`, `border`. Se encadenan con `.` y el orden importa.

## 2. Layouts
- `Column { }` → apila verticalmente
- `Row { }` → apila horizontalmente
- `Box { }` → posición absoluta / superponer elementos
- `Spacer(modifier = Modifier.height(8.dp))` → espacio en blanco
- `Scaffold(topBar = {}, bottomBar = {}) { innerPadding -> /* contenido */ }` → esqueleto de pantalla

## 3. Listas y Grids
```kotlin
LazyColumn {
    item { Text("Primer elemento") }
    items(lista) { elemento -> ItemRow(elemento) }
    itemsIndexed(lista) { index, elemento -> /* ... */ }
}

LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
    items(fotos) { foto -> FotoItem(foto) }
}
```

## 4. ViewModel (patrón completo — el más importante)
```kotlin
data class MiUiState(val dato: String = "")

class MiViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MiUiState())
    val uiState: StateFlow<MiUiState> = _uiState.asStateFlow()

    fun actualizarDato(nuevo: String) {
        _uiState.update { it.copy(dato = nuevo) }
    }
}

@Composable
fun MiPantalla(viewModel: MiViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Text(uiState.dato)
}
```
Flujo UDF: **Evento (UI) → el ViewModel actualiza el estado → la UI observa el estado y se redibuja.**

## 5. Dialog
```kotlin
AlertDialog(
    onDismissRequest = { },
    title = { Text("Título") },
    text = { Text("Mensaje") },
    confirmButton = { TextButton(onClick = { }) { Text("Aceptar") } },
    dismissButton = { TextButton(onClick = { }) { Text("Cancelar") } }
)
```

## 6. REST API con Ktor
```kotlin
val client = HttpClient(OkHttp) {
    defaultRequest { url("https://miapi.com/api/") }
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
}
suspend fun getUsers(): UsersList = client.get("users").body()

// En el Composable:
var users by remember { mutableStateOf(listOf<User>()) }
LaunchedEffect(key1 = null) { users = apiClient.getUsers().results }
```
⚠️ No olvidar el permiso en el manifest: `<uses-permission android:name="android.permission.INTERNET" />`

## 7. Navegación (Navigation 3)
```kotlin
@Serializable data object HomeRoute : NavKey
@Serializable data class DetailsRoute(val id: String) : NavKey

val backstack = rememberNavBackStack(HomeRoute)
NavDisplay(
    backStack = backstack,
    onBack = { backstack.removeLastOrNull() },
    entryProvider = { key ->
        when (key) {
            is HomeRoute -> NavEntry(key) { HomeScreen(onGo = { backstack.add(DetailsRoute("1")) }) }
            is DetailsRoute -> NavEntry(key) { DetailsScreen(key.id) }
            else -> NavEntry(key) { NotFoundScreen() }
        }
    }
)
```

## 8. Permisos con Accompanist
```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PantallaConCamara() {
    val permissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    when {
        permissionState.status.isGranted -> { /* usar la cámara */ }
        permissionState.status.shouldShowRationale -> { /* mostrar explicación al usuario */ }
        else -> LaunchedEffect(Unit) { permissionState.launchPermissionRequest() }
    }
}
```
- **Normales** (se otorgan solos): internet, bluetooth, NFC, vibración, alarmas.
- **Runtime / con riesgo** (los pide el usuario): cámara, contactos, ubicación, micrófono, SMS, calendario, almacenamiento.

## 9. DropdownMenu + Abrir URL + Imagen circular (típicos de ejercicios)
```kotlin
// DropdownMenu
var expanded by remember { mutableStateOf(false) }
Box {
    TextField(value = seleccion, onValueChange = {}, readOnly = true,
        modifier = Modifier.clickable { expanded = true })
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        opciones.forEach { op -> DropdownMenuItem(text = { Text(op) }, onClick = { seleccion = op; expanded = false }) }
    }
}

// Abrir una URL
val uriHandler = LocalUriHandler.current
Button(onClick = { uriHandler.openUri("https://www.javeriana.edu.co") }) { Text("Página web") }

// Imagen circular
Image(painter = painterResource(R.drawable.logo), contentDescription = null,
    modifier = Modifier.size(120.dp).clip(CircleShape))
```

## 10. Toast rápido
```kotlin
Toast.makeText(LocalContext.current, "Hola!", Toast.LENGTH_SHORT).show()
```

## 11. Recursos
```kotlin
Text(text = stringResource(R.string.hello))
Text(text = stringResource(R.string.congratulate, "New Year", 2025))
```
