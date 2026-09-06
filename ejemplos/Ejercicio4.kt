// ============================================================
// SOLUCIÓN — Ejercicio 4 (4.1 a 4.4)
// - Margen 35dp, título "Sucesión Triangular", imagen circular,
//   TextField y botón "Calcular" a todo el ancho
// - Si el número NO está entre 0 y 50 -> Toast + Log
// - Si SÍ está -> lista (ListItem + HorizontalDivider) con la
//   sucesión triangular: 0,1,3,6,10,15...
// - Al tocar un ítem -> pantalla con el valor en negrilla, rojo, 30sp
// ============================================================

@Serializable
data object Ej4PrincipalRoute : NavKey

@Serializable
data class Ej4ListaRoute(val hasta: Int) : NavKey

@Serializable
data class Ej4ValorRoute(val valor: Int) : NavKey

// ---------- 4.1 y 4.2: Pantalla principal con validación ----------
@Composable
fun Ejercicio4PantallaPrincipal(onNumeroValido: (Int) -> Unit) {
    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(35.dp), // margen de 35dp
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sucesión Triangular", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = android.R.drawable.sym_def_app_icon), // reemplaza por tu imagen
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape) // imagen con forma de círculo
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = texto,
            onValueChange = { nuevo -> if (nuevo.all { it.isDigit() }) texto = nuevo },
            label = { Text("Ingrese un número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth() // todo el ancho disponible
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val numero = texto.toIntOrNull()
                if (numero == null || numero !in 0..50) {
                    Toast.makeText(context, "El número está fuera del rango", Toast.LENGTH_SHORT).show()
                    Log.w("Ejercicio4", "Número fuera de rango: $texto")
                } else {
                    onNumeroValido(numero)
                }
            },
            modifier = Modifier.fillMaxWidth() // todo el ancho disponible
        ) {
            Text("Calcular")
        }
    }
}

// ---------- 4.3: sucesión triangular y lista ----------
fun sucesionTriangular(hasta: Int): List<Int> {
    val lista = mutableListOf<Int>()
    var suma = 0
    for (i in 0..hasta) {
        suma += i
        lista.add(suma)
    }
    return lista
}

@Composable
fun Ejercicio4Lista(hasta: Int, onValorSeleccionado: (Int) -> Unit) {
    val valores = remember(hasta) { sucesionTriangular(hasta) }
    LazyColumn {
        itemsIndexed(valores) { index, valor ->
            ListItem(
                headlineContent = { Text(valor.toString()) },
                modifier = Modifier.clickable { onValorSeleccionado(valor) }
            )
            if (index < valores.lastIndex) HorizontalDivider()
        }
    }
}

// ---------- 4.4: pantalla del valor seleccionado (negrilla, rojo, 30sp) ----------
@Composable
fun Ejercicio4ValorSeleccionado(valor: Int) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = valor.toString(),
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            fontSize = 30.sp
        )
    }
}

// ---------- Navegación completa del ejercicio ----------
@Composable
fun Ejercicio4App() {
    val backstack = rememberNavBackStack(Ej4PrincipalRoute)

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                Ej4PrincipalRoute -> NavEntry(key) {
                    Ejercicio4PantallaPrincipal(
                        onNumeroValido = { numero -> backstack.add(Ej4ListaRoute(numero)) }
                    )
                }
                is Ej4ListaRoute -> NavEntry(key) {
                    Ejercicio4Lista(
                        hasta = key.hasta,
                        onValorSeleccionado = { valor -> backstack.add(Ej4ValorRoute(valor)) }
                    )
                }
                is Ej4ValorRoute -> NavEntry(key) { Ejercicio4ValorSeleccionado(key.valor) }
                else -> NavEntry(key) { Text("Pantalla no encontrada") }
            }
        }
    )
}
