// ============================================================
// DropdownMenu + Abrir una URL con LocalUriHandler
// Dónde pegarlo: ver SETUP.md (raíz del repo) sección 3
// Requiere material-icons-extended para el ícono ArrowDropDown: ver SETUP.md sección 4
// ============================================================

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun SelectorDeNivel(
    nivelSeleccionado: String,
    onNivelCambiado: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Primaria", "Secundaria", "Pregrado", "Posgrado")

    Box(modifier = modifier) {
        // ⚠️ enabled = false es NECESARIO: sin esto, el propio OutlinedTextField
        // "se queda" con el toque y el menú nunca se abre (bug conocido de Compose
        // al combinar readOnly + clickable). Los "colors" son para que NO se vea
        // apagado/gris como un campo deshabilitado normal.
        OutlinedTextField(
            value = nivelSeleccionado,
            onValueChange = { },
            readOnly = true,
            enabled = false,
            label = { Text("Nivel educativo") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onNivelCambiado(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun BotonAbrirPaginaWeb() {
    val uriHandler = LocalUriHandler.current
    Button(onClick = { uriHandler.openUri("https://www.javeriana.edu.co") }) {
        Text("Página web")
    }
}
