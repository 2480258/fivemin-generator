package com.fivemin.generator.model.ruleVerify

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class VerifyResultEntity {

    @Id
    @GeneratedValue
    open var id: Long? = null

    lateinit var collectedErrorMessages: List<String>
    lateinit var notExecutedRules: List<String>

    constructor() {

    }

    constructor(_collectedErrorMessages: List<String>, _notExecutedRules: List<String>) : this() {
        collectedErrorMessages = _collectedErrorMessages
        notExecutedRules = _notExecutedRules
    }
}