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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
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
        MatcherAssert.assertThat(viewModel.weatherStateFlow.value as ApiState.Sucessed,`is`(ApiState.loading))
        MatcherAssert.assertThat(viewModel.weatherStateFlow.first(),`is`(ApiState.loading))
        MatcherAssert.assertThat(result,`is`(Unit))
//
//// Assert that the weather state is still loading
//        MatcherAssert.assertThat(viewModel.weatherStateFlow.value, `is`(ApiState.Sucessed::class.java))
//
//        delay(8001)
//        val successData = (viewModel.weatherStateFlow.value as ApiState.Sucessed).data
//
//// Assert the id of the response
//        MatcherAssert.assertThat(successData.id, `is`(task1.id))
    }
    @Test
    fun getAllWeatherGps()= runBlockingTest{




    }
}