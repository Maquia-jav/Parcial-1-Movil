// ============================================================
// Patrón: TextField numérico + validar rango + Toast/Log
// + navegar mostrando el resultado de un cálculo (factorial,
// sucesión triangular, cuadrática, etc.)
// Aplica directo a los Ejercicios 2, 3 y 4 de la guía
// Dónde pegarlo: ver SETUP.md (raíz del repo) sección 3
// ============================================================

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaConValidacion(
    onNumeroValido: (Int) -> Unit // callback para navegar con el número ya validado
) {
    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = texto,
            onValueChange = { nuevo -> if (nuevo.all { it.isDigit() }) texto = nuevo },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("0 - 20") },
            modifier = Modifier
                .weight(0.7f) // 70% del ancho
                .padding(end = 8.dp)
        )
        Button(
            onClick = {
                val numero = texto.toIntOrNull()
                if (numero == null || numero !in 0..20) {
                    Toast.makeText(context, "El número está fuera del rango", Toast.LENGTH_SHORT).show()
                    Log.w("Validacion", "Número fuera de rango: $texto")
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

// --- Cálculos típicos que piden en la guía --------------------------

// Factorial: 1*2*3*...*n
fun calcularFactorial(n: Int): Long {
    var resultado = 1L
    for (i in 1..n) resultado *= i
    return resultado
}

// Sucesión "cuadrática" (cubo de la posición): 0, 1, 8, 27, 64...
fun sucesionCubica(hasta: Int): List<Double> =
    (0..hasta).map { i -> Math.pow(i.toDouble(), 3.0) }

// Sucesión triangular: 0, 1, 3, 6, 10, 15...
fun sucesionTriangular(hasta: Int): List<Int> {
    val lista = mutableListOf<Int>()
    var suma = 0
    for (i in 0..hasta) {
        suma += i
        lista.add(suma)
    }
    return lista
}

// --- Pantalla que muestra el resultado de un cálculo -------------------
// Usa un Icon() como placeholder para no depender de recursos externos (ver SETUP.md punto 6)
@Composable
fun PantallaResultadoFactorial(numero: Int) {
    val resultado = calcularFactorial(numero)
    val operacion = (1..numero).joinToString("*")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = Icons.Default.Star, contentDescription = null)
        Text("Operación: $operacion")
        Text("Resultado: $resultado")
    }
}

// --- Pantalla que muestra un valor seleccionado con estilo (Ej. 3.4 / 4.4) ---
@Composable
fun PantallaValorSeleccionado(valor: Number, color: Color, tamanoSp: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = valor.toString(),
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = tamanoSp.sp
        )
    }
}
