// ============================================================
// SOLUCIÓN — Ejercicio 1 (1.1 a 1.5)
// - Pantalla principal: imagen sobre TextField, DropdownMenu
//   centrado, botones abajo en Row, márgenes en todos los bordes
// - Botón "Página web" abre la Universidad Javeriana
// - Botón "Pantalla 2" navega mostrando nombre + nivel ingresado
// - Botón nuevo navega a una lista de 10 universidades colombianas
// ============================================================

// ---------- Rutas ----------
@Serializable
data object Ej1PrincipalRoute : NavKey

@Serializable
data class Ej1Pantalla2Route(val nombre: String, val nivel: String) : NavKey

@Serializable
data object Ej1UniversidadesRoute : NavKey

// ---------- 1.1: Pantalla principal ----------
@Composable
fun Ejercicio1PantallaPrincipal(
    onAbrirPaginaWeb: () -> Unit,
    onIrAPantalla2: (nombre: String, nivel: String) -> Unit,
    onVerUniversidades: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var nivelExpanded by remember { mutableStateOf(false) }
    var nivel by remember { mutableStateOf("Primaria") }
    val niveles = listOf("Primaria", "Secundaria", "Pregrado", "Posgrado")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // márgenes en todos los bordes
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenido centrado: imagen sobre el textfield, y el dropdown
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.sym_def_app_icon), // reemplaza por tu logo
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // DropdownMenu
            Box(modifier = Modifier.fillMaxWidth(0.8f)) {
                OutlinedTextField(
                    value = nivel,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { nivelExpanded = true }
                )
                DropdownMenu(expanded = nivelExpanded, onDismissRequest = { nivelExpanded = false }) {
                    niveles.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = { nivel = opcion; nivelExpanded = false }
                        )
                    }
                }
            }
        }

        // 1.4: nuevo botón para ver la lista de universidades
        Button(onClick = onVerUniversidades, modifier = Modifier.fillMaxWidth()) {
            Text("Ver universidades")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Botones abajo, en un Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onAbrirPaginaWeb) { Text("Página web") }
            Button(onClick = { onIrAPantalla2(nombre, nivel) }) { Text("Pantalla 2") }
        }
    }
}

// ---------- 1.3: Pantalla 2 (imagen de fondo + texto con los datos) ----------
@Composable
fun Ejercicio1Pantalla2(nombre: String, nivel: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = android.R.drawable.screen_background_light), // reemplaza por tu imagen
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

// ---------- 1.5: Lista de 10 universidades colombianas ----------
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
fun Ejercicio1ListaUniversidades() {
    LazyColumn {
        items(universidadesColombianas) { universidad ->
            Text(universidad, modifier = Modifier.padding(16.dp))
        }
    }
}

// ---------- Navegación completa del ejercicio ----------
@Composable
fun Ejercicio1App() {
    val backstack = rememberNavBackStack(Ej1PrincipalRoute)
    val uriHandler = LocalUriHandler.current

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                Ej1PrincipalRoute -> NavEntry(key) {
                    Ejercicio1PantallaPrincipal(
                        onAbrirPaginaWeb = { uriHandler.openUri("https://www.javeriana.edu.co") },
                        onIrAPantalla2 = { nombre, nivel -> backstack.add(Ej1Pantalla2Route(nombre, nivel)) },
                        onVerUniversidades = { backstack.add(Ej1UniversidadesRoute) }
                    )
                }
                is Ej1Pantalla2Route -> NavEntry(key) { Ejercicio1Pantalla2(key.nombre, key.nivel) }
                Ej1UniversidadesRoute -> NavEntry(key) { Ejercicio1ListaUniversidades() }
                else -> NavEntry(key) { Text("Pantalla no encontrada") }
            }
        }
    )
}
