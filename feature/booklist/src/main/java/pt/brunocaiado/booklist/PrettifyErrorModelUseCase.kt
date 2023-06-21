package pt.brunocaiado.booklist

import pt.brunocaiado.datalibrary.data.model.ErrorModel
import pt.brunocaiado.booklist.ErrorUiModel
import javax.inject.Inject

class PrettifyErrorModelUseCase @Inject constructor(){

    fun getPrettifiedVersionOfErrorModel(errorModel: ErrorModel) : pt.brunocaiado.booklist.ErrorUiModel {
        // Get message from a better resource based on the errorModel.errorCode
        return pt.brunocaiado.booklist.ErrorUiModel(
            title = "Ocorreu um erro!",
            message = "Tente novamente mais tarde"
        )
    }

}