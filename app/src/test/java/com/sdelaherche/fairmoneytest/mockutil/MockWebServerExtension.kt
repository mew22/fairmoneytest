package com.sdelaherche.fairmoneytest.mockutil

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import java.io.InputStreamReader

fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
    enqueue(
        MockResponse()
            .setResponseCode(code)
            .setBody(MockResponseFileReader(fileName).content)
    )
}

class MockResponseFileReader(fileName: String) {

    val content =
        InputStreamReader(
            this.javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
        ).use {
            it.readText()
        }
}
