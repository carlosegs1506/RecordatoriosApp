
package com.example.recordatoriosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ======================================
// 1. CATEGORIAS
// ======================================

sealed class Categoria(val nombre: String) {

    object Trabajo : Categoria("Trabajo")
    object Estudio : Categoria("Estudio")
    object Personal : Categoria("Personal")
    object Salud : Categoria("Salud")
}

// ======================================
// 2. PRIORIDADES
// ======================================

enum class Prioridad {
    BAJA,
    MEDIA,
    ALTA
}

// ======================================
// 3. ESTADOS
// ======================================

sealed class Estado {

    object Pendiente : Estado()
    object EnProgreso : Estado()
    object Completado : Estado()
}

// ======================================
// 4. MODELO DE RECORDATORIO
// ======================================

data class Recordatorio(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: Estado,
    val fecha: String? = null,
    val ubicacion: String? = null
)

// ======================================
// 5. LISTA DE DATOS SIMULADOS
// ======================================

val recordatorios = listOf(

    Recordatorio(
        id = 1,
        titulo = "Entregar actividad",
        descripcion = "Finalizar actividad de programación móvil",
        categoria = Categoria.Estudio,
        prioridad = Prioridad.ALTA,
        estado = Estado.Pendiente,
        fecha = "10/11/2026"
    ),

    Recordatorio(
        id = 2,
        titulo = "Revisar redes",
        descripcion = "Estudiar configuración de routers",
        categoria = Categoria.Trabajo,
        prioridad = Prioridad.MEDIA,
        estado = Estado.EnProgreso
    ),

    Recordatorio(
        id = 3,
        titulo = "Realizar ejercicio",
        descripcion = "Completar actividad física diaria",
        categoria = Categoria.Salud,
        prioridad = Prioridad.BAJA,
        estado = Estado.Completado
    ),

    Recordatorio(
        id = 4,
        titulo = "Revisar proyecto",
        descripcion = "Verificar avances del proyecto personal",
        categoria = Categoria.Personal,
        prioridad = Prioridad.ALTA,
        estado = Estado.Pendiente
    )
)

// ======================================
// 6. SET: CATEGORIAS SIN REPETIR
// ======================================

val categoriasUnicas = recordatorios
    .map { it.categoria }
    .toSet()

// ======================================
// 7. MAP: COLORES POR CATEGORIA
// ======================================

val coloresCategorias = mapOf(

    Categoria.Trabajo to Color(0xFF1976D2),

    Categoria.Estudio to Color(0xFF388E3C),

    Categoria.Personal to Color(0xFF7B1FA2),

    Categoria.Salud to Color(0xFFEF6C00)
)

// ======================================
// 8. FILTROS
// ======================================

fun obtenerPendientes(): List<Recordatorio> {

    return recordatorios.filter {
        it.estado is Estado.Pendiente
    }
}

fun obtenerAltaPrioridad(): List<Recordatorio> {

    return recordatorios.filter {
        it.prioridad == Prioridad.ALTA
    }
}

// ======================================
// 9. ACTIVIDAD PRINCIPAL
// ======================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme {

                PantallaRecordatorios()
            }
        }
    }
}

// ======================================
// 10. PANTALLA PRINCIPAL
// ======================================

@Composable
fun PantallaRecordatorios() {

    var filtroSeleccionado by remember {
        mutableStateOf("Todos")
    }

    val listaFiltrada = when (filtroSeleccionado) {

        "Pendientes" -> obtenerPendientes()

        "Prioridad alta" -> obtenerAltaPrioridad()

        else -> recordatorios
    }

    Scaffold(

        topBar = {

            Surface(
                color = Color(0xFF263B63)
            ) {

                Text(
                    text = "Mis Recordatorios",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F5F9))
                .padding(padding)
                .padding(16.dp)

        ) {

            Text(
                text = "Organiza tus actividades",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263B63)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total de recordatorios: ${recordatorios.size}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Text(
                text = "Categorías registradas: ${categoriasUnicas.size}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // FILTROS

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                listOf(
                    "Todos",
                    "Pendientes",
                    "Prioridad alta"
                ).forEach { filtro ->

                    FilterChip(

                        selected = filtroSeleccionado == filtro,

                        onClick = {
                            filtroSeleccionado = filtro
                        },

                        label = {
                            Text(filtro)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LISTA DE RECORDATORIOS

            LazyColumn(

                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {

                items(
                    items = listaFiltrada,
                    key = { it.id }
                ) { recordatorio ->

                    TarjetaRecordatorio(recordatorio)
                }
            }
        }
    }
}

// ======================================
// 11. TARJETA DE RECORDATORIO
// ======================================

@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio
) {

    val colorCategoria =
        coloresCategorias[recordatorio.categoria]
            ?: Color.Gray

    val estadoTexto = when (recordatorio.estado) {

        is Estado.Pendiente -> "Pendiente"

        is Estado.EnProgreso -> "En progreso"

        is Estado.Completado -> "Completado"
    }

    val prioridadTexto = when (recordatorio.prioridad) {

        Prioridad.BAJA -> "Baja"

        Prioridad.MEDIA -> "Media"

        Prioridad.ALTA -> "Alta"
    }

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(14.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )

    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = recordatorio.titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263B63)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recordatorio.descripcion,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    color = colorCategoria,
                    shape = RoundedCornerShape(8.dp)
                ) {

                    Text(
                        text = recordatorio.categoria.nombre,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Prioridad: $prioridadTexto",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estado: $estadoTexto",
                fontSize = 13.sp,
                color = Color.DarkGray
            )

            recordatorio.fecha?.let { fecha ->

                Text(
                    text = "Fecha: $fecha",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            recordatorio.ubicacion?.let { ubicacion ->

                Text(
                    text = "Ubicación: $ubicacion",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}