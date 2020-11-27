package com.example.latestimagekt

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 遍历本地图片
 */
@Parcelize
class ImageItem( var name:String ?= null , var path:String ?= null, var size:Long =0, var width:Int = 0, var height:Int = 0, var mineType:String ?= null, var addTime:Long = 0) : Parcelable{


}