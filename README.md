# 📱 Computación Móvil — Repo de Apoyo para el Parcial 1

Repositorio de estudio con **toda la teoría y el código** visto hasta la Sesión 6 (Permisos),
organizado para tenerlo abierto durante el parcial teórico-práctico.

## 📚 Contenido

| Carpeta | Tema | Sesión |
|---|---|---|
| [`01-compose-basico`](./01-compose-basico) | Jetpack Compose UI, modificadores, layouts básicos (Column/Row/Box), recursos, colores, animaciones | 3 |
| [`02-layouts-listas-navegacion`](./02-layouts-listas-navegacion) | Listas (LazyColumn/Grid), ListItem, REST API con Ktor, imágenes, Navegación (Navigation 3) | 4 |
| [`03-viewmodel-arquitectura`](./03-viewmodel-arquitectura) | Arquitectura de apps, ViewModel, StateFlow, UDF, Dialogs | 5 |
| [`04-permisos`](./04-permisos) | Permisos normales/runtime, Manifest, Google Accompanist | 6 |
| [`05-componentes-de-examen`](./05-componentes-de-examen) | DropdownMenu, LocalUriHandler, imagen circular, texto con estilo, validación de rango, pasar datos entre pantallas | Guía de ejercicios |
| [`codigo`](./codigo) | Archivos `.kt` completos y funcionales, listos para copiar/adaptar en el examen (incluye un ejercicio tipo examen resuelto completo: `10_EjercicioResuelto_PantallaPrincipal.kt`) | — |
| [`CHEATSHEET.md`](./CHEATSHEET.md) | Todo lo esencial en una sola página, para repasar en 5 minutos | — |

## 🧭 Cómo usar este repo en el examen

1. ¿El ejercicio pide **mostrar algo en pantalla** (listas, texto, imágenes)? → `01-compose-basico` o `02-layouts-listas-navegacion`.
2. ¿Pide **manejar el estado/lógica de una pantalla** (guardar datos, reaccionar a clicks)? → `03-viewmodel-arquitectura`.
3. ¿Pide **acceder a cámara, contactos, ubicación, micrófono, etc.**? → `04-permisos`.
4. ¿Pide DropdownMenu, abrir una URL, validar un número en un rango, pasar datos entre pantallas, texto con estilo o imagen circular? → `05-componentes-de-examen`.
5. ¿Necesitas código ya armado para copiar y adaptar rápido? → `codigo/` (revisa `10_EjercicioResuelto_PantallaPrincipal.kt`, es un ejercicio completo tipo examen ya resuelto).
6. ¿Poco tiempo? → `CHEATSHEET.md` tiene todo resumido.

## 🗓️ Contexto del curso

Clase 1131 — Martes 9AM-12M.
Plan: Sesión 3 (Compose/Layouts) → Sesión 4 (Dynamic Layouts/Navigation) → Sesión 5 (ViewModel) → Sesión 6 (Permisos) → **Parcial 1 Teórico-Práctico (Semana 7)**.
