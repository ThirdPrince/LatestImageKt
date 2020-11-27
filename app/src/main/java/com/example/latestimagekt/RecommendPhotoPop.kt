package com.example.latestimagekt

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import java.io.File


/**
 * 弹出框
 * @author dhl
 */
open class RecommendPhotoPop() : PopupWindow() {

    val tag = "RecommendPhotoPop"

    /**
     *
     */
    private var iv: ImageView? = null

    /**
     *
     */
    private var context: Context? = null

    private var image: String? = null


    constructor(context: Context?, image: String) : this() {
        this.context = context
        this.image = image
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_photo_pop, null, false)
        iv = view.findViewById<View>(R.id.photo_img) as ImageView
        contentView = view
        width = dip2px(90)
        height = dip2px(135)
        isFocusable = true
        setBackgroundDrawable(BitmapDrawable())
        // 设置点击其他地方 就消失 (只设置这个，没有效果)
        isOutsideTouchable = true

    }

    fun setImgPath(path: String?) {
        iv!!.setOnClickListener { }
        Glide.with(context!!).load(Uri.fromFile(File(path))).centerCrop()
            .into(iv!!)
    }

    /**
     * 显示图片提示
     * @param context
     * @param view
     */
    fun recommendPhoto(
        activity: Activity?,
        view: View
    ): RecommendPhotoPop? {
        val recommendPhotoPop = RecommendPhotoPop(context, image!!)
        recommendPhotoPop.setImgPath(image)
        val location = IntArray(2)
        val x: Int = getScreenWidth(context!!) - dip2px(92)

       view.viewTreeObserver.addOnGlobalLayoutListener(
            ViewTreeObserver.OnGlobalLayoutListener
            //当键盘弹出隐藏的时候会 调用此方法。
            {
                val r = Rect()
                //获取当前界面可视部分
                activity?.getWindow()?.getDecorView()?.getWindowVisibleDisplayFrame(r)
                //获取屏幕的高度
                val screenHeight = getScreenHeight(context!!)
               //  val heightDifference = screenHeight - r.bottom
                val heightDifference = screenHeight - (r.bottom - r.top)
                val isKeyboardShowing = heightDifference > screenHeight / 3
                if (isKeyboardShowing ) {
                    view.getLocationOnScreen(location)
                    val y: Int = 0
                       // screenHeight / 3
                       // screenHeight-r.bottom-view.measuredHeight
                    Log.e(tag,"view height:${view.measuredHeight}")
                    Log.e(tag," r.top:${ r.top}")
                    Log.e(tag," r.bottom:${ r.bottom}")
                    Log.e(tag," screenHeight:${screenHeight}")
                    Log.e(tag," view location:${location[1]}")
                    Log.e(
                        tag,
                        " pop location()==${y} "
                    )
                    // recommendPhotoPop.showAsDropDown(view);
                    recommendPhotoPop.showAtLocation(view, Gravity.START, x, y)
                }
            })


        /* view.getLocationOnScreen(location)
         val y: Int =  800
         // location[1]+heightDifference

         Log.e(tag," getScreenHeight()=${getScreenHeight(context!!)} view.measuredHeight==${location[1] } ")
         // recommendPhotoPop.showAsDropDown(view);
         recommendPhotoPop.showAtLocation(view, Gravity.NO_GRAVITY, x, y)*/
        // iv?.setImageBitmap(BitmapFactory.decodeFile(image))*/
        // Glide.with(context).load(image).into(iv)
        return recommendPhotoPop
    }


    /** dip转换px  */
    fun dip2px(dip: Int): Int {
        val scale: Float =
            context?.resources?.displayMetrics!!.density
        return (dip * scale + 0.5f).toInt()
    }

    /** pxz转换dip  */
    fun px2dip(px: Int): Int {
        val scale = context!!.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }


}