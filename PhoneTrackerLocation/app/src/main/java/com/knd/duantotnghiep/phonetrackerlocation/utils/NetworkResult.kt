package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import com.knd.duantotnghiep.phonetrackerlocation.models.MessageResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import retrofit2.Response
import java.net.SocketTimeoutException

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : NetworkResult<T>(data)
    class Error<T>(message: String?) : NetworkResult<T>(null, message)
    class Loading<T> : NetworkResult<T>()
}

fun <T> ViewModel.handleApiCall(
    liveData: MutableLiveData<NetworkResult<T>>,
    apiCall: suspend  () -> Response<T>,
    saveData: ((String) -> Unit)? = null
) {
    launch {
        liveData.postValue(NetworkResult.Loading())
        try {
            val response = apiCall.invoke()
            liveData.handleApiResponse(response, saveData)
        } catch (e: Exception) {
            liveData.handleApiError(e)
        }
    }
}

fun <T> MutableLiveData<NetworkResult<T>>.handleApiError(exception: Exception) {
    when (exception) {
        is SocketTimeoutException -> postValue(NetworkResult.Error("Request timed out. Please check your internet connection and try again."))
        is ApiException -> postValue(NetworkResult.Error("An error occurred while processing your request. Please try again later."))
        else -> postValue(NetworkResult.Error(exception.localizedMessage))
    }
}//"An unexpected error occurred. Please try again later."

fun <T> MutableLiveData<NetworkResult<T>>.handleApiResponse(
    response: Response<T>,
    saveData: ((String) -> Unit)? = null
) {

    if (response.isSuccessful && response.body() != null) {
        val data = response.body()!!
        response.headers()["Authorization"]?.let { saveData?.invoke(it) }
        postValue(NetworkResult.Success(data))
    } else if (response.errorBody() != null) {
        var errorMessage = response.message() //get error theo status
        var errorBody = response.errorBody()!!.string()
        val messageResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
        postValue(NetworkResult.Error(messageResponse.message))
    } else {
        postValue(NetworkResult.Error("Something Went Wrong"))
    }
}
