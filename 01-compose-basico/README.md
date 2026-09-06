# Sesión 3 — Jetpack Compose UI y Layouts básicos

## 1. ¿Qué es un Composable?

Cada función marcada con `@Composable` es un **bloque de código** que define un elemento de la interfaz
(un texto, un botón, una pantalla completa, etc.). Compose es literalmente **un sistema de bloques**:
cada bloque puede contener otros bloques adentro, y así se arman interfaces complejas a partir de piezas simples y reutilizables.

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
```

- Puede recibir **parámetros** (`name`) para personalizarse.
- Puede tener **valores por defecto** (`name: String = "John Doe"`).
- Se puede previsualizar en Android Studio con `@Preview`.

```kotlin
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Text(text = "Hello World!")
    }
}
```

## 2. Modificadores (`Modifier`)

Los modificadores son objetos que cambian la **apariencia y el comportamiento** de un composable:
tamaño, color de fondo, si es clickeable, márgenes, alineación, etc. Se **encadenan** con `.` (el orden importa).

| Modificador | Qué hace |
|---|---|
| `padding(16.dp)` | espacio alrededor del composable |
| `background(Color.Blue)` | color de fondo |
| `clickable { }` | hace que el elemento responda a toques |
| `fillMaxWidth()` / `fillMaxHeight()` | ocupa todo el ancho/alto disponible |
| `align(...)` | alinea el composable respecto a su padre |
| `border(...)` | agrega un borde |

```kotlin
Text(
    text = "Hello $name!",
    modifier = modifier
        .background(Color.Blue)
        .padding(16.dp)
        .padding(8.dp) // se pueden repetir/encadenar
)
```

## 3. Layouts básicos

En Compose anidar composables (Column/Row/Box, LazyColumn, etc.) es una práctica normal y de buen
rendimiento (al contrario del XML tradicional donde se evita anidar layouts).

- **Column** → organiza elementos verticalmente.
- **Row** → organiza elementos horizontalmente.
- **Box** → organiza elementos de forma absoluta (superpuestos, útil para overlays/badges).
- **Spacer** → agrega espacio vacío entre elementos.

```kotlin
@Composable
fun ArtistCardRow(artist: Artist) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(bitmap = artist.image, contentDescription = "Artist")
        Column {
            Text(artist.name)
            Text(artist.lastSeenOnline)
        }
    }
}
```

```kotlin
@Composable
fun ArtistAvatar(artist: Artist) {
    Box {
        Image(bitmap = artist.image, contentDescription = "Artist")
        Icon(Icons.Filled.Check, contentDescription = "Check mark")
    }
}
```

`Column` y `Row` aceptan `verticalArrangement` / `horizontalArrangement` para distribuir el espacio
(ej. `Arrangement.SpaceEvenly`, `Arrangement.SpaceAround`).

## 4. Scaffold

`Scaffold` da la estructura típica de una pantalla: barra superior, barra inferior y contenido central.

```kotlin
@Composable
fun ScaffoldExample() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Top app bar") }) },
        bottomBar = { BottomAppBar { Text("Bottom app bar") } }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Contenido de la pantalla")
        }
    }
}
```

## 5. Data class

Sirve para representar un **modelo de datos**. Kotlin genera automáticamente `toString()`, `equals()`,
`hashCode()` y `copy()` — esto último es clave para actualizar estados inmutables (ver carpeta 03).

```kotlin
data class Character(
    val name: String,
    val alias: String,
    val image: Bitmap
)
```

## 6. Toast

Mensaje corto en la parte inferior de la pantalla. Necesita un `context`.

```kotlin
val context = LocalContext.current
Toast.makeText(context, "Hello World!", Toast.LENGTH_SHORT).show()
```

## 7. Recursos (strings.xml)

Las cadenas de texto se definen aparte (facilita traducir la app) en `app/res/values/strings.xml`:

```xml
<resources>
    <string name="hello">Hello!</string>
    <string name="congratulate">Happy %1$s %2$d</string>
</resources>
```

Y se usan con `stringResource`:

```kotlin
Text(text = stringResource(R.string.hello))
Text(text = stringResource(R.string.congratulate, "New Year", 2025))
```

## 8. Colores y Material Theme

Los colores también se definen como recursos (`res/values/colors.xml`) o dentro del **tema** de la app
(`ui/theme/Theme.kt`), que es un Composable raíz que envuelve toda la app y define el look claro/oscuro.

```kotlin
setContent {
    AppTheme {
        Scaffold { innerPadding -> /* nuestros composables */ }
    }
}
```

Dentro de los composables se referencia así: `MaterialTheme.colorScheme...` / `MaterialTheme.typography...`.

## 9. Animaciones

`AnimatedVisibility` anima la entrada/salida de un composable según una variable de estado booleana.

```kotlin
var visible by remember { mutableStateOf(true) }

AnimatedVisibility(
    visible = visible,
    enter = fadeIn() + slideInVertically(),
    exit = fadeOut() + slideOutVertically()
) {
    // composable a animar
}
```

Efectos comunes: `fadeIn()/fadeOut()`, `slideInVertically()/slideOutVertically()`, `scaleIn()/scaleOut()`,
`expandIn()/shrinkOut()`, `expandHorizontally()/shrinkHorizontally()`, `expandVertically()/shrinkVertically()`.

## 10. Log

Para depurar / imprimir mensajes en Logcat:

```kotlin
Log.i("MyAPP", "Mensaje")
```
