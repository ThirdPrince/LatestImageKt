package com.example.latestimagekt

import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author dhl
 * ViewModel 查询最近保存的图片
 * 需要 application 实例，查询数据库
 */
class LatestImagePickerViewModel(application: Application) : BaseViewModel(application) {


    companion object {
        const val TAG = "LatestImagePicker"
    }

    private val imageType = arrayOf("image/png", "image/jpeg")

    /**
     * 判断图片的路径 是否截屏
     */
    private val screenShoot = arrayOf(
        "screenshot", "screen_shot", "screen-sh", "screen shot",
        "screencapture", "screen_capture ", "screen-capture", "screen capture",
        "screencap", "screen_cap", "screen-cap", "screen cap"
    )


    /**
     * 至少需要高宽，时间
     */
    private val imageProjection = arrayOf( //查询图片需要的数据列
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,  //图片的显示名称  aaa.jpg
        MediaStore.Images.Media.DATA,  //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
        MediaStore.Images.Media.SIZE,  //图片的大小，long型  132492
        MediaStore.Images.Media.WIDTH,  //图片的宽度，int型  1920
        MediaStore.Images.Media.HEIGHT,  //图片的高度，int型  1080
        MediaStore.Images.Media.MIME_TYPE,  //图片的类型     image/jpeg
        MediaStore.Images.Media.DATE_ADDED //图片被添加的时间，long型  1450518608
    )

    private val _lvImageData = MutableLiveData<ImageItem>()

    val lvMediaData: LiveData<ImageItem>
        get() = _lvImageData


    private val _lvDataChanged = MutableLiveData<Boolean>()

    private val _imageIsScreenShotData = MutableLiveData<Boolean>()

    val imageIsScreenShotData :LiveData<Boolean>
       get() = _imageIsScreenShotData

    val lvDataChanged: LiveData<Boolean>
        get() = _lvDataChanged

    private var contentObserver: ContentObserver? = null

    private fun registerContentObserver() {
        if (contentObserver == null) {
            contentObserver = getApplication<Application>().contentResolver.registerObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ) {
                _lvDataChanged.value = true
            }
        }
    }


    fun getLatestImage(bucketId: String? = null) {
        launchDataLoad {
            val imageItems = queryImages(bucketId)
            _lvImageData.postValue(imageItems)
            registerContentObserver()
            val imagePath = imageItems.path!!.toLowerCase()
            screenShoot.forEach {
                if(imagePath!!.contains(it) && (System.currentTimeMillis()/1000-imageItems.addTime < 1)) {
                    _imageIsScreenShotData.postValue(true)
                    return@forEach
                }

            }
        }
    }

    /**
     * 只获取普通图片，不获取Gif
     */
    @WorkerThread
    suspend fun queryImages(bucketId: String?):ImageItem {
        val imageItem = ImageItem()
        withContext(Dispatchers.IO) {
            val uri = MediaStore.Files.getContentUri("external")
            val sortOrder = MediaStore.Images.Media._ID + " DESC limit 1 "
            var selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                    + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) +
                    " AND " + MediaStore.Images.Media.MIME_TYPE + "=?" +
                    " or " + MediaStore.Images.Media.MIME_TYPE + "=?"

            val data = getApplication<Application>().contentResolver.query(
                uri,
                imageProjection,
                selection,
                imageType,
                sortOrder
            )

            if (data!!.moveToFirst()) {
                //查询数据
                val imageId: String =
                    data.getString(data.getColumnIndexOrThrow(imageProjection[0]))
                val imagePath: String =
                    data.getString(data.getColumnIndexOrThrow(imageProjection[1]))
                val imageSize: Long =
                    data.getLong(data.getColumnIndexOrThrow(imageProjection[2]))
                val imageWidth: Int =
                    data.getInt(data.getColumnIndexOrThrow(imageProjection[3]))
                val imageHeight: Int =
                    data.getInt(data.getColumnIndexOrThrow(imageProjection[4]))
                val imageMimeType: String =
                    data.getString(data.getColumnIndexOrThrow(imageProjection[5]))
                val imageAddTime: Long =
                    data.getLong(data.getColumnIndexOrThrow(imageProjection[6]))
                imageItem.path = imagePath
                imageItem.addTime = imageAddTime
            }

        }
        return imageItem
    }


    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

}



