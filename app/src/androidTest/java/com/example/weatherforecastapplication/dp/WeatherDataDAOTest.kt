package com.example.weatherforecastapplication.dp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.CoordWeather
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Wind
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherDataDAOTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: db
    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            db::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()


    @Test
    fun getAll()= runBlockingTest {
        val weather1=
            WeatherData(
                id=1,
                base = "baseValue",
                clouds = Clouds(all = 0),
                cod = 200,
                coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                dt = 123456789,
                main = Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                name = "Cairo",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        database.getWeatherDataDao().insert(weather1)
        val result=database.getWeatherDataDao().getAll().first()
        //Then
        MatcherAssert.assertThat(result[0], CoreMatchers.`is`(weather1))
    }
    @Test
    fun insert()= runBlockingTest {
        val weather1=
            WeatherData(
                id=1,
                base = "baseValue",
                clouds = Clouds(all = 0),
                cod = 200,
                coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                dt = 123456789,
                main = Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                name = "Tanta",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        val weather2=
            WeatherData(
                id=2,
                base = "baseValue",
                clouds = Clouds(all = 0),
                cod = 200,
                coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                dt = 123456789,
                main = Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                name = "Cairo",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        database.getWeatherDataDao().insert(weather1)
        database.getWeatherDataDao().insert(weather2)
        val result=database.getWeatherDataDao().getAll().first()
        //Then
        MatcherAssert.assertThat(result[0], CoreMatchers.`is`(weather1))
        MatcherAssert.assertThat(result[1], CoreMatchers.`is`(weather2))
        MatcherAssert.assertThat(result[1].name, CoreMatchers.`is`(weather2.name))
    }
    @Test
    fun delete()= runBlockingTest {
        val weather1=
            WeatherData(
                id=1,
                base = "baseValue",
                clouds = Clouds(all = 0),
                cod = 200,
                coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                dt = 123456789,
                main = Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                name = "Tanta",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        val weather2=
            WeatherData(
                id=2,
                base = "baseValue",
                clouds = Clouds(all = 0),
                cod = 200,
                coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                dt = 123456789,
                main = Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                name = "Cairo",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        database.getWeatherDataDao().insert(weather1)
        database.getWeatherDataDao().insert(weather2)
        val result=database.getWeatherDataDao().getAll().first()
        //Then
        MatcherAssert.assertThat(result[0], CoreMatchers.`is`(weather1))
        MatcherAssert.assertThat(result[1], CoreMatchers.`is`(weather2))
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(result[1].name, CoreMatchers.`is`(weather2.name))
        database.getWeatherDataDao().delete(weather2)
        val result2=database.getWeatherDataDao().getAll().first()
        MatcherAssert.assertThat(result2[0], CoreMatchers.`is`(weather1))
        MatcherAssert.assertThat(result2.size, CoreMatchers.`is`(1))


    }





}