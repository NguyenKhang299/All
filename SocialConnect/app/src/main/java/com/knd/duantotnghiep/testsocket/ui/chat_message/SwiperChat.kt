package com.knd.duantotnghiep.testsocket.ui.chat_message

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.knd.duantotnghiep.testsocket.R
import com.knd.duantotnghiep.testsocket.utils.BitmapUtils
import com.knd.duantotnghiep.testsocket.utils.vibrator
import kotlin.math.abs


interface SwiperListener {
    fun onSwiped(position: Int)
}

class SwiperChat(val context: Context, val listener: SwiperListener) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
    var typeSwipeDirs: Int = -1
    var slowTranslation = 0.5f
    var maxRadius = 50f
    var canvasMarginEnd = 100f
    var translationX = 0f
    private var position = -1
    var scaleFactor = 0.8f
    var bitmapDraw: Bitmap? = null
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {

    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        translationX = dX * slowTranslation

        if (viewHolder.adapterPosition == RecyclerView.NO_POSITION) return
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (translationX >= 0 && typeSwipeDirs == ItemTouchHelper.LEFT) {
                drawCircle(c, translationX, viewHolder, ItemTouchHelper.LEFT)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    translationX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            } else if (translationX <= 0 && typeSwipeDirs == ItemTouchHelper.RIGHT) {
                drawCircle(c, translationX, viewHolder, ItemTouchHelper.RIGHT)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    translationX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            return
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


    private fun drawCircle(c: Canvas, dX: Float, viewHolder: ViewHolder, typeSwipeDirs: Int) {
        val paint = Paint().apply {
            color = Color.BLACK
            isAntiAlias = true
        }
        val checkSwiper = abs(dX) >= canvasMarginEnd
        if (checkSwiper) {
            var radius = (abs(dX) - canvasMarginEnd) * scaleFactor
            radius = if (radius >= maxRadius) maxRadius else radius
            val bottom = viewHolder.itemView.bottom.toFloat()

            val swipedPositionCanvas = if (typeSwipeDirs == ItemTouchHelper.RIGHT)
                viewHolder.itemView.right.toFloat()
            else
                viewHolder.itemView.left.toFloat()

            var circleX = swipedPositionCanvas + dX


            val circleY = bottom - maxRadius
            val checkMarginCircle = (
                    if (typeSwipeDirs == ItemTouchHelper.RIGHT)
                        swipedPositionCanvas - circleX
                    else
                        swipedPositionCanvas + circleX
                    ) >= canvasMarginEnd

            circleX = if (checkMarginCircle) {
                if (abs(dX) >= maxRadius + canvasMarginEnd) handleSwiped(viewHolder)
                if (typeSwipeDirs == ItemTouchHelper.RIGHT)
                    swipedPositionCanvas - canvasMarginEnd
                else
                    swipedPositionCanvas + canvasMarginEnd
            } else circleX

            c.drawCircle(circleX, circleY, radius, paint)
            drawIconInCircle(c, circleX, circleY, radius)
        }
    }

    private fun drawIconInCircle(c: Canvas, circleX: Float, circleY: Float, radius: Float) {
        val iconX = circleX - (radius / 2)
        val iconY = circleY - (radius / 2)

        val size = abs(radius.toInt())
        bitmapDraw = BitmapUtils.getBitmap(context, R.drawable.ic_reply, size, size)
        if (bitmapDraw != null) {
            c.drawBitmap(bitmapDraw!!, iconX, iconY, Paint())
            bitmapDraw!!.recycle()
        }
    }

    private fun handleSwiped(viewHolder: ViewHolder) {
        if (position != viewHolder.adapterPosition) {
            position = viewHolder.adapterPosition
            if (position != -1) {
                listener.onSwiped(position)
                context.vibrator(100)
            }
        }
    }

    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        return 10f
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 15f
    }


}
