// ============================================================
// SOLUCIÓN — Ejercicio 2 (2.1 a 2.3)
// - Botón "Factorial" + TextField que solo acepta enteros de 1 a 15
// - La UI se ve bien en Portrait y Landscape (usa fillMaxWidth,
//   NO usa tamaños fijos en dp) -> no necesita ViewModel
// - Al pulsar "Factorial", navega mostrando la operación y el resultado
// Dónde pegarlo: crea un archivo Ejercicio2.kt en tu paquete (ver SETUP.md
// sección 3) y pega TODO este contenido tal cual, incluidos los imports.
// Dependencias necesarias: ver SETUP.md sección 4 (Navigation 3)
// ============================================================

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
data object Ej2PrincipalRoute : NavKey

@Serializable
data class Ej2ResultadoRoute(val numero: Int) : NavKey

// ---------- 2.2: Pantalla principal (responsive) ----------
@Composable
fun Ejercicio2PantallaPrincipal(onCalcularFactorial: (Int) -> Unit) {
    var texto by remember { mutableStateOf("") }

    // Usar fillMaxWidth() (nunca .width(300.dp) fijo) es lo que hace
    // que se vea bien tanto en Portrait como en Landscape.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val numero = texto.toIntOrNull()
                if (numero != null && numero in 1..15) {
                    onCalcularFactorial(numero)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Factorial")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = texto,
            onValueChange = { nuevo ->
                // Solo dígitos, y solo hasta 2 caracteres (máximo 15)
                if (nuevo.all { it.isDigit() } && nuevo.length <= 2) {
                    texto = nuevo
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ---------- Cálculo del factorial ----------
fun calcularFactorial(n: Int): Long {
    var resultado = 1L
    for (i in 1..n) resultado *= i
    return resultado
}

// ---------- 2.3: Pantalla de resultado ----------
@Composable
fun Ejercicio2PantallaResultado(numero: Int) {
    val resultado = calcularFactorial(numero)
    val operacion = (1..numero).joinToString("*")

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Reemplaza este ícono por tu propia imagen si quieres (ver SETUP.md punto 6)
        Image(
            painter = painterResource(id = android.R.drawable.sym_def_app_icon),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Operación: $operacion")
        Text("Resultado: $resultado")
    }
}

// ---------- Navegación completa del ejercicio ----------
@Composable
fun Ejercicio2App() {
    val backstack = rememberNavBackStack(Ej2PrincipalRoute)

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                Ej2PrincipalRoute -> NavEntry(key) {
                    Ejercicio2PantallaPrincipal(
                        onCalcularFactorial = { numero -> backstack.add(Ej2ResultadoRoute(numero)) }
                    )
                }
                is Ej2ResultadoRoute -> NavEntry(key) { Ejercicio2PantallaResultado(key.numero) }
                else -> NavEntry(key) { Text("Pantalla no encontrada") }
            }
        }
    )
}
