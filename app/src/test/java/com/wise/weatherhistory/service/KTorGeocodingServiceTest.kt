package com.wise.weatherhistory.service

import com.wise.weatherhistory.model.GeocodingService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test


class KTorGeocodingServiceTest{
    @Test
    fun `parameters is passed correctly`() {
        runBlocking {
            val mockEngine = MockEngine { _ ->
                respond(
                    JSON_RESPONSE,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            KTorGeocodingService(buildHttpClient(mockEngine)).getLocations("xxx",
                GeocodingService.RequestParameter(10)
            )
            mockEngine.requestHistory.first().also {
                assertEquals(it.method, HttpMethod.Get)
                assertEquals(it.url.parameters["name"], "xxx")
                assertEquals(it.url.parameters["count"], "10")
                assertEquals(it.url.parameters["format"], "json")
            }
        }
    }

    @Test
    fun `response is passed correctly`() {
        runBlocking {
            val mockEngine = MockEngine { _ ->
                respond(
                    JSON_RESPONSE,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val response = KTorGeocodingService(buildHttpClient(mockEngine)).getLocations("xxx")
            assertEquals(response.size,1)
            response.first().also {
                assertEquals("Germany",it.country)
                assertEquals("Berlin, Stadt",it.name)
                assertEquals(52.52437f,it.latitude)
                assertEquals(13.41053f,it.longitude)
            }
        }
    }


    companion object{
        const val JSON_RESPONSE  = """
            {
            "results":[
              {
                 "id":2950159,
                 "name":"Berlin",
                 "latitude":52.52437,
                 "longitude":13.41053,
                 "elevation":74.0,
                 "feature_code":"PPLC",
                 "country_code":"DE",
                 "admin1_id":2950157,
                 "admin3_id":6547383,
                 "admin4_id":6547539,
                 "timezone":"Europe/Berlin",
                 "population":3426354,
                 "postcodes":[
                    "10967",
                    "13347"
                 ],
                 "country_id":2921044,
                 "country":"Germany",
                 "admin1":"Land Berlin",
                 "admin3":"Berlin, Stadt",
                 "admin4":"Berlin"
              }
            ], 
            "generationtime_ms":0.9409189
            }"""
    }

}