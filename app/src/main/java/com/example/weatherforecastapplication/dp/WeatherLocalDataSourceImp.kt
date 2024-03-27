import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.dp.ResponceDao
import com.example.weatherforecastapplication.dp.WeatherDataDAO
import com.example.weatherforecastapplication.dp.WeatherLocalDataSource
import com.example.weatherforecastapplication.dp.db
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.Coord
import com.example.weatherforecastapplication.model2.CoordWeather
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Wind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class WeatherLocalDataSourceImp(context: Context) : WeatherLocalDataSource {
    private val dao: WeatherDataDAO by lazy {
        val dbInstance = db.getInstance(context)
        dbInstance.getWeatherDataDao()
    }
    private val daoResponce: ResponceDao by lazy {
        val dbInstance = db.getInstance(context)
        dbInstance.responceDao()

    }

    override suspend fun insertWeatherData(weatherData: WeatherData) {
        dao.insert(weatherData)
    }


    override suspend fun deleteWeatherData(weatherData: WeatherData) {
        dao.delete(weatherData)
    }

    override suspend fun getStoredProducts(): Flow<List<WeatherData>> {
        return dao.getAll()
    }



    override suspend fun insertWeatherHome(weather: Responce) {
        daoResponce.insertResponce(weather)
    }

    override suspend fun getStoredWeatherHome(): Flow<List<Responce>> {
        return daoResponce.getAllResponces()
    }
}
