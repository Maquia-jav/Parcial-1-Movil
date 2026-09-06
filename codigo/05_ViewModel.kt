// ============================================================
// EJEMPLO — Patrón completo de ViewModel + StateFlow + UDF
// Este es el patrón MÁS IMPORTANTE del curso, apréndetelo de memoria
// ============================================================

// 1) Estado de la UI (data class inmutable) --------------------------
data class ContadorUiState(
    val contador: Int = 0,
    val mensaje: String = ""
)

// 2) ViewModel --------------------------------------------------------
class ContadorViewModel : ViewModel() {

    // Estado privado y mutable (solo el ViewModel lo puede cambiar)
    private val _uiState = MutableStateFlow(ContadorUiState())

    // Estado público e inmutable (la UI solo puede leerlo)
    val uiState: StateFlow<ContadorUiState> = _uiState.asStateFlow()

    // --- Eventos que la UI puede disparar ---
    fun incrementar() {
        _uiState.update { estadoActual ->
            estadoActual.copy(contador = estadoActual.contador + 1)
        }
    }

    fun decrementar() {
        _uiState.update { estadoActual ->
            estadoActual.copy(contador = estadoActual.contador - 1)
        }
    }

    fun reiniciar() {
        _uiState.update { ContadorUiState() }
    }

    fun mostrarMensaje(texto: String) {
        _uiState.update { it.copy(mensaje = texto) }
    }
}

// 3) Conectar el ViewModel con la UI ------------------------------------
@Composable
fun PantallaContador(
    viewModel: ContadorViewModel = viewModel()
) {
    // Se re-dibuja automáticamente cuando uiState cambia
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Contador: ${uiState.contador}", fontSize = 32.sp)

        Row {
            // Evento -> ViewModel actualiza estado -> UI se redibuja sola
            Button(onClick = { viewModel.decrementar() }) { Text("-") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.incrementar() }) { Text("+") }
        }

        Button(onClick = { viewModel.reiniciar() }) {
            Text("Reiniciar")
        }

        if (uiState.mensaje.isNotEmpty()) {
            Text(uiState.mensaje)
        }
    }
}
