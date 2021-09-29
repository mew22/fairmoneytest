package com.sdelaherche.fairmoneytest.userdetail.data.mapper

import com.sdelaherche.fairmoneytest.common.data.model.LocationData
import com.sdelaherche.fairmoneytest.userdetail.data.remote.LocationRemoteData
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.City
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Country
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Location
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.State
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.Street
import com.sdelaherche.fairmoneytest.userdetail.domain.entity.TimeZone

fun LocationRemoteData.toLocal(): LocationData = LocationData(
    street, city, state, country, timezone
)

fun LocationData.toEntity(): Location = Location(
    street = Street(street),
    city = City(city),
    state = State(state),
    country = Country(country),
    timezone = TimeZone(timezone)
)
