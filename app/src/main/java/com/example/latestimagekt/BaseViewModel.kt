package com.example.latestimagekt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

/**
 * 获取最新图片的VM
 * @author dhl
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * 取消协程
     */
    private val viewModelJob = SupervisorJob()

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        t.printStackTrace()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob + exceptionHandler)

    private val _lvError = MutableLiveData<Exception>()

    open val lvError: LiveData<Exception>
        get() = _lvError

    fun launchDataLoad(block: suspend (scope: CoroutineScope) -> Unit): Job {
        return uiScope.launch {
            try {
                block(this)
            } catch (error: Exception) {
                handleException(error)
            } finally {
            }
        }
    }

    private fun handleException(error: Exception) {
        error.printStackTrace()
        if (error !is CancellationException) {
            _lvError.value = error
        }
    }

    public override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}