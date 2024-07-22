package com.knd.duantotnghiep.testsocket.ui.chat_message.record

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class WaveformView(context: Context?, attrs: AttributeSet?) : View(context, attrs), IWaveformView {
    private var paint = Paint().apply { color = Color.RED }
    private var amplitudes = ArrayList<Float>()
    private var spikes = ArrayList<RectF>()
    private var rW = 9f
    private var d = 6f
    private var radius = 6f
    private var vW = 0f
    private var vH = 0f
    private var maxSpikes = 0
    var color = Color.BLACK


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val viewHeightPixels = height
        val viewWidthPixels = width
        vW = viewWidthPixels.toFloat()
        vH = viewHeightPixels.toFloat()
        maxSpikes = (vW / (rW + d)).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        rx	float: Bán kính x của hình bầu dục dùng để làm tròn các góc
//        ry	float: Bán kính y của hình bầu dục dùng để làm tròn các góc
        paint.color = color
        spikes.forEach {
            canvas.drawRoundRect(it, radius, radius, paint)
        }
    }

//        left	    float: Tọa độ X của cạnh trái hình chữ nhật
//        top	    float: Tọa độ Y của đỉnh hình chữ nhật
//        right  	float: Tọa độ X của cạnh phải của hình chữ nhật
//        bottom	float: Tọa độ Y của đáy hình chữ nhật
    override fun addAmplitude(amp: Int) {
        val norm = (amp.toInt() / 7).coerceAtMost(vH.toInt()).toFloat()

        amplitudes.add(norm)

        spikes.clear()
        val amps = amplitudes.takeLast(maxSpikes)
        for (i in amps.indices) {
            val left = i * (rW + d)
            val top = vH / 2 - amps[i] / 2
            val right = left + rW
            val bottom = top + amps[i]
            spikes.add(RectF(left, top, right, bottom))
        }
        invalidate()
    }

    override fun clear() {
        amplitudes.clear()
        spikes.clear()
        invalidate()
    }
}