import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.dp.AlarmDao
import com.example.weatherforecastapplication.dp.ResponceDao
import com.example.weatherforecastapplication.dp.WeatherDataDAO
import com.example.weatherforecastapplication.dp.WeatherLocalDataSource
import com.example.weatherforecastapplication.dp.db
import com.example.weatherforecastapplication.model2.Alarm
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
    private val daoAlarm: AlarmDao by lazy {
        val dbInstance = db.getInstance(context)
        dbInstance.AlarmDao()
    }

    override suspend fun insertWeatherData(weatherData: WeatherData) {
        dao.insert(weatherData)
    }


    override suspend fun deleteWeatherData(weatherData: WeatherData) {
        dao.delete(weatherData)
    }

    override  fun getStoredProducts(): Flow<List<WeatherData>> {
        return dao.getAll()
    }



    override suspend fun insertWeatherHome(weather: Responce) {
        daoResponce.insertResponce(weather)
    }

    override  fun getStoredWeatherHome(): Flow<List<Responce>> {
        return daoResponce.getAllResponces()
    }

    override  fun getStoredAlarms(): Flow<List<Alarm>> {
        return  daoAlarm.getAllAlarms()
    }

    override suspend fun insertAlarms(alarm: Alarm) {
        daoAlarm.insertAlarm(alarm)
    }

    override suspend fun deleteAlarms(alarm: Alarm) {
        daoAlarm.deleteAlarm(alarm)
    }
}
