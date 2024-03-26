package com.example.weatherforecastapplication.model2

import com.example.weatherforecastapplication.dp.WeatherLocalDataSource
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.network.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test


class RepositoryImpTest{
    lateinit var  fakeremote : RemoteDataSource
    lateinit var  fakelocal : WeatherLocalDataSource
    lateinit var  repo :RepositoryImp
    val task1=
        Responce(0,City(Coord(30.7914776,30.9957296),"EG",
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
    val task2=
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
    val task22=
        WeatherData(
            id=1,
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
    val task3= mutableListOf<WeatherData>(task2,task22)
    val task4=  mutableListOf<Responce>(task1,task1,task1)
//    val task5=Task("task5")
    @Before
    fun createRepository() {
        fakeremote = FakeRemoteDataSource(task1,task2)
        fakelocal = FakeLocalDataSource(task3,task4)
        repo = RepositoryImp(
            fakeremote, fakelocal
        )
    }
    @Test
   fun getWeatherOverNetwork_allTasksFromRemoteDataSource()=runBlockingTest{
        val result=repo.getAllWeather(30.7914776,30.9957296,"7f6473d2786753ccda5811e204914fff","en","")
        MatcherAssert.assertThat(result, `is`(task1))
    }



    @Test
    fun getWeatherWithCity_allTasksFromRemoteDataSource()=runBlockingTest{
        val result=repo.getWeatherWithCity(30.7914776,30.9957296,"7f6473d2786753ccda5811e204914fff","")
        MatcherAssert.assertThat(result.first().name, `is`(task2.name))
    }

    @Test
    fun getWeatherWithCity2_allTasks()= runBlockingTest{
        val result=repo.getWeatherWithCity2("Tanta","7f6473d2786753ccda5811e204914fff")
        MatcherAssert.assertThat(result, `is`(task2))
    }

    @Test
    fun insertWeatherData()= runBlockingTest{
        var weather= WeatherData(
            id=1,

            base = "baseValue",
            clouds = Clouds(all = 0),
            cod = 404,
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
        repo.insertWeatherData(weather)
//        repo.insertWeatherData(weather2)
        val result = repo.getStored().first()
        val latitudes = result.map { it.coord.lat }

        MatcherAssert.assertThat(result.size, `is`(3))
        MatcherAssert.assertThat(latitudes[0], `is`(weather.coord.lat))
        MatcherAssert.assertThat(result[2].cod, `is`(404))

    }


    @Test
    fun getStored()= runBlockingTest {
        val result=repo.getStored().last()
        MatcherAssert.assertThat(result[0].name, `is`(task2.name))
        MatcherAssert.assertThat(result[1].name, `is`(task22.name))
    }

    @Test
    fun getStoredHome()= runBlockingTest {
        val result=repo.getStoredHome().first()
        MatcherAssert.assertThat(result[0].city, `is`(task1.city))
        MatcherAssert.assertThat(result[1].id, `is`(0))
    }


    @Test
    fun insertResponceData()= runBlockingTest {
        val responce= Responce(3,City(Coord(51.5085,-0.1257),"GB",
            347497,"London",15000,1711338799,1711383021,7200),40,"200"
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
        repo.insertResponceData(responce)
        val result = repo.getStoredHome().first()
        MatcherAssert.assertThat(result.size, `is`(4))


    }

    @Test
    fun deletetWeatherData()= runBlockingTest{
        repo.deletetWeatherData(task2)
        val result=repo.getStored().first()
        MatcherAssert.assertThat(result.size, `is`(1))
    }

}