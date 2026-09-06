// ============================================================
// EJEMPLO — Navegación completa con Navigation 3
// Copia esta plantilla y agrega tus propias pantallas/rutas
// ============================================================

// 1) Definir las rutas (NavKey) --------------------------------
@Serializable
data object HomeRoute : NavKey

@Serializable
data class DetailsRoute(val userId: String) : NavKey

@Serializable
data object SettingsRoute : NavKey

// 2) Pantallas (composables) ------------------------------------
@Composable
fun HomeScreen(onVerDetalle: (String) -> Unit) {
    Column {
        Text("Pantalla de inicio")
        Button(onClick = { onVerDetalle("123") }) {
            Text("Ver detalle del usuario 123")
        }
    }
}

@Composable
fun DetailsScreen(userId: String) {
    Text("Detalles del usuario: $userId")
}

@Composable
fun SettingsScreen() {
    Text("Configuración")
}

@Composable
fun NotFoundScreen() {
    Text("Pantalla no encontrada")
}

// 3) Armar la navegación (esto va en tu MainActivity / composable raíz) ---
@Composable
fun AppNavigation() {
    val backstack = rememberNavBackStack(HomeRoute)

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                HomeRoute -> NavEntry(key) {
                    HomeScreen(onVerDetalle = { id -> backstack.add(DetailsRoute(id)) })
                }
                is DetailsRoute -> NavEntry(key) {
                    DetailsScreen(userId = key.userId)
                }
                SettingsRoute -> NavEntry(key) {
                    SettingsScreen()
                }
                else -> NavEntry(key) { NotFoundScreen() }
            }
        }
    )
}
