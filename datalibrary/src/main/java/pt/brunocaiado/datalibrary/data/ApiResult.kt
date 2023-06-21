package pt.brunocaiado.datalibrary.data

import pt.brunocaiado.datalibrary.data.model.ErrorModel

sealed class ApiResult<out T : Any?> {

    data class Success<out T : Any>(val data: T) : ApiResult<T>()

    data class Error(val error: ErrorModel) : ApiResult<Nothing>()

}
