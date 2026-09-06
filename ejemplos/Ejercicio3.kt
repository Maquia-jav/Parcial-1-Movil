// ============================================================
// SOLUCIÓN — Ejercicio 3 (3.1 a 3.4)
// - TextField (70%) + botón (30%) centrados, margen 10dp
// - Si el número NO está entre 0 y 20 -> Toast + Log
// - Si SÍ está -> lista (LazyColumn) con la sucesión cúbica: 0,1,8,27,64...
// - Al tocar un ítem -> pantalla con el valor en negrilla, azul, 25sp
// ============================================================

@Serializable
data object Ej3PrincipalRoute : NavKey

@Serializable
data class Ej3ListaRoute(val hasta: Int) : NavKey

@Serializable
data class Ej3ValorRoute(val valor: Double) : NavKey

// ---------- 3.1 y 3.2: Pantalla principal con validación ----------
@Composable
fun Ejercicio3PantallaPrincipal(onNumeroValido: (Int) -> Unit) {
    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), // margen de 10dp por todos los bordes
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = texto,
                onValueChange = { nuevo -> if (nuevo.all { it.isDigit() }) texto = nuevo },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(0.7f) // 70% del ancho
                    .padding(end = 8.dp)
            )
            Button(
                onClick = {
                    val numero = texto.toIntOrNull()
                    if (numero == null || numero !in 0..20) {
                        Toast.makeText(context, "El número está fuera del rango", Toast.LENGTH_SHORT).show()
                        Log.w("Ejercicio3", "Número fuera de rango: $texto")
                    } else {
                        onNumeroValido(numero)
                    }
                },
                modifier = Modifier.weight(0.3f) // 30% del ancho
            ) {
                Text("Lista")
            }
        }
    }
}

// ---------- 3.3: sucesión cúbica y lista ----------
fun sucesionCubica(hasta: Int): List<Double> =
    (0..hasta).map { i -> Math.pow(i.toDouble(), 3.0) }

@Composable
fun Ejercicio3Lista(hasta: Int, onValorSeleccionado: (Double) -> Unit) {
    val valores = remember(hasta) { sucesionCubica(hasta) }
    LazyColumn {
        items(valores) { valor ->
            Text(
                text = valor.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onValorSeleccionado(valor) }
                    .padding(16.dp)
            )
        }
    }
}

// ---------- 3.4: pantalla del valor seleccionado (negrilla, azul, 25sp) ----------
@Composable
fun Ejercicio3ValorSeleccionado(valor: Double) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = valor.toString(),
            fontWeight = FontWeight.Bold,
            color = Color.Blue,
            fontSize = 25.sp
        )
    }
}

// ---------- Navegación completa del ejercicio ----------
@Composable
fun Ejercicio3App() {
    val backstack = rememberNavBackStack(Ej3PrincipalRoute)

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                Ej3PrincipalRoute -> NavEntry(key) {
                    Ejercicio3PantallaPrincipal(
                        onNumeroValido = { numero -> backstack.add(Ej3ListaRoute(numero)) }
                    )
                }
                is Ej3ListaRoute -> NavEntry(key) {
                    Ejercicio3Lista(
                        hasta = key.hasta,
                        onValorSeleccionado = { valor -> backstack.add(Ej3ValorRoute(valor)) }
                    )
                }
                is Ej3ValorRoute -> NavEntry(key) { Ejercicio3ValorSeleccionado(key.valor) }
                else -> NavEntry(key) { Text("Pantalla no encontrada") }
            }
        }
    )
}
