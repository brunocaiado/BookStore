package pt.brunocaiado.datalibrary.data

import pt.brunocaiado.datalibrary.data.model.ErrorModel
import retrofit2.Response

open class ApiPerformer<T : Any> {

    suspend fun performSafeCall(call: suspend () -> Response<T>): ApiResult<T> {
        return try {
            getResult(call())
        } catch (ex: Exception) {
            ApiResult.Error(
                ErrorModel(
                    errorCode = "",
                    errorMessage = ex.message
                )
            )
        }

    }

    private fun getResult(response: Response<T>): ApiResult<T> {

        if(response.isSuccessful){

            response.body()?.let { body ->
                return ApiResult.Success(body)
            }

        }



        return ApiResult.Error(
            ErrorModel(
                errorCode = response.code().toString(),
                errorMessage = response.errorBody()?.string()
            )
        )

    }

}