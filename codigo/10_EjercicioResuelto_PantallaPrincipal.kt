// ============================================================
// EJEMPLO RESUELTO — Recrea la estructura del Ejercicio 1.1-1.5
// Imagen arriba, TextField + DropdownMenu al centro,
// botones abajo en una Row, márgenes en todos los bordes,
// navegación a "Pantalla 2" pasando los datos ingresados.
// Adapta nombres y vistas a lo que pida tu enunciado real.
// ============================================================

@Serializable
data object PrincipalRoute : NavKey

@Serializable
data class Pantalla2Route(val nombre: String, val nivel: String) : NavKey

@Serializable
data object ListaUniversidadesRoute : NavKey

@Composable
fun PantallaPrincipal(
    onIrAPaginaWeb: () -> Unit,
    onIrAPantalla2: (nombre: String, nivel: String) -> Unit,
    onIrALista: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var nivel by remember { mutableStateOf("Primaria") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // márgenes en todos los bordes
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // La imagen y los campos van en el centro -> los metemos en
        // una Column con weight(1f) para que empuje los botones al fondo
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.android_logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SelectorDeNivel(
                nivelSeleccionado = nivel,
                onNivelCambiado = { nivel = it }
            )
        }

        // Botones abajo, en una Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onIrAPaginaWeb) { Text("Página web") }
            Button(onClick = { onIrAPantalla2(nombre, nivel) }) { Text("Pantalla 2") }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onIrALista, modifier = Modifier.fillMaxWidth()) {
            Text("Ver universidades")
        }
    }
}

// --- Pantalla 2: imagen de fondo + texto encima con los datos -----------
@Composable
fun Pantalla2(nombre: String, nivel: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.fondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "$nombre - $nivel",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// --- Lista de universidades colombianas (arreglo fijo de Strings) -------
val universidadesColombianas = listOf(
    "Universidad Nacional de Colombia",
    "Pontificia Universidad Javeriana",
    "Universidad de los Andes",
    "Universidad de Antioquia",
    "Universidad del Valle",
    "Universidad Industrial de Santander",
    "Universidad EAFIT",
    "Universidad del Norte",
    "Universidad del Rosario",
    "Universidad Externado de Colombia"
)

@Composable
fun ListaUniversidadesScreen() {
    LazyColumn {
        items(universidadesColombianas) { universidad ->
            Text(universidad, modifier = Modifier.padding(16.dp))
        }
    }
}

// --- Armando toda la navegación junta ------------------------------------
@Composable
fun AppNavigationEjercicio1() {
    val backstack = rememberNavBackStack(PrincipalRoute)

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                PrincipalRoute -> NavEntry(key) {
                    PantallaPrincipal(
                        onIrAPaginaWeb = { /* usar LocalUriHandler aquí, ver 08_DropdownMenuYUrl.kt */ },
                        onIrAPantalla2 = { nombre, nivel -> backstack.add(Pantalla2Route(nombre, nivel)) },
                        onIrALista = { backstack.add(ListaUniversidadesRoute) }
                    )
                }
                is Pantalla2Route -> NavEntry(key) { Pantalla2(key.nombre, key.nivel) }
                ListaUniversidadesRoute -> NavEntry(key) { ListaUniversidadesScreen() }
                else -> NavEntry(key) { Text("Pantalla no encontrada") }
            }
        }
    )
}
