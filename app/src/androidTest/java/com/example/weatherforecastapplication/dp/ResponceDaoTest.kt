package com.example.weatherforecastapplication.dp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weatherforecastapplication.model2.City
import com.example.weatherforecastapplication.model2.Clouds
import com.example.weatherforecastapplication.model2.Coord
import com.example.weatherforecastapplication.model2.Listt
import com.example.weatherforecastapplication.model2.Main
import com.example.weatherforecastapplication.model2.Rain
import com.example.weatherforecastapplication.model2.Responce
import com.example.weatherforecastapplication.model2.Sys
import com.example.weatherforecastapplication.model2.Weather
import com.example.weatherforecastapplication.model2.Wind
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResponceDaoTest {
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
    fun insertResponce()= runBlockingTest {
        //given
        val response = Responce(1, City(
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

        val responce2= Responce(2, City(
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
        database.responceDao().insertResponce(response)
        database.responceDao().insertResponce(responce2)
        // WHEN
        val loaded = database.responceDao().getAllResponces().first()
        //Then
        MatcherAssert.assertThat(loaded[0], CoreMatchers.`is`(response))
        MatcherAssert.assertThat(loaded[1].id, CoreMatchers.`is`(responce2.id))


    }

    @Test
    fun getAllResponces()= runBlockingTest {

    }

}