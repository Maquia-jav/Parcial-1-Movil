// ============================================================
// EJEMPLOS — LazyColumn, LazyRow, LazyVerticalGrid, ListItem
// ============================================================

data class Mensaje(val autor: String, val texto: String)

// --- LazyColumn básico con item / items / itemsIndexed ---
@Composable
fun ListaDeMensajes(mensajes: List<Mensaje>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text("Encabezado de la lista", modifier = Modifier.padding(bottom = 8.dp))
        }

        items(mensajes) { mensaje ->
            MensajeRow(mensaje)
        }

        itemsIndexed(mensajes) { index, mensaje ->
            Text("#$index -> ${mensaje.autor}")
        }
    }
}

@Composable
fun MensajeRow(mensaje: Mensaje) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Person, contentDescription = null)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(mensaje.autor, fontWeight = FontWeight.Bold)
            Text(mensaje.texto)
        }
    }
}

// --- ListItem (Material 3) ---
@Composable
fun ItemDeLista(nombre: String, subtitulo: String) {
    ListItem(
        leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
        headlineContent = { Text(nombre) },
        supportingContent = { Text(subtitulo) },
        trailingContent = { Icon(Icons.Default.MoreVert, contentDescription = null) }
    )
}

// --- Grid con fotos ---
data class Foto(val id: Int, val url: String)

@Composable
fun GridDeFotos(fotos: List<Foto>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(fotos) { foto ->
            AsyncImage(
                model = foto.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(4.dp)
                    .aspectRatio(1f)
            )
        }
    }
}

// --- Lista con sticky header ---
@Composable
fun ListaConHeader(items: List<String>) {
    LazyColumn {
        stickyHeader {
            Text(
                "Categoría",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(8.dp)
            )
        }
        items(items) { item -> Text(item, modifier = Modifier.padding(8.dp)) }
    }
}
