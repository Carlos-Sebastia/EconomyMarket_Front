import android.content.Context
import com.example.sebastia_carlos_proyectodi.data.local.AppDataBase
import com.example.sebastia_carlos_proyectodi.data.remote.ProductoApiService
import com.example.sebastia_carlos_proyectodi.data.remote.TiendaApiService // OJO: Verifica que se llame así
import com.example.sebastia_carlos_proyectodi.data.repository.ProductoRepositoryImpl
import com.example.sebastia_carlos_proyectodi.data.repository.TiendaRepositoryImpl
import com.example.sebastia_carlos_proyectodi.domain.repository.ProductoRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.TiendaRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val productoRepository: ProductoRepository
    val tiendaRepository: TiendaRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val baseUrl = "http://10.0.2.2:8080/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    // Las instancias de las APIs ya no son objetos globales, viven aquí dentro
    private val productoService: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    private val tiendaService: TiendaApiService by lazy {
        retrofit.create(TiendaApiService::class.java)
    }

    private val database: AppDataBase by lazy {
        AppDataBase.getDatabase(context)
    }

    // Los repositorios se crean pasando esas APIs
    override val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl(
            productoService,
            database.productoDao()

        )
    }

    override val tiendaRepository: TiendaRepository by lazy {
        TiendaRepositoryImpl(
            tiendaService,
            database.tiendaDao()

        )
    }
}