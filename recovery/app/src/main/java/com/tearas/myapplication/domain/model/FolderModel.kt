package com.tearas.myapplication.domain.model

import java.io.Serializable
import java.util.UUID

data class FolderModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val path: String = ""
) : Serializable {

}