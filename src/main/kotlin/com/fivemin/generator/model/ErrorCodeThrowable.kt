package com.fivemin.generator.model

import org.springframework.http.HttpStatus
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

fun HttpErrorCode.convertToStatus() : HttpStatus {
    return when(this) {
        HttpErrorCode.BAD_REQUEST -> HttpStatus.BAD_REQUEST
        HttpErrorCode.NOT_FOUND -> HttpStatus.NOT_FOUND
        HttpErrorCode.INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
    }
}

enum class HttpErrorCode {
    BAD_REQUEST,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR
}

@Entity
class ErrorCodeThrowable : Exception {
    @Id
    @GeneratedValue
    open var id: Long? = null

    lateinit var error: HttpErrorCode

    constructor() : super()

    constructor(err: HttpErrorCode, reason: String) : super(reason) {
        error = err
    }
    constructor(err: HttpErrorCode, cause: Throwable) : super(cause) {
        error = err
    }
    constructor(err: HttpErrorCode, reason: String, cause: Throwable) : super(reason, cause) {
        error = err
    }
}
