package com.sdelaherche.fairmoneytest.userdetail.domain.entity


data class Location(
    val street: Street, val city: City, val state: State, val country: Country,
    val timezone: TimeZone
)