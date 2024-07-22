package com.pranksound.fartsound.trollandjoke.funnyapp.model

import java.io.Serializable

class DataImage(
    var id: String,
    var name: String,
    var icon: String,
    var isSelected: Boolean = false
) :
    Serializable {

    override fun toString(): String {
        return "DataImage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}'
    }
}