package com.fivemin.generator.presentation

import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.HttpErrorCode
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

internal class ErrorControllerTest {

    val errorController = ErrorController()

    @Test
    fun handleException() {
        val result = errorController.handleException(ErrorCodeThrowable(HttpErrorCode.BAD_REQUEST, "reason"))

        assertEquals(result.statusCodeValue, 400)
        assertEquals(result.body!!.message, "reason")
    }
}
