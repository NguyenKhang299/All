package com.photo.imagecompressor.tools.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.photo.imagecompressor.tools.presentation.main.MainActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.photo.imagecompressor.tools.action.CompressionQuantity
import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.domain.model.Resolution_
import java.io.File

class Utils {
    companion object {
        fun getAppVersion(context: Context): String {
            return try {
                val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                packageInfo.versionName // Lấy tên phiên bản
            } catch (e: PackageManager.NameNotFoundException) {
                "N/A" // Trường hợp không tìm thấy phiên bản
            }
        }

        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()
        }

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun screenHeight(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }

        fun screenWidth(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }

        fun isPackageInstalled(context: Context, packageName: String): Boolean {
            return try {
                context.packageManager.getApplicationInfo(packageName, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                false
            }
        }

        fun loadImage(view: RoundedImageView, path: String) {
            Glide.with(view.context).load("$path").into(view)
        }

        fun loadImagePis(view: RoundedImageView, path: String) {
            Picasso.get().load("file:///$path")
                .memoryPolicy(MemoryPolicy.NO_STORE).fit().into(view);
        }

        fun Resolution_.getResolutionBySize(
            compressionQuantity: CompressionQuantity,
            image: Image_? = null
        ): Resolution_ {
                 val resize = when (compressionQuantity) {
                    is CompressionQuantity.VerySmallSize -> 0.2f
                    is CompressionQuantity.SmallSize -> 0.4f
                    is CompressionQuantity.MediumSize -> 0.6f
                    is CompressionQuantity.LagerQuality -> 0.8f
                }
                Log.d("dsodopsdk", resize.toString())
                val rW = if ((rW * resize).toInt() != 0) (rW * resize).toInt() else 1
                val rH = if ((rH * resize).toInt() != 0) (rH * resize).toInt() else 1
                return Resolution_(rW, rH)

        }

        fun Context.fileToUri(file: File): Uri = FileProvider.getUriForFile(
            this,
            Const.authorities, file
        )

        fun Context.shareMultiple(imageFilePaths: List<Uri>) {
            val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
            shareIntent.type = "image/*"
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(imageFilePaths))
            shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            startActivity(Intent.createChooser(shareIntent, "Share:"))
        }

        fun FragmentActivity.startToMain() {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}