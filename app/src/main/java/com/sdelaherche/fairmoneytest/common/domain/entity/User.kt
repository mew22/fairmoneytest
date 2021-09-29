package com.sdelaherche.fairmoneytest.common.domain.entity

import java.net.URI

data class User(
    val id: Id,
    val title: Title,
    val firstName: Name,
    val lastName: Name,
    val picture: URI
)
