package com.example.qstreak.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    retrofit: Retrofit,
    call: suspend () -> T
): ApiResult<T> {
    return withContext(dispatcher) {
        try {
            ApiResult.Success(call.invoke())
        } catch (throwable: Throwable) {
            var errors: String? = null
            var code: Int? = null
            if (throwable is HttpException) {
                val converter: Converter<ResponseBody, ApiError> =
                    retrofit.responseBodyConverter(
                        ApiError::class.java,
                        arrayOfNulls<Annotation>(0)
                    )
                throwable.response()?.errorBody()?.let {
                    errors = converter.convert(it)?.getErrorString()
                }
                code = throwable.code()
            }
            ApiResult.Error(code, throwable.message, errors)
        }
    }
}
