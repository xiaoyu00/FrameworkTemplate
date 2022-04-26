package com.framework.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Size
import java.io.*
import java.net.URL

object ImageUtils {

    fun getBitmapForUrl(url: String): Bitmap? {
        try {
            val url = URL(url)
            val ips: InputStream = url.openStream()
            val bitmap = BitmapFactory.decodeStream(ips)
            ips.close()
            return bitmap
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun getBitmapForPath(path: String): Bitmap {
        return BitmapFactory.decodeFile(path)
    }

    fun getImageByteArrayForPath(path: String): ByteArray {
        return bmpToByteArray(getBitmapForPath(path))
    }

    fun getImageByteArrayForUrl(url: String): ByteArray {
        getBitmapForUrl(url)?.let {
            return bmpToByteArray(it)
        }
        return byteArrayOf()
    }

    fun base64ToBitmap(base64String: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val bitmapArray: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean = true): ByteArray {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)//设置图片格式压缩相关
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result//输出
    }

    fun idCreateScaledBitmap(context: Context, id: Int, size: Size = Size(200, 200)): Bitmap {
        return Bitmap.createScaledBitmap(idToBitmap(context, id), size.width, size.height, true)
    }

    /**
     * R id 转bitmap
     */
    fun idToBitmap(context: Context, id: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, id)
    }

    fun saveBitmap(dir: String?, b: Bitmap): String? {
        val dataTake = System.currentTimeMillis()
        val jpegName = dir + File.separator + "picture_" + dataTake + ".jpg"
        return try {
            val fout = FileOutputStream(jpegName)
            val bos = BufferedOutputStream(fout)
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
            jpegName
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}