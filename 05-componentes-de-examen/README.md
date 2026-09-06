# 05 — Componentes que aparecen en los ejercicios tipo examen

Estos son componentes puntuales que **no salían en las diapositivas de clase tal cual**, pero que
aparecen repetidamente en la guía de ejercicios de práctica. Van directo en `codigo/08` a `codigo/10`.

## 1. DropdownMenu (menú desplegable)

Compose no tiene un "Spinner" como el XML tradicional; se arma con un `TextField` de solo lectura +
un `DropdownMenu` que se muestra/oculta con una variable de estado.

```kotlin
var expanded by remember { mutableStateOf(false) }
var opcionSeleccionada by remember { mutableStateOf("Primaria") }
val opciones = listOf("Primaria", "Secundaria", "Pregrado", "Posgrado")

Box {
    TextField(
        value = opcionSeleccionada,
        onValueChange = { },
        readOnly = true,
        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
        modifier = Modifier.clickable { expanded = true }
    )
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        opciones.forEach { opcion ->
            DropdownMenuItem(
                text = { Text(opcion) },
                onClick = {
                    opcionSeleccionada = opcion
                    expanded = false
                }
            )
        }
    }
}
```

## 2. Abrir una página web (LocalUriHandler)

```kotlin
val uriHandler = LocalUriHandler.current

Button(onClick = { uriHandler.openUri("https://www.javeriana.edu.co") }) {
    Text("Página web")
}
```

## 3. Imagen circular

```kotlin
Image(
    painter = painterResource(R.drawable.logo),
    contentDescription = null,
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .size(120.dp)
        .clip(CircleShape)
)
```

## 4. Texto con estilo (negrilla, color, tamaño)

```kotlin
Text(
    text = "120",
    fontWeight = FontWeight.Bold,
    color = Color.Blue,
    fontSize = 25.sp
)
```

## 5. Validar un rango numérico + Toast + Log

Patrón típico de "si el número no está entre X y Y, avisar; si sí, navegar mostrando el resultado".

```kotlin
var texto by remember { mutableStateOf("") }
val context = LocalContext.current

Button(onClick = {
    val numero = texto.toIntOrNull()
    if (numero == null || numero !in 0..20) {
        Toast.makeText(context, "El número está fuera del rango", Toast.LENGTH_SHORT).show()
        Log.w("Validacion", "Número fuera de rango: $texto")
    } else {
        // navegar a la siguiente pantalla con el número válido
    }
})
```

## 6. Campo de texto que solo acepta números (y limita el rango de escritura)

```kotlin
TextField(
    value = texto,
    onValueChange = { nuevo ->
        if (nuevo.all { it.isDigit() }) texto = nuevo // solo dígitos
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
)
```

## 7. ListItem + HorizontalDivider

```kotlin
LazyColumn {
    itemsIndexed(numeros) { index, numero ->
        ListItem(headlineContent = { Text(numero.toString()) })
        if (index < numeros.lastIndex) HorizontalDivider()
    }
}
```

## 8. Pasar datos entre pantallas al navegar

La forma más simple es meter los datos directo en el `NavKey` (ver `codigo/03_Navegacion.kt`):

```kotlin
@Serializable
data class ResultadoRoute(val nombre: String, val nivel: String) : NavKey

// Al navegar:
backstack.add(ResultadoRoute(nombre = "Ana", nivel = "Pregrado"))

// En la pantalla destino, los datos ya vienen listos:
@Composable
fun ResultadoScreen(nombre: String, nivel: String) {
    Text("$nombre - $nivel")
}
```

## 9. Portrait vs Landscape

Para que la UI se vea bien en ambas orientaciones **sin usar ViewModel**, evita valores fijos de
ancho/alto (usa `fillMaxWidth()`, `weight()`, `Arrangement`) y deja que `Column`/`Row` se adapten solos.
Si quieres detectar la orientación explícitamente:

```kotlin
val configuration = LocalConfiguration.current
val esVertical = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
```
