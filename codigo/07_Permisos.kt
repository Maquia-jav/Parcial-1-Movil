// ============================================================
// EJEMPLO — Solicitar permisos con Google Accompanist
// build.gradle: implementation("com.google.accompanist:accompanist-permissions:0.37.3")
// ============================================================

// --- Permiso único (ej. cámara) ---------------------------------------
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PantallaConCamara() {
    val permissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    when {
        // Paso 2: ¿ya está concedido?
        permissionState.status.isGranted -> {
            Text("Permiso concedido, aquí se abriría la cámara")
        }
        // Paso 3: ¿hay que explicarle al usuario por qué se necesita?
        permissionState.status.shouldShowRationale -> {
            Column {
                Text("Necesitamos acceso a la cámara para tomar tu foto de perfil.")
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("Dar permiso")
                }
            }
        }
        // Paso 4: aún no se ha pedido -> pedirlo
        else -> {
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }
            Text("Solicitando permiso...")
        }
    }
}

// --- Múltiples permisos a la vez (ej. ubicación fina + gruesa) ----------
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PantallaConUbicacion() {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    when {
        permissionsState.allPermissionsGranted -> {
            Text("Permisos de ubicación concedidos")
        }
        permissionsState.shouldShowRationale -> {
            Column {
                Text("Necesitamos tu ubicación para mostrarte lugares cercanos.")
                Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                    Text("Dar permisos")
                }
            }
        }
        else -> {
            LaunchedEffect(Unit) {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
    }
}

// ------------------------------------------------------------------------
// Recordatorio de permisos comunes que se declaran en el Manifest:
//
// <uses-permission android:name="android.permission.CAMERA"/>
// <uses-permission android:name="android.permission.READ_CONTACTS"/>
// <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
// <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
// <uses-permission android:name="android.permission.RECORD_AUDIO"/>
// <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
// <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
// <uses-permission android:name="android.permission.INTERNET"/>  (normal, no pide diálogo)
// ------------------------------------------------------------------------
