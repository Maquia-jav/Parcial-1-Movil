// ============================================================
// EJEMPLOS BASE — Composables, Modificadores, Layouts, Toast
// Copia y adapta lo que necesites para el ejercicio del examen
// Dónde pegarlo: ver SETUP.md (raíz del repo) sección 3
// ============================================================

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

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

        // Box: superponer un ícono sobre otro (ej. avatar con check)
        // Reemplaza Icons.Default.Person por tu propia imagen con Image() y
        // painterResource() si quieres usar un recurso de res/drawable (ver SETUP.md punto 6)
        Box {
            Icon(Icons.Filled.Person, contentDescription = "Avatar")
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
