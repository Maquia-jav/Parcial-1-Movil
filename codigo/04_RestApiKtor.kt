// ============================================================
// EJEMPLO — Consumir un REST API con Ktor y mostrarlo en pantalla
// No olvides el permiso de INTERNET en el AndroidManifest.xml:
// <uses-permission android:name="android.permission.INTERNET" />
// ============================================================

// 1) Modelo de datos que representa la respuesta del servidor -----
data class User(
    val id: Int,
    val name: String,
    val email: String
)

data class UsersList(
    val results: List<User>
)

// 2) Cliente Ktor ---------------------------------------------------
class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest {
            url("https://mi-api-de-ejemplo.com/api/")
        }
        install(Logging) {
            logger = Logger.SIMPLE
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getUsers(): UsersList {
        return client.get("users").body()
    }

    // Ejemplo de POST
    suspend fun createUser(user: User): User {
        return client.post("users") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()
    }
}

// 3) Consumirlo desde un Composable ----------------------------------
@Composable
fun PantallaDeUsuarios() {
    val apiClient = remember { KtorClient() }
    var users by remember { mutableStateOf(listOf<User>()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        users = apiClient.getUsers().results
        cargando = false
    }

    if (cargando) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(users) { user ->
                ListItem(
                    headlineContent = { Text(user.name) },
                    supportingContent = { Text(user.email) }
                )
            }
        }
    }
}
