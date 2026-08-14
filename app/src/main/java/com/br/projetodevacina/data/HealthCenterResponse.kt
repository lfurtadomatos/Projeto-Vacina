package com.br.projetodevacina.data

import com.google.gson.annotations.SerializedName

data class HealthCenterResponse(
    @SerializedName("estabelecimentos")
    val estabelecimentos: List<HealthCenter>
)