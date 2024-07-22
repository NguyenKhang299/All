package com.knd.duantotnghiep.phonetrackerlocation.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knd.duantotnghiep.phonetrackerlocation.models.MessageResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.respository.UploadRepository
import com.knd.duantotnghiep.phonetrackerlocation.respository.UserRepository
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.handleApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _onUpdateProfile = MutableLiveData<NetworkResult<MessageResponse>>()
    val onUpdateProfile: LiveData<NetworkResult<MessageResponse>> = _onUpdateProfile

    private val _getProfileUser = MutableLiveData<NetworkResult<UserRequest>>()
    val getProfileUser: LiveData<NetworkResult<UserRequest>> = _getProfileUser

    val txtName = MutableLiveData<String>()
    val txtAddress = MutableLiveData<String>()
    val avatar = MutableLiveData<String>()
    val isEnableEdit = MutableLiveData<Boolean>()
    fun handel(action: ProfileAction) {
        when (action) {
            is ProfileAction.UpdateProfile -> handelUpdateProfile(action.rqBd)
            is ProfileAction.DisableEditProfile -> handelDisableEditProfile()
            is ProfileAction.EnableEditProfile -> handelEnableEditProfile()
            else -> {}
        }
    }

    private fun handelEnableEditProfile() {
        isEnableEdit.postValue(true)
    }

    private fun handelDisableEditProfile() {
        isEnableEdit.postValue(false)
    }

    private fun handelUpdateProfile(rqBd: MultipartBody.Part) {
        handleApiCall(
            _onUpdateProfile,
            { userRepository.updateProfile(txtName.value!!, txtAddress.value!!, rqBd) })
    }
}