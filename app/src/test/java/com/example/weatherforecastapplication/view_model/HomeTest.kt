package com.example.weatherforecastapplication.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.MainRule
import com.example.weatherforecastapplication.model2.City
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.Coord
import com.example.weatherforecastapplication.model2.FakeRepo
import com.example.weatherforecastapplication.model2.Listt
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model2.Rain
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.Wind
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val main = MainRule()
    lateinit var viewModel: home
    lateinit var repo: FakeRepo
    @Before
    fun setup(){
        repo=FakeRepo()
        viewModel=home(repo, SharedPreferencesManager.getInstance(ApplicationProvider.getApplicationContext()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllWeatherMap()= runBlockingTest{
        val result=viewModel.getAllWeatherMap()
        MatcherAssert.assertThat(result , not(nullValue()))
        MatcherAssert.assertThat(result,`is`(Unit))
        viewModel.getAllWeatherMap()
       val  jop= launch {
        viewModel.weatherStateFlow.collect {
            when (it) {
                is ApiState.Sucessed -> {
                    Assert.assertThat(it.data.city.country, IsEqual("EG"))
                }
                else -> {
                }
            }
        }
    }
        jop.cancel()
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllWeatherGps()= runBlockingTest{
        val result=viewModel.getAllWeatherGps()
        MatcherAssert.assertThat(result , not(nullValue()))
        MatcherAssert.assertThat(result,`is`(Unit))
        viewModel.getAllWeatherMap()
         val jop= launch {
            viewModel.weatherStateFlow.collect {
                when (it) {
                    is ApiState.Sucessed -> {
                        Assert.assertThat(it.data.city.name, IsEqual("Tanta"))
                        Assert.assertThat(it.data.city.coord.lat, IsEqual(30.7914776))
                    }
                    else -> {
                    }
                }
            }
        }
        jop.cancel()
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllWeatherFromFav()= runBlockingTest{
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
        val result=viewModel.getAllWeatherFromFav(30.7914776,30.9957296)
        viewModel.getAllWeatherFromFav(30.7914776,30.9957296)
        MatcherAssert.assertThat(result,`is`(Unit))
       val jop= launch {
            viewModel.weatherStateFlow.collect {
                when (it) {
                    is ApiState.Sucessed -> {
                        Assert.assertThat(it.data.city.coord.lon, IsEqual(30.9957296))
                    }
                    else -> {
                    }
                }
            }
        }
        jop.cancel()

    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getStoredHome()= runBlockingTest {
        val responce1=
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
       viewModel.insertHome(responce1)
        val result=viewModel.getStoredHome()
        MatcherAssert.assertThat(result,`is`(Unit))
        MatcherAssert.assertThat(result , not(nullValue()))
        viewModel.getStoredHome()
        val jop= launch {
            viewModel.weatherStateFlow.collect {
                when (it) {
                    is ApiState.Sucessed -> {
                        Assert.assertThat(it.data.city.coord.lon, IsEqual(responce1.city.coord.lon))
                    }
                    else -> {
                    }
                }
            }
        }
        jop.cancel()
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertHome()= runBlockingTest {
        val responce1=
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
        val result=  viewModel.insertHome(responce1)
        MatcherAssert.assertThat(result,`is`(Unit))
        val result2=viewModel.getStoredHome()
        MatcherAssert.assertThat(result2,`is`(Unit))
        MatcherAssert.assertThat(result2 , not(nullValue()))
        viewModel.getStoredHome()
      val jop=
          launch {
            viewModel.weatherStateFlow.collect {
                when (it) {
                    is ApiState.Sucessed -> {
                        Assert.assertThat(it.data.city.name, IsEqual(responce1.city.name))
                    }
                    else -> {
                    }
                }
            }
        }
        jop.cancel()
    }
}