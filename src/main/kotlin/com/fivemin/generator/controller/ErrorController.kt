package com.fivemin.generator.controller

import com.fivemin.generator.model.ErrorCodeThrowable
import com.fivemin.generator.model.convertToStatus
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class ErrorCodeWrapper() {
    constructor(_status: HttpStatus, _message: String) : this() {
        status = _status
        message = _message
    }

    @Id
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    var status: HttpStatus? = null

    lateinit var message: String
}

@RestControllerAdvice
class ErrorController {
    @ExceptionHandler(ErrorCodeThrowable::class)
    fun handleException(ex: ErrorCodeThrowable): ResponseEntity<ErrorCodeWrapper> {
        val status = ex.error.convertToStatus()

        return ResponseEntity.status(status).body(ErrorCodeWrapper(status, ex.message ?: "Not provided"))
    }
}
