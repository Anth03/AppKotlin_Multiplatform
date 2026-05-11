package com.example.applicationcompose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import applicationcompose.composeapp.generated.resources.Res
import applicationcompose.composeapp.generated.resources.compose_multiplatform

fun formatTime(localTime: kotlinx.datetime.LocalTime): String {
    val hour = localTime.hour.toString().padStart(2, '0')
    val minute = localTime.minute.toString().padStart(2, '0')
    val second = localTime.second.toString().padStart(2, '0')
    return "$hour:$minute:$second"
}

// Función que calcula la hora actual en una ubicación
fun currentTimeAt(location: String, timeZoneId: String): String? {
    return try {
        val zone = TimeZone.of(timeZoneId)
        val now = Clock.System.now()
        val localTime = now.toLocalDateTime(zone).time
        "The time in $location is ${formatTime(localTime)}"
    } catch (e: IllegalTimeZoneException) {
        null
    }
}

// Data class para país con imagen
data class Country(
    val name: String,
    val timeZone: String,
    val flagResource: DrawableResource
)

// Lista de países con sus banderas
val countries = listOf(
    Country("Japan", "Asia/Tokyo", Res.drawable.japan),
    Country("France", "Europe/Paris", Res.drawable.france),
    Country("Mexico", "America/Mexico_City", Res.drawable.mexico),
    Country("Indonesia", "Asia/Jakarta", Res.drawable.indonesia),
    Country("Egypt", "Africa/Cairo", Res.drawable.egypt)
)

@Composable
@Preview
fun App() {
    MaterialTheme {
        var selectedCountry by remember { mutableStateOf(countries[0]) }
        var timeAtLocation by remember { mutableStateOf("Select a country to see the time") }
        var expanded by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Local Time App",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Tarjeta que muestra la hora
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = timeAtLocation,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // Dropdown menu con banderas
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCountry.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select a country") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Imagen de la bandera
                                        Image(
                                            painter = painterResource(country.flagResource),
                                            contentDescription = "${country.name} flag",
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                        )
                                        Text(
                                            text = country.name,
                                            fontSize = 16.sp
                                        )
                                    }
                                },
                                onClick = {
                                    selectedCountry = country
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón para mostrar la hora
                Button(
                    onClick = {
                        val time = currentTimeAt(selectedCountry.name, selectedCountry.timeZone)
                        timeAtLocation = time ?: "Error: Invalid timezone"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Show Current Time",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Texto informativo
                Text(
                    text = "Select a country and tap the button to see its current time",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}