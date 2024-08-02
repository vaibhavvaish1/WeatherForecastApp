import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {
    @GET("geo/1.0/direct")
    suspend fun searchPlaces(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<Place>
}

data class Place(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)

object RetrofitInstance {
    private const val BASE_URL = "https://api.openweathermap.org/"

    private val gson: Gson = GsonBuilder().create()

    val api: OpenWeatherMapApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpenWeatherMapApi::class.java)
    }
}
