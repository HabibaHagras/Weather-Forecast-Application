package com.example.weatherforecastapplication.view_model

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeTest {
    lateinit var viewModel: home
    lateinit var repo: FakeRepo

    @Before
    fun setup(){
        repo=FakeRepo()
        viewModel=home(repo, SharedPreferencesManager.getInstance(ApplicationProvider.getApplicationContext()))
    }

    @Test
    fun getAllWeatherMap()= runBlockingTest{
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
        val result=viewModel.getAllWeatherMap()
        viewModel.getAllWeatherMap()
        MatcherAssert.assertThat(result , not(nullValue()))
        MatcherAssert.assertThat(result,`is`(Unit))


    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllWeatherGps()= runBlockingTest{

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
        val result=viewModel.getAllWeatherGps()
        viewModel.getAllWeatherMap()
        MatcherAssert.assertThat(viewModel.weatherStateFlow.first(),`is`(ApiState.loading))
        MatcherAssert.assertThat(result,`is`(Unit))
        val job = launch {
            viewModel.weatherStateFlow.collect {
                when (it) {
                    is ApiState.Sucessed -> {
                        Assert.assertThat(it.data.city.name, IsEqual("Tanta"))
                    }
                    else -> {
                    }
                }
            }
        }
        job.cancelAndJoin()
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
        viewModel.getAllWeatherMap()
        MatcherAssert.assertThat(result,`is`(Unit))
        val job = launch {
            viewModel.weatherStateFlow.collect {
                when (it) {
                    is ApiState.Sucessed -> {
                        Assert.assertThat(it.data.city.coord.lat, IsEqual(task1.city.coord.lat)) }
                    else -> {
                    }
                }
            }
        }
        job.cancelAndJoin()
    }

    @Test
    fun getStoredHome()= runBlockingTest {
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
       viewModel.insertHome(task1)
        val result=viewModel.getStoredHome()
        MatcherAssert.assertThat(result,`is`(Unit))
        MatcherAssert.assertThat(result , not(nullValue()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertHome()= runBlockingTest {
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
        val result=  viewModel.insertHome(task1)
        MatcherAssert.assertThat(result,`is`(Unit))
        viewModel.getStoredHome()
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
        job.cancelAndJoin()
    }
}