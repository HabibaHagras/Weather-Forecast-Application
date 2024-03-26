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
        Log.i("TAG", "insertWeatherData: insertWeatherData")
        dao.insert(weatherData)
    }


    override suspend fun deleteWeatherData(weatherData: WeatherData) {
        Log.i("TAG", "deleteWeatherDataaaaaaaaaaaaaaaaaaaaaaaaaaa: ")
        dao.delete(weatherData)
    }

    override suspend fun getStoredProducts(): Flow<List<WeatherData>> {

        return dao.getAll()
//            .map { weatherDataEntities ->
//            weatherDataEntities.map { weatherDataEntity ->
//                // Fetch weather entities for the current weather data entity
//                val weatherEntities = dao.getWeatherByParentId(weatherDataEntity.id).firstOrNull() ?: emptyList()
//            WeatherData(
//                base = "baseValue",
//                clouds = Clouds(all = 0),
//                cod = 200,
//                coord = CoordWeather(lon =weatherDataEntity.coord.lon,lat = weatherDataEntity.coord.lat),
//                dt = 123456789,
//                main = weatherDataEntity.main,
//                name = weatherDataEntity.name,
//                sys = Sys(""),
//                timezone = 0,
//                visibility = 1000,
//                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
//                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
//            )
//            }
//        }
    }
//
//            WeatherData(
//                base = "baseValue",
//                clouds = Clouds(all = 0),
//                cod = 200,
//                coord = CoordWeather(lon =weatherDataEntity.coord.lon,lat = weatherDataEntity.coord.lat),
//                dt = 123456789,
//                main = weatherDataEntity.main,
//                name = weatherDataEntity.name,
//                sys = Sys(""),
//                timezone = 0,
//                visibility = 1000,
//                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
//                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
//            )



    override suspend fun insertWeatherHome(weather: Responce) {
        daoResponce.insertResponce(weather)
    }

    override suspend fun getStoredWeatherHome(): Flow<List<Responce>> {
        return daoResponce.getAllResponces()
    }
}
