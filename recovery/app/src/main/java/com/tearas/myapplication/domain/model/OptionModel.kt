package com.tearas.myapplication.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OptionModel(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val type: OptionEnum
)