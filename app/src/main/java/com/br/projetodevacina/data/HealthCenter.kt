package com.br.projetodevacina.data

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class HealthCenter(
    @SerializedName("cnes")
    val cnes: String? = null,

    @SerializedName("nome_fantasia")
    val name: String? = null,

    @SerializedName("descricao_logradouro")
    val address: String? = null,

    @SerializedName("bairro")
    val neighborhood: String? = null,

    @SerializedName("latitude")
    val rawLatitude: String? = null,

    @SerializedName("longitude")
    val rawLongitude: String? = null,

    val availableVaccines: List<String>? = null,
    var distanceKm: Double? = null
){
    val latitude: Double?
        get() = rawLatitude?.toDoubleOrNull()

    val longitude: Double?
        get() = rawLongitude?.toDoubleOrNull()
    val position: LatLng?
        get() = if (latitude != null && longitude != null) {
            LatLng(latitude!!, longitude!!)
        } else {
            null
        }
    val formattedName: String
        get() {
            if (name.isNullOrBlank()) return "Unidade de Saúde"
            return name.replace(
                Regex("^(US|CS|USF|UBDS|UMA|Policlínica)\\s*\\d*\\s*", RegexOption.IGNORE_CASE),
                ""
            ).trim().ifEmpty { name }
        }
}