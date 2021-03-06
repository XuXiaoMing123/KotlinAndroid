package com.xiaoming.couroutine.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 *
 * @Description:
 * @Author:         hayden
 * @CreateDate:     2021/4/26 11:45
 */
open class BaseViewModel : ViewModel() {

    protected fun <T> requestData(
        onBlock: suspend () -> T,
        onSucceed: suspend (T) -> Unit,
        onFailed: suspend (throwable: Throwable) -> Unit = {}
    ) {
        //默认是在主线程中
        viewModelScope.launch {
            runCatching {
                onBlock()
            }.onSuccess {
                onSucceed(it)
            }.onFailure {
                it.printStackTrace()
                onFailed(it)
            }
        }
    }
}