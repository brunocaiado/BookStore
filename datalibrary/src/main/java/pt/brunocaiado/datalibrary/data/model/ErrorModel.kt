package pt.brunocaiado.datalibrary.data.model

data class ErrorModel(
    val errorCode : String,
    val errorMessage : String?
): Throwable()

