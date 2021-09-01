package com.example.tvclient.data.mvapi

import com.example.tvclient.data.Response
import com.google.gson.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.reflect.Type
import javax.inject.Inject

//import kotlinx.serialization.Serializable

//@Serializable
data class InternalResponse<T> (
    val errorCode: Int,
    val result: T
)

interface ApiService {
    @GET("jsonRequest?action=getChannelCategoryList")
    suspend fun getChannelCategoryList(): InternalResponse<List<ChannelCategoryDto>>
}

class MwApi @Inject constructor() {
    companion object {
        private const val BASE_URL = "https://try3.mediastage.tv/Subscriber/openAPI/"
    }

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(provideGson()))
        .build()
        .create(ApiService::class.java)

    private fun provideGson() : Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeHierarchyAdapter(InternalResponse::class.java, ResponseTypeDeserializer())
        return gsonBuilder.create()
    }

    suspend fun getChannelCategoryList(): Response<List<ChannelCategoryDto>> = convertResponse(apiService.getChannelCategoryList())

    private fun <T> convertResponse(r: InternalResponse<T>) =
        when (r.errorCode) {
            0 -> Response.Success(r.result as T)
            else -> Response.Error(Exception("MW error, code = ${r.errorCode}, message = ${r.result as String}"))
        }
}

class ResponseTypeDeserializer : JsonDeserializer<InternalResponse<*>> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): InternalResponse<*> {
        val gson = Gson()
        val response = json?.asJsonObject
        val errorCode = response?.getAsJsonPrimitive("errorCode")?.asInt ?: -1

        if (errorCode != 0) {
            val errorMsg = response?.getAsJsonPrimitive("result")?.asString ?: ""
            return InternalResponse<String>(errorCode, errorMsg)
        } else {
            return gson.fromJson(json, typeOfT)
        }
    }
}
