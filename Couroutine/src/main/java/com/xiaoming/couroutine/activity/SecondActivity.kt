package com.xiaoming.couroutine.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.xiaoming.couroutine.R
import com.xiaoming.couroutine.base.BaseActivity
import com.xiaoming.couroutine.server.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.act_second.*
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL


class SecondActivity : BaseActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun getCustomerLayout(): Int {
        return R.layout.act_second
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = getViewModel(AppViewModel::class.java)

        val async1 = lifecycleScope.async {
            delay(2000)
            "async1"
        }
        val async2 = lifecycleScope.async {
            delay(3000)
            "async2"
        }
        lifecycleScope.launch {
            Log.e("lifecycleScope", "async值开始时间：${System.currentTimeMillis()}")
            Log.e("lifecycleScope", "async值：${async1.await()}******${async2.await()}")
            Log.e("lifecycleScope", "async值结束时间：${System.currentTimeMillis()}")
        }

        appViewModel.appVersionLiveData.observe(this, Observer {
            Log.e("http", "返回值：$it")
            if (it?.isSuccess() == true) {
                tv_content.text = it.message
            }
        })

        btn_send_request.setOnClickListener {
//            lifecycleScope.launch {
//                val bitmap = loadImage(Constants.URL_IMG)
//                imageView.setImageBitmap(bitmap)
//            }

            requestAppVersion()
        }

    }

    private fun requestAppVersion() {
        appViewModel.getAppVersion(21041001)
    }

    /**
     * 加载图片
     * @param url String
     * @return (android.graphics.Bitmap..android.graphics.Bitmap?)
     */
    private suspend fun loadImage(url: String) = withContext(Dispatchers.IO) {
        val httpURLConnection = (URL(url).openConnection() as HttpURLConnection)
        httpURLConnection.apply {
            requestMethod = "GET"
            connectTimeout = 6000
        }.connect()
        val inputStream = httpURLConnection.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        bitmap
    }

}
