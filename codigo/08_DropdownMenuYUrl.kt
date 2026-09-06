// ============================================================
// DropdownMenu + Abrir una URL con LocalUriHandler
// ============================================================

@Composable
fun SelectorDeNivel(
    nivelSeleccionado: String,
    onNivelCambiado: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Primaria", "Secundaria", "Pregrado", "Posgrado")

    Box(modifier = modifier) {
        OutlinedTextField(
            value = nivelSeleccionado,
            onValueChange = { },
            readOnly = true,
            label = { Text("Nivel educativo") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            },
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
