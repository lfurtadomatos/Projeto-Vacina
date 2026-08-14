package com.br.projetodevacina.data

import com.google.android.gms.maps.model.LatLng

data class HealthCenter(
    val cnes: String = "",
    val name: String = "",
    val address: String = "",
    val neighborhood: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val availableVaccines: List<String> = emptyList(),
    val distanceKm: Double = 0.0
) {
    val position: LatLng
        get() = LatLng(latitude, longitude)
}