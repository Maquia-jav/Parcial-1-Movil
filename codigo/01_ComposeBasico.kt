// ============================================================
// EJEMPLOS BASE — Composables, Modificadores, Layouts, Toast
// Copia y adapta lo que necesites para el ejercicio del examen
// ============================================================

// --- Composable simple con modificadores ---
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hola $name!",
        modifier = modifier
            .background(Color.Blue)
            .padding(16.dp)
            .clickable { /* acción al hacer click */ }
    )
}

// --- Pantalla completa combinando Column, Row, Box ---
@Composable
fun PantallaEjemplo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Elemento 1")
        Text("Elemento 2")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text("Item A")
            Text("Item B")
            Text("Item C")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Box: superponer un ícono sobre una imagen (ej. avatar con check)
        Box {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "Avatar"
            )
            Icon(Icons.Filled.Check, contentDescription = "Verificado")
        }
    }
}

// --- Scaffold: esqueleto típico de pantalla ---
@Composable
fun ScaffoldEjemplo() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi App") })
        },
        bottomBar = {
            BottomAppBar { Text("Pie de página", modifier = Modifier.fillMaxWidth()) }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Contenido de la pantalla")
        }
    }
}

// --- Data class típica ---
data class Usuario(
    val nombre: String,
    val correo: String,
    val edad: Int
)

// --- Toast ---
@Composable
fun BotonConToast() {
    val context = LocalContext.current
    Button(onClick = {
        Toast.makeText(context, "¡Acción realizada!", Toast.LENGTH_SHORT).show()
    }) {
        Text("Presionar")
    }
}

// --- Animación de aparición/desaparición ---
@Composable
fun CajaAnimada() {
    var visible by remember { mutableStateOf(true) }

    Column {
        Button(onClick = { visible = !visible }) {
            Text("Mostrar/Ocultar")
        }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.Green)
            )
        }
    }
}
