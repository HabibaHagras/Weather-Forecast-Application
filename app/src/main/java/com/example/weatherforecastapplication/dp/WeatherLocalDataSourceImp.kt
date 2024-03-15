import android.content.Context
import com.example.weatherforecastapplication.dp.WeatherDataDAO
import com.example.weatherforecastapplication.dp.WeatherLocalDataSource
import com.example.weatherforecastapplication.dp.db
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData

class WeatherLocalDataSourceImp(context: Context) : WeatherLocalDataSource {
    private val dao: WeatherDataDAO by lazy {
        val dbInstance = db.getInstance(context)
        dbInstance.getWeatherDataDao()
    }

    override suspend fun insertWeather(weather: Weather) {
        // Not needed for WeatherDataDAO
    }

    override suspend fun insertWeatherData(weatherData: WeatherData) {
        dao.insert(weatherData)
    }

    override suspend fun deleteWeather(weather: Weather) {
        // Not needed for WeatherDataDAO
    }

    override suspend fun deleteWeatherData(weatherData: WeatherData) {
        // Not needed for WeatherDataDAO
    }

    override suspend fun getStoredProducts(): List<WeatherData> {
        return dao.getAll().map { weatherDataEntity ->
            val weatherEntities = dao.getWeatherByParentId(weatherDataEntity.id)
            val weatherList = weatherEntities.map { weatherEntity ->
                Weather(
                    description = weatherEntity.description,
                    icon = weatherEntity.icon,
                    id = weatherEntity.id,
                    main = weatherEntity.main
                )
            }
            WeatherData(
                name = weatherDataEntity.name,
                main = weatherDataEntity.main,
                weather = weatherList
            )
        }
    }
}
