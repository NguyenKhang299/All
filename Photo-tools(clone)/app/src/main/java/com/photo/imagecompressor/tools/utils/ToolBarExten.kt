package com.photo.imagecompressor.tools.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.photo.imagecompressor.tools.databinding.LayoutToolBarBinding

fun LayoutToolBarBinding.setIConNavigation(@DrawableRes draw: Int) {
    mToolbar.setNavigationIcon(draw)
}

fun LayoutToolBarBinding.clickIconNavigation(onClick: () -> Unit) {
    mToolbar.setNavigationOnClickListener {
        onClick()
    }
}

fun Toolbar.setMenuToolbar(@MenuRes id: Int, onClickMenu: (MenuItem) -> Boolean) {
    inflateMenu(id)

    setOnMenuItemClickListener { menuItem ->
        onClickMenu(menuItem)
    }
}

 fun LayoutToolBarBinding.setColorItemMenu(idItem: Int, color: Int) {
    mToolbar.menu!!.findItem(idItem)?.let { menuItem ->
        val colorStateList = ColorStateList.valueOf(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            menuItem.iconTintList = colorStateList
        }
        val spanString = SpannableString(menuItem.title)
        val colorSpan = ForegroundColorSpan(color)
        spanString.setSpan(
            colorSpan,
            0,
            spanString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        menuItem.title = spanString
    }
}

@SuppressLint("NewApi")
fun Toolbar.setColorItemMenu(idItem: Int, color: Int) {
    menu.findItem(idItem)?.let { menuItem ->
        val colorStateList = ColorStateList.valueOf(color)
        menuItem.iconTintList = colorStateList
        val spanString = SpannableString(menuItem.title)
        val colorSpan = ForegroundColorSpan(color)
        spanString.setSpan(
            colorSpan,
            0,
            spanString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        menuItem.title = spanString
    }
}

fun Toolbar.setIConNavigation(@DrawableRes draw: Int) {
    setNavigationIcon(draw)
}

fun Toolbar.clickIconNavigation(onClick: () -> Unit) {
    setNavigationOnClickListener {
        onClick()
    }
}

fun LayoutToolBarBinding.setMenuToolbar(@MenuRes menu: Int, onClickMenu: (MenuItem) -> Boolean) {
    mToolbar.inflateMenu(menu)
    mToolbar.setOnMenuItemClickListener { menuItem ->
        onClickMenu(menuItem)
    }
}