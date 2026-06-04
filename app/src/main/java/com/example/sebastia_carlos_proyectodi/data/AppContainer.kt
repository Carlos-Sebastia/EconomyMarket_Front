import android.content.Context
import com.example.sebastia_carlos_proyectodi.data.local.AppDataBase
import com.example.sebastia_carlos_proyectodi.data.remote.ProductoApiService
import com.example.sebastia_carlos_proyectodi.data.remote.TiendaApiService
import com.example.sebastia_carlos_proyectodi.data.remote.UsuarioApiService
import com.example.sebastia_carlos_proyectodi.data.remote.NotificacionApiService
import com.example.sebastia_carlos_proyectodi.data.repository.ProductoRepositoryImpl
import com.example.sebastia_carlos_proyectodi.data.repository.TiendaRepositoryImpl
import com.example.sebastia_carlos_proyectodi.data.repository.UsuarioRepositoryImpl
import com.example.sebastia_carlos_proyectodi.data.repository.NotificacionRepositoryImpl
import com.example.sebastia_carlos_proyectodi.domain.repository.NotificacionRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.ProductoRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.TiendaRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {

    val usuarioRepository: UsuarioRepository
    val productoRepository: ProductoRepository
    val tiendaRepository: TiendaRepository
    val notificacionRepository: NotificacionRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val baseUrl = "http://10.0.2.2:8080/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val usuarioService: UsuarioApiService by lazy {
        retrofit.create(UsuarioApiService::class.java)
    }

    private val productoService: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    private val tiendaService: TiendaApiService by lazy {
        retrofit.create(TiendaApiService::class.java)
    }

    private val notificacionService: NotificacionApiService by lazy {
        retrofit.create(NotificacionApiService::class.java)
    }

    private val database: AppDataBase by lazy {
        AppDataBase.getDatabase(context)
    }

    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepositoryImpl(
            usuarioService,
            database.usuarioDao()
        )
    }

    override val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl(
            productoService,
            database.productoDao(),
            database.listaDao()

        )
    }

    override val tiendaRepository: TiendaRepository by lazy {
        TiendaRepositoryImpl(
            tiendaService,
            database.tiendaDao()

        )
    }

    override val notificacionRepository: NotificacionRepository by lazy {
        NotificacionRepositoryImpl(
            notificacionService
        )
    }
}