package com.br.projetodevacina.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.br.projetodevacina.api.RetrofitClient
import com.br.projetodevacina.data.HealthCenter

@Composable
fun HealthCentersScreen(
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    var healthCenters by remember { mutableStateOf<List<HealthCenter>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            errorMessage = null

            val response = RetrofitClient.apiService.getHealthCenters()
            healthCenters = response.estabelecimentos ?: emptyList()

        } catch (e: Exception) {
            errorMessage = "Erro ao carregar postos: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    val filteredCenters = healthCenters.filter { center ->
        if (searchQuery.isBlank()) {
            true
        } else {
            val nameMatches = center.name?.contains(searchQuery, ignoreCase = true)
            val vaccineMatches = center.availableVaccines?.any { it.contains(searchQuery, ignoreCase = true) } == true
            nameMatches == true || vaccineMatches
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar por nome ou vacina (ex: UBS, Gripe)") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onOpenMap,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Map, contentDescription = "Mapa")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver postos no Mapa")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Postos Encontrados (${filteredCenters.size})",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            filteredCenters.isEmpty() -> {
                Text(
                    text = "Nenhum posto encontrado.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredCenters) { center ->
                        HealthCenterCard(center = center)
                    }
                }
            }
        }
    }
}

@Composable
fun HealthCenterCard(center: HealthCenter) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = center.formattedName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Endereço",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))

                val fullAddress = buildString {
                    append(center.address ?: "Endereço não informado")
                    if (!center.neighborhood.isNullOrBlank()) {
                        append(" - ${center.neighborhood}")
                    }
                }

                Text(
                    text = fullAddress,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val vaccinesText = center.availableVaccines?.joinToString(", ")
            if (!vaccinesText.isNullOrBlank()) {
                Text(
                    text = "Vacinas informadas:",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = vaccinesText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}