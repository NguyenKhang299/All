package com.tearas.myapplication.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.util.Objects

data class ItemMenu(
    @StringRes val text: Int,
    @DrawableRes val icon: Int,
    val option: Any,
    var isSelected: Boolean = false,
)