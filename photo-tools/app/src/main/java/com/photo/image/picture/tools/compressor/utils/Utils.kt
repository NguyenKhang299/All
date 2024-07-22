package com.photo.image.picture.tools.compressor.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.photo.image.picture.tools.compressor.action.CompressionQuantity
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.domain.model.Resolution
import com.makeramen.roundedimageview.RoundedImageView
import com.photo.image.picture.tools.compressor.persentation.main.MainActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
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
            Glide.with(view.context).load("file:///$path").into(view)
        }

        fun loadImagePis(view: RoundedImageView, path: String) {
            Picasso.get().load("file:///$path")
                .memoryPolicy(MemoryPolicy.NO_STORE).fit().into(view);
        }

        fun Resolution.getResolutionBySize(
            compressionQuantity: CompressionQuantity,
            image: Image? = null
        ): Resolution {
            if (compressionQuantity !is CompressionQuantity.Custom) {
                val resize = when (compressionQuantity) {
                    is CompressionQuantity.VerySmallSize -> 0.15f
                    is CompressionQuantity.SmallSize -> 0.25f
                    is CompressionQuantity.MediumSize -> 0.5f
                    is CompressionQuantity.LagerQuality -> 0.75f
                    is CompressionQuantity.BestQuality -> 1f
                    is CompressionQuantity.Custom -> 0f
                }
                return Resolution(
                    (rW * resize).toInt(),
                    (rH * resize).toInt()
                )
            }
            if (image == null) throw NullPointerException()
            val percentage = ((compressionQuantity.size / image.size * 1.0f) * 100).toInt()
            return Resolution(
                rW * percentage / 100,
                rH * percentage / 100
            )
        }

        fun Context.fileToUri(file: File): Uri = FileProvider.getUriForFile(this,Const.authorities,file)
        fun Context.shareMultiple(imageFilePaths: List<Uri>) {
            val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
            shareIntent.type = "image/*"
            shareIntent .putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(imageFilePaths))
            shareIntent.flags=Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(Intent.createChooser(shareIntent, "Share:"))
        }
        fun Context.startToMain(){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}