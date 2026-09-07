// ============================================================
// SOLUCIÓN — Ejercicio 4 (4.1 a 4.4)
// - Margen 35dp, título "Sucesión Triangular", imagen circular,
//   TextField y botón "Calcular" a todo el ancho
// - Si el número NO está entre 0 y 50 -> Toast + Log
// - Si SÍ está -> lista (ListItem + HorizontalDivider) con la
//   sucesión triangular: 0,1,3,6,10,15...
// - Al tocar un ítem -> pantalla con el valor en negrilla, rojo, 30sp
// Dónde pegarlo: crea un archivo Ejercicio4.kt en tu paquete (ver SETUP.md
// sección 3) y pega TODO este contenido tal cual, incluidos los imports.
// Dependencias necesarias: ver SETUP.md sección 4 (Navigation 3)
// ============================================================

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

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

        // Reemplaza este ícono por tu propia imagen si quieres (ver SETUP.md punto 6)
        Image(
            painter = painterResource(id = android.R.drawable.sym_def_app_icon),
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
