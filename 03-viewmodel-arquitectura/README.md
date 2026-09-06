# Sesión 5 — Arquitectura de la app y ViewModel

## 1. Definición de una arquitectura (organización de carpetas)

```
/ui
  /theme       -> tema material de la app
  /components  -> componentes reutilizables
  /screens     -> pantallas
/data          -> entidades de datos
/utils         -> utilidades generales
/navigation    -> configuración de navegación
```

## 2. Arquitectura recomendada por Google

Toda app se divide en capas:

- **UI Layer** — muestra datos y reacciona a acciones del usuario.
- **Data Layer** — contiene la lógica de negocio y el acceso a datos.
- *(Opcional)* **Domain Layer** — simplifica y reutiliza las interacciones entre datos y UI.

```
UI Layer  →  Domain Layer (opcional)  →  Data Layer
```

## 3. Arquitectura de la capa UI

La capa de UI se compone de **2 partes**:

1. **UI elements**: los `@Composables` (reutilizables).
2. **State holders**: los **ViewModels**, que contienen la lógica de la UI y los datos que se muestran.

```
Data Layer → [ UI Layer: ViewModel (UI state) → UI elements ] → eventos vuelven al ViewModel
```

## 4. ViewModel — ¿qué es y por qué se usa?

Es una clase que **almacena y maneja los datos de la UI de forma que sobrevive a los cambios de
configuración** (ej. rotar la pantalla). Ventajas:

- Persiste datos de la UI aunque la Activity se recree.
- Da acceso a la lógica de negocio de la app.
- Se integra directamente con Compose y con Navigation.

Para crear uno, se extiende de la clase `ViewModel`:

```kotlin
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
}
```

### Estado de la UI con StateFlow

El ViewModel guarda una referencia a una clase de estado (normalmente un `data class`):

```kotlin
data class GameUiState(
    val currentScrambledWord: String = ""
)
```

`StateFlow` permite **emitir y recibir** valores de estado, conectando el `GameUiState` con el `GameViewModel`:

```kotlin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private val _uiState = MutableStateFlow(GameUiState())
val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
```

**Muy importante**: el estado (`_uiState`) es **privado**, solo el ViewModel lo puede modificar.
Afuera solo se expone la versión inmutable (`uiState`, gracias a `asStateFlow()`).

### Flujo de datos unidireccional (UDF)

```
1. Eventos     -> la UI genera un evento y lo envía al ViewModel
2. Actualizar  -> el ViewModel recibe el evento y cambia el estado
3. Observar    -> la UI observa el estado y se redibuja sola
```

### Conectar el ViewModel a un Composable

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    // ...
}
```

`collectAsState()` asegura que cuando `uiState` cambia, la UI se actualiza automáticamente.

### Modificar el estado desde el ViewModel

Se usa `_uiState.update { }` junto con `copy()` (el método que generan los `data class` automáticamente)
para cambiar solo lo necesario sin tocar el resto del estado:

```kotlin
import kotlinx.coroutines.flow.update

if (userGuess.equals(currentWord, ignoreCase = true)) {
    // acierto
} else {
    _uiState.update { currentState ->
        currentState.copy(isGuessedWordWrong = true)
    }
}
```

## 5. Dialogs

Un `Dialog` es una ventana pequeña que **no cubre toda la pantalla**. Partes que puede tener:

1. Contenedor
2. Ícono (opcional)
3. Título
4. Texto
5. Divider
6. Acciones (botones)

Compose ya trae `AlertDialog` para crearlo:

```kotlin
AlertDialog(
    onDismissRequest = { },
    title = { },
    text = { },
    modifier = modifier,
    dismissButton = { },
    confirmButton = { }
)
```

Ejemplo real (diálogo de puntaje final de un juego):

```kotlin
@Composable
fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("¡Felicitaciones!") },
        text = { Text("Obtuviste: $score puntos") },
        dismissButton = {
            TextButton(onClick = { /* salir */ }) { Text("Salir") }
        },
        confirmButton = {
            TextButton(onClick = { onPlayAgain() }) { Text("Jugar otra vez") }
        },
        modifier = modifier
    )
}
```

Nota sobre Kotlin: `onPlayAgain: () -> Unit` es un **parámetro de tipo función** — no recibe nada y no
retorna nada. Es muy común pasar funciones como parámetros para que el componente hijo (el diálogo)
avise al padre cuándo se presionó un botón, sin que el diálogo necesite saber qué hacer con esa acción.
