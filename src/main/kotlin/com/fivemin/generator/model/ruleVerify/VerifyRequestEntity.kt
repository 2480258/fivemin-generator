package com.fivemin.generator.model.ruleVerify

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class VerifyRequestEntity {

    @Id
    @GeneratedValue
    open var id: Long? = null

    lateinit var json: String

    constructor() {

    }

    constructor(_json: String) : this() {
        json = _json
    }
}