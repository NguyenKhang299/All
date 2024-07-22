package com.photo.image.picture.tools.compressor.persentation.view

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide.init
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.databinding.LayoutMyTabBinding
import com.photo.image.picture.tools.compressor.utils.XAnimationUtils

interface MyTabEvent {
    fun tabSelected(position: Int)
}

class MyTab : ConstraintLayout, MyTabEvent {

    private var _colorTabSelected: Int = context.getColor(R.color.bg_tab_selected)
    private var _colorTabUnselected: Int = context.getColor(R.color.bg_tab_unselected)
    private var _colorTextSelected: Int = context.getColor(R.color.text_selected)
    private var _colorTextUnselected: Int = context.getColor(R.color.text_unselected)
    private var _titleTab_1: String = "Tab 1"
    private var _titleTab_2: String = "Tab 2"
    private var _selected: Int = 0
    private lateinit var myTabEvent: MyTabEvent

    private var viewTabBinding: LayoutMyTabBinding =
        LayoutMyTabBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        this.addView(viewTabBinding.root)
        viewTabBinding.lifecycleOwner = viewTabBinding.lifecycleOwner
        viewTabBinding.tabEvent = this
    }

    private val animationListener = object : AnimationListener {
        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {

        }

        override fun onAnimationRepeat(animation: Animation?) {

        }

    }

    fun setOnListenTabSelected(myTabEvent: MyTabEvent) {
        this.myTabEvent = myTabEvent
    }

    override fun tabSelected(position: Int) {
        startAnimation(position)
    }

    private fun startAnimation(position: Int) {
        if (_selected != position) {
            when (position) {
                0 -> XAnimationUtils.prevTransitionView(
                    viewTabBinding.viewSelected,
                    animationListener
                )

                1 -> XAnimationUtils.nextTransitionView(
                    viewTabBinding.viewSelected,
                    animationListener
                )
            }
            viewTabBinding.tabSelected = position
            myTabEvent.tabSelected(position)
            _selected = position
        }
    }


    var colorTabSelected: Int = _colorTabSelected
        set(value) {
            field = value
            viewTabBinding.colorTabSelected = value
        }

    var colorTabUnselected: Int = _colorTabUnselected
        set(value) {
            field = value
            viewTabBinding.colorTabUnselected = value
        }

    var colorTextSelected: Int = _colorTextSelected
        set(value) {
            field = value
            viewTabBinding.colorTextSelected = value
        }

    var colorTextUnselected: Int = _colorTextUnselected
        set(value) {
            field = value
            viewTabBinding.colorTextUnselected = value
        }

    var titleTab_1: String = _titleTab_1
        set(value) {
            field = value
            viewTabBinding.titleTab1 = value
        }

    var titleTab_2: String = _titleTab_2
        set(value) {
            field = value
            viewTabBinding.titleTab2 = value
        }
    var selected: Int = 0
        get() = _selected
        set(value) {
            tabSelected(value)
            field = value
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {


        val a = context.obtainStyledAttributes(
            attrs, R.styleable.MyTab, defStyle, 0
        )

        _colorTabSelected = a.getColor(
            R.styleable.MyTab_color_tab_selected, _colorTabSelected
        )
        _colorTabUnselected = a.getColor(
            R.styleable.MyTab_color_tab_unselected, _colorTabUnselected
        )
        _colorTextSelected = a.getColor(
            R.styleable.MyTab_color_text_selected, _colorTextSelected
        )
        _colorTextUnselected = a.getColor(
            R.styleable.MyTab_color_text_unselected, _colorTextUnselected
        )
        _titleTab_1 = a.getString(
            R.styleable.MyTab_title_tab_2
        ).toString()
        _titleTab_2 = a.getString(
            R.styleable.MyTab_title_tab_1
        ).toString()
        colorTabSelected = _colorTabSelected
        colorTabUnselected = _colorTabUnselected
        colorTextSelected = _colorTextSelected
        colorTextUnselected = _colorTextUnselected
        titleTab_1 = _titleTab_1
        titleTab_2 = _titleTab_2
        selected = _selected
        a.recycle()
    }
}