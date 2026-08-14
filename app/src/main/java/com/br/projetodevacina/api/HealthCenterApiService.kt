package com.br.projetodevacina.api

import com.br.projetodevacina.data.HealthCenterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HealthCenterApiService {
    @GET("cnes/estabelecimentos")
    suspend fun getHealthCenters(
        @Query("codigo_tipo_unidade") tipoUnidade: String = "2",
        @Query("codigo_uf") codigoUf: String = "26",
        @Query("codigo_municipio") codigoMunicipio: String = "261160",
        @Query("status") status: String = "1",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 1
    ): HealthCenterResponse
}