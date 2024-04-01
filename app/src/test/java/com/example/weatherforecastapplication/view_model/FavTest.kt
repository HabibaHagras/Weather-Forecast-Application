package com.example.weatherforecastapplication.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.MainRule
import com.example.weatherforecastapplication.model2.City
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.Coord
import com.example.weatherforecastapplication.model2.CoordWeather
import com.example.weatherforecastapplication.model2.FakeRepo
import com.example.weatherforecastapplication.model2.Listt
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model2.Rain
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.WeatherData
import com.example.weatherforecastapplication.model2.Wind
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class FavTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val main = MainRule()
    lateinit var viewModel: Fav
    lateinit var repo: FakeRepo
    @Before
    fun setup(){
        repo=FakeRepo()
        viewModel= Fav(repo, "",SharedPreferencesManager.getInstance(ApplicationProvider.getApplicationContext()))
    }
    @Test
    fun getStored()= runBlockingTest {
        val task1=
            WeatherData(
                id=1,
                base = "baseValue",
                clouds = Clouds(all = 0),
                cod = 200,
                coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                dt = 123456789,
                main =Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                name = "Tanta",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        viewModel.insertProducts(task1)
        val result=viewModel.getStored()
        MatcherAssert.assertThat(result, CoreMatchers.`is`(Unit))
        MatcherAssert.assertThat(result , CoreMatchers.not(CoreMatchers.nullValue()))
        val job = launch {
            viewModel.weatherStateFlow.collect {
                when (it) {

                    is ApiState.SucessWeatherData -> {
                        Assert.assertThat(it.data[0].name, IsEqual(task1.name))
                    }
                    else -> {
                    }
                }
            }
        }
        job.cancel()
    }
    @Test
    fun insertProducts()= runBlockingTest {
        val task1=
            WeatherData(
                id=1,
                base = "baseValue",
                clouds = Clouds(all = 0),
                cod = 200,
                coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                dt = 123456789,
                main =Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                name = "Tanta",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        val result=  viewModel.insertProducts(task1)
        MatcherAssert.assertThat(result, CoreMatchers.`is`(Unit))
    }
    @Test
    fun deleteWeather()= runBlockingTest {
            val task1=
                WeatherData(
                    id=1,
                    base = "baseValue",
                    clouds = Clouds(all = 0),
                    cod = 200,
                    coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                    dt = 123456789,
                    main =Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                    name = "Tanta",
                    sys = Sys(""),
                    timezone = 0,
                    visibility = 1000,
                    weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                    wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
                )
        val task2=
            WeatherData(
                id=2,
                base = "baseValue",
                clouds = Clouds(all = 0),
                cod = 200,
                coord = CoordWeather(lon =30.9957296,lat = 30.7914776),
                dt = 123456789,
                main =Main(0.0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0),
                name = "Cairo",
                sys = Sys(""),
                timezone = 0,
                visibility = 1000,
                weather = listOf(Weather(description = "description", icon = "icon", id = 800, main = "main")),
                wind = Wind(deg = 0, gust = 0.0, speed = 0.0)
            )
        viewModel.insertProducts(task1)
         viewModel.insertProducts(task2)
        val result=  viewModel.deleteWeather(task1)

        MatcherAssert.assertThat(result, CoreMatchers.`is`(Unit))
        viewModel.getStored()
        val job = launch {
            viewModel.weatherStateFlow.collect {
                when (it) {
                    is ApiState.SucessWeatherData -> {
                        Assert.assertThat(it.data.size ,`is`(1))
                    }
                    else -> {
                    }
                }
            }
        }
        job.cancel()
        }
    @Test
    fun getAllWeather()= runBlockingTest{
        val task1=
            Responce(0, City(
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
        val result=viewModel.getAllWeather("Tanta")
        viewModel.getAllWeather("Tanta")
        MatcherAssert.assertThat(result, CoreMatchers.`is`(Unit))
        val job = launch {
            viewModel.weatherStateFlow.collect {
                when (it) {

                    is ApiState.Sucessed -> {
                        Assert.assertThat(it.data.city.name, IsEqual(task1.city.name))
                    }
                    else -> {
                    }
                }
            }
        }
        job.cancel()
    }

}