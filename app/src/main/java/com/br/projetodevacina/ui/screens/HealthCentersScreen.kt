package com.br.projetodevacina.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.br.projetodevacina.data.HealthCenter

@Composable
fun HealthCentersScreen(
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val sampleCenters = remember {
        listOf(
            HealthCenter(
                cnes = "001",
                name = "UBS Mário Pinotti",
                address = "Rua das Flores, 123",
                neighborhood = "Centro",
                latitude = -8.0522,
                longitude = -34.8856,
                availableVaccines = listOf("Gripe", "COVID-19", "Hepatite B"),
                distanceKm = 1.2
            ),
            HealthCenter(
                cnes = "002",
                name = "Centro de Saúde Boa Vista",
                address = "Av. Agamenon Magalhães, 450",
                neighborhood = "Boa Vista",
                latitude = -8.0580,
                longitude = -34.8910,
                availableVaccines = listOf("Gripe", "Dengue", "Tríplice Viral"),
                distanceKm = 2.5
            )
        )
    }

    val filteredCenters = sampleCenters.filter { center ->
        if (searchQuery.isBlank()) true
        else center.availableVaccines.any { it.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Qual vacina você procura? (ex: Gripe)") },
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
            Text("Ver postos próximos no Mapa")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Postos Encontrados (${filteredCenters.size})",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredCenters.isEmpty()) {
            Text(
                text = "Nenhum posto encontrado com a vacina informada.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
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

@Composable
fun HealthCenterCard(center: HealthCenter) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = center.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${center.distanceKm} km",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Endereço",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${center.address} - ${center.neighborhood}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Vacinas informadas:",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = center.availableVaccines.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}