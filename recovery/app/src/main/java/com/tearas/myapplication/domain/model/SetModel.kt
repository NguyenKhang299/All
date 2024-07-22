package com.tearas.myapplication.domain.model

import java.util.UUID

data class SetModel(
    val id: String = UUID.randomUUID().toString(),
    val listMedia: List<MediaModel>
)