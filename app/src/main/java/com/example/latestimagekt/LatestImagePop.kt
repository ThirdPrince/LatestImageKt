package com.example.latestimagekt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.circularreveal.CircularRevealCompat
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.labo.kaji.relativepopupwindow.RelativePopupWindow
import kotlin.math.hypot
import kotlin.math.max

/**
 * 最近你要发送的图片 pop
 */
class LatestImagePop(context: Context, imagePath:String) : RelativePopupWindow(context) {

    private var imageView :ImageView
    private var vertPos:Int
    private var  horizPos:Int
    private var fitInScreen :Boolean

    init {
        @SuppressLint("InflateParams")
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_photo_pop, null)
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        imageView = contentView.findViewById(R.id.photo_img)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Disable default animation for circular reveal
        animationStyle = 0
        width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            90f,
            context.resources.displayMetrics
        ).toInt()
        height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            135f,
            context.resources.displayMetrics
        ).toInt()
         vertPos = RelativePopupWindow.VerticalPosition.ABOVE
         horizPos = RelativePopupWindow.HorizontalPosition.CENTER
         fitInScreen = true
        Glide.with(context).load(imagePath).into(imageView)

    }

     fun showPop(anchor: View?) {
         showOnAnchor(anchor!!, vertPos, horizPos, fitInScreen)
    }

}
