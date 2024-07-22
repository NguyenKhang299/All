package com.knd.duantotnghiep.testsocket.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.knd.duantotnghiep.testsocket.response.MessageResponse
import kotlinx.coroutines.launch
import retrofit2.Response


sealed class NetworkResult<O>(val result: O? = null, val error: String? = null) {
    class OnSuccess<O>(result: O?) : NetworkResult<O>(result)
    class OnFailure<O>(error: String?) : NetworkResult<O>(error = error)
    class IsLoading<O>() : NetworkResult<O>()
}

fun <T> ViewModel.handleCallAPI(
    liveData: MutableLiveData<NetworkResult<T>>,
    apiCall: suspend () -> Response<T>,
    saveData: ((String) -> Unit)? = null
) {
    viewModelScope.launch {
        liveData.postValue(NetworkResult.IsLoading())
        try {
            val response = apiCall.invoke()
            liveData.handleApiResponse(response, saveData)
        } catch (e: Exception) {
            liveData.postValue(NetworkResult.OnFailure(e.localizedMessage))
        }
    }
}


fun <O> MutableLiveData<NetworkResult<O>>.handleApiResponse(
    response: Response<O>,
    saveData: ((String) -> Unit)? = null
) {
    if (response.isSuccessful && response.body() != null) {
        val data = response.body()!!
        response.headers()["Authorization"]?.let { saveData?.invoke(it) }
        postValue(NetworkResult.OnSuccess(data))
    } else if (response.errorBody() != null) {
        var errorMessage = response.message() //get error theo status
        response.errorBody()?.let {
            val messageResponse = Gson().fromJson(it.string(), MessageResponse::class.java)
            postValue(NetworkResult.OnFailure(messageResponse.message))
        } ?: postValue(NetworkResult.OnFailure("Unknown error"))
    } else {
        postValue(NetworkResult.OnFailure("Something Went Wrong"))
    }
}
