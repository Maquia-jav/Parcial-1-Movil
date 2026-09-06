// ============================================================
// EJEMPLO — AlertDialog controlado desde un ViewModel
// ============================================================

@Composable
fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* no hacer nada al tocar fuera, obliga a elegir */ },
        title = { Text("¡Felicitaciones!") },
        text = { Text("Obtuviste: $score puntos") },
        dismissButton = {
            TextButton(onClick = onExit) { Text("Salir") }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) { Text("Jugar otra vez") }
        },
        modifier = modifier
    )
}

// --- Uso típico: se muestra u oculta según una bandera del uiState ---
@Composable
fun PantallaConDialogo(uiState: MiEstado, onPlayAgain: () -> Unit, onExit: () -> Unit) {
    if (uiState.juegoTerminado) {
        FinalScoreDialog(
            score = uiState.puntaje,
            onPlayAgain = onPlayAgain,
            onExit = onExit
        )
    }
    // resto de la pantalla...
}

data class MiEstado(val juegoTerminado: Boolean = false, val puntaje: Int = 0)

// --- Diálogo simple de confirmación (patrón genérico muy común en examen) ---
@Composable
fun DialogoConfirmacion(
    titulo: String,
    mensaje: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(titulo) },
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = onConfirmar) { Text("Aceptar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}
