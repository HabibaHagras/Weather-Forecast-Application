package com.example.weatherforecastapplication.dp

import WeatherLocalDataSourceImp
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.example.weatherforecastapplication.model2.City
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.Coord
import com.example.weatherforecastapplication.model2.CoordWeather
import com.example.weatherforecastapplication.model2.Listt
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model2.Rain
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Wind
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test

@MediumTest
class WeatherLocalDataSourceTest {
    private lateinit var database: db
    private lateinit var localDataSource:WeatherLocalDataSource
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            db::class.java
        )

            .build()
        localDataSource = WeatherLocalDataSourceImp(ApplicationProvider.getApplicationContext())
    }
    @After
    fun teardown() {
        // close the database
        database.close()
    }

    @Test
    fun insertWeatherData()= runTest{
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
        localDataSource.insertWeatherData(weather1)
        val result = localDataSource.getStoredProducts()
        MatcherAssert.assertThat(result.first().get(0), CoreMatchers.`is`(weather1))
        MatcherAssert.assertThat(result.first()[0].id, CoreMatchers.`is`(weather1.id))


    }
    @Test
    fun deleteWeatherData()= runTest{
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
                name = "Tanta",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        localDataSource.insertWeatherData(weather1)
        localDataSource.insertWeatherData(weather2)
            val result = localDataSource.getStoredProducts()
        MatcherAssert.assertThat(result.first()[0], CoreMatchers.`is`(weather1))
        MatcherAssert.assertThat(result.first()[1], CoreMatchers.`is`(weather2))
         localDataSource.deleteWeatherData(weather2)
        val result2 = localDataSource.getStoredProducts()
        MatcherAssert.assertThat(result2.first().size, CoreMatchers.`is`(1))
        }

    @Test
    fun getStoredProducts()= runTest{
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
                name = "Tanta",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        localDataSource.insertWeatherData(weather1)
        localDataSource.insertWeatherData(weather2)
        val result = localDataSource.getStoredProducts()
        MatcherAssert.assertThat(result.first()[0], CoreMatchers.`is`(weather1))
        MatcherAssert.assertThat(result.first()[1], CoreMatchers.`is`(weather2))
        MatcherAssert.assertThat(result.first().size, CoreMatchers.`is`(2))
    }
    @Test
    fun insertWeatherHome()= runTest{
        val weather1=
            Responce(1, City(
                Coord(30.7914776,30.9957296),"EG",
                347497,"Tanta",15000,1711338799,1711383021,7200),40,"200"
                ,listOf(
                    Listt(
                        Clouds(0),
                        0,
                        "dt_txt",
                        Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                        0.0,
                        Rain(0.0),
                        Sys("pod"),
                        0,
                        listOf(Weather("description", "icon", 0, "main")),
                        Wind(0, 0.0, 0.0)
                    )
                ),0

            )
        localDataSource.insertWeatherHome(weather1)
        val result = localDataSource.getStoredWeatherHome()
        MatcherAssert.assertThat(result.first().get(0), CoreMatchers.`is`(weather1))
        MatcherAssert.assertThat(result.first()[0].id, CoreMatchers.`is`(weather1.id))


    }
    @Test
    fun getStoredWeatherHome()= runTest{
        val weather1=
            Responce(1, City(
                Coord(30.7914776,30.9957296),"EG",
                347497,"Tanta",15000,1711338799,1711383021,7200),40,"200"
                ,listOf(
                    Listt(
                        Clouds(0),
                        0,
                        "dt_txt",
                        Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                        0.0,
                        Rain(0.0),
                        Sys("pod"),
                        0,
                        listOf(Weather("description", "icon", 0, "main")),
                        Wind(0, 0.0, 0.0)
                    )
                ),0

            )
        val weather2=
            Responce(2, City(
                Coord(30.7914776,30.9957296),"EG",
                347497,"Tanta",15000,1711338799,1711383021,7200),40,"200"
                ,listOf(
                    Listt(
                        Clouds(0),
                        0,
                        "dt_txt",
                        Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                        0.0,
                        Rain(0.0),
                        Sys("pod"),
                        0,
                        listOf(Weather("description", "icon", 0, "main")),
                        Wind(0, 0.0, 0.0)
                    )
                ),0

            )
        localDataSource.insertWeatherHome(weather1)
        localDataSource.insertWeatherHome(weather2)
        val result = localDataSource.getStoredWeatherHome()
        MatcherAssert.assertThat(result.first()[0], CoreMatchers.`is`(weather1))
        MatcherAssert.assertThat(result.first()[1], CoreMatchers.`is`(weather2))
        MatcherAssert.assertThat(result.first().size, CoreMatchers.`is`(2))
    }
}