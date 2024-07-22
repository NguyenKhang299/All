package com.tearas.myapplication.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TabItem(
    val id: Int,
    @StringRes val name: Int,
    @DrawableRes val icon: Int
)