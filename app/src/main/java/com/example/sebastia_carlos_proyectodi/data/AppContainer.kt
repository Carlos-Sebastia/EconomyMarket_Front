import com.example.sebastia_carlos_proyectodi.data.remote.ProductoApiService
import com.example.sebastia_carlos_proyectodi.data.remote.TiendaApiService // OJO: Verifica que se llame así
import com.example.sebastia_carlos_proyectodi.data.repository.ProductoRepositoryImpl
import com.example.sebastia_carlos_proyectodi.data.repository.TiendaRepositoryImpl
import com.example.sebastia_carlos_proyectodi.domain.repository.ProductoRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.TiendaRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// data/AppContainer.kt
interface AppContainer {
    val productoRepository: ProductoRepository
    val tiendaRepository: TiendaRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://10.0.2.2:8080" // Tu código de RetrofitInstance aquí

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    // Las instancias de las APIs ya no son objetos globales, viven aquí dentro
    private val retrofitService: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    private val tiendaService: TiendaApiService by lazy {
        retrofit.create(TiendaApiService::class.java)
    }

    // Los repositorios se crean pasando esas APIs
    override val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl(retrofitService)
    }

    override val tiendaRepository: TiendaRepository by lazy {
        TiendaRepositoryImpl(tiendaService)
    }
}