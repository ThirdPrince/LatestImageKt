package com.example.latestimagekt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.labo.kaji.relativepopupwindow.RelativePopupWindow


class MainActivity : AppCompatActivity() {



    companion object{
        const val TAG = "MainActivity"
    }

    var permissions = Manifest.permission.READ_EXTERNAL_STORAGE
    var permissionArray = arrayOf(permissions)
    var btn: Button? = null
    var editText: EditText? = null
    var  currentImage: String? = null
    var imageView: ImageView? = null

    lateinit var viewModel: LatestImagePicker

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        ).get(LatestImagePicker::class.java)
        val checkPermission = this?.let { ActivityCompat.checkSelfPermission(it, permissions) }
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            //执行到这里说明用户已经申请了权限直接加载数据就可以
            //  viewModel?.getLatestImage(null)
        } else {
            requestPermissions(permissionArray, 0)
        }
        btn = findViewById(R.id.photo_btn)
        imageView = findViewById(R.id.image)
        editText = findViewById(R.id.et_view)
        btn?.setOnClickListener {
            showInput(editText!!)
            viewModel?.getLatestImage(null)
            if(!TextUtils.isEmpty(currentImage)){
                LatestImagePop(btn!!.context, currentImage!!).let {
                    Glide.with(this).load(currentImage).into(imageView)
                    it.showPop(btn!!)
                }

            }else {
                viewModel?.getLatestImage(null)
            }
        }
        viewModel.lvMediaData.observe(this, Observer { data ->
            currentImage = data[0]?.path!!
            LatestImagePop(btn!!.context, currentImage!!).let {
                Glide.with(this).load(currentImage).into(imageView)
                it.showPop(btn!!)
            }

        })
        viewModel.lvDataChanged.observe(this, Observer {
            viewModel.getLatestImage(null)
        })


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }

    }

    fun showInput(et: EditText) {
        et.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }


}