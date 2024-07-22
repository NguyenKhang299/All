package com.knd.duantotnghiep.phonetrackerlocation.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseActivity
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ActivityProfileBinding
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.DialogSave
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.DialogSaveCallBack
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.PickImageOnly
 import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import com.knd.duantotnghiep.phonetrackerlocation.utils.DialogUtils.dismissDialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.DialogUtils.showDialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.FileHandel
import com.knd.duantotnghiep.phonetrackerlocation.utils.FileHandel.saveImage
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission.hasEnableGPS
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission.hasPermissionCamera
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>() {
    private val profileViewModel by viewModels<ProfileViewModel>()
    private lateinit var uriImgCamera: Uri
    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager
    private lateinit var dialogSave: DialogSave
    private lateinit var file: File
    private lateinit var context: ProfileActivity
    private lateinit var multipartBody: MultipartBody.Part
    private val pickVisualMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                setUpFileAvatar(uri)
            }
        }
    private val startCameraResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) setUpFileAvatar(uriImgCamera)
        }
    private val rqePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { rs ->
            if (rs) {
                startCamera()
            }
        }

    private fun setUpFileAvatar(uri: Uri) {
        binding.imgPick.setImageURI(uri)
        this.file = ImageView(this).let {
            it.setImageURI(uri)
            it.saveImage(context)!!
        }
        multipartBody = MultipartBody.Part.createFormData(
            Constants.KEY_AVATAR,
            file.name,
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
    }


    override fun getViewBinding(): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuEdit -> handelEvent(ProfileEvent.ClickMenuEdit)
            R.id.menuCancel -> handelEvent(ProfileEvent.ClickMenuCancel)
            R.id.menuSave -> handelEvent(ProfileEvent.ClickMenuSave)
            android.R.id.home -> handelEvent(ProfileEvent.ClickMenuHome)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setUpDialogSave() {
        dialogSave = DialogSave(object : DialogSaveCallBack {
            override fun onSaveClick() {
                profileViewModel.handel(ProfileAction.UpdateProfile(multipartBody))
            }
        })
        dialogSave.show(supportFragmentManager, "DialogSave")
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initView() {
        context = this
        with(binding) {
            setUpToolBar(toolBar, true,
                getDrawable(R.drawable.ic_back_24)?.apply { setTint(Color.BLACK) })

            imgPick.setOnClickListener {
                pickVisualMedia.launch(PickImageOnly())
            }
            imgCamera.setOnClickListener {
                if (!hasPermissionCamera()) {
                    rqePermission.launch(Permission.CAMERA)
                } else {
                    startCamera()
                }
            }
        }
    }

    override fun getMenu(): Int {
        return R.menu.menu_profile
    }

    private fun startCamera() {
        uriImgCamera = FileHandel.getUri(context, File(filesDir, "avatar.png"))
        startCameraResult.launch(uriImgCamera)
    }

    override fun initData() {
        val user = userPreferencesManager.getCurrentUser()!!
        //     Picasso.get().load(user.avatar).fit().into(binding.imgPick)

        with(profileViewModel) {
            if (user != null) {
                txtAddress.postValue(user.address)
                txtName.postValue(user.name)
                avatar.postValue(user.avatar)
            }
        }
        binding.lifecycleOwner = this
        binding.viewModel = profileViewModel

    }

    override fun initObserver() {
        with(profileViewModel) {
            onUpdateProfile.observe(context) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        dismissDialogLoading()
                        updateMenuVisibility(true, false, false)
                    }

                    is NetworkResult.Error -> {
                        dismissDialogLoading()
                        showMessage(result.message.toString())
                    }

                    is NetworkResult.Loading -> {
                        showDialogLoading()
                        if (context::dialogSave.isInitialized) dialogSave.dismiss()
                    }
                }
            }
        }
    }

    private fun handelEvent(event: ProfileEvent): Boolean {
        when (event) {
            is ProfileEvent.ClickMenuEdit -> {
                updateMenuVisibility(false, true, true)
                profileViewModel.handel(ProfileAction.EnableEditProfile)
            }

            is ProfileEvent.ClickMenuSave -> {
                updateMenuVisibility(true, false, false)
                profileViewModel.handel(ProfileAction.UpdateProfile(multipartBody))
            }

            is ProfileEvent.ClickMenuCancel -> {
                updateMenuVisibility(true, false, false)
                profileViewModel.handel(ProfileAction.DisableEditProfile)
            }

            is ProfileEvent.ClickMenuHome -> setUpDialogSave()
        }
        return true
    }

    private fun updateMenuVisibility(
        menuEdit: Boolean,
        menuCancel: Boolean,
        menuSave: Boolean
    ) {
        menu.apply {
            getItem(0).isVisible = menuEdit
            getItem(1).isVisible = menuCancel
            getItem(2).isVisible = menuSave
        }
    }
}
