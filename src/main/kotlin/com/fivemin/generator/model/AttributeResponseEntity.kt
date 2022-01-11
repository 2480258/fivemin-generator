package com.fivemin.generator.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class AttributeResponseEntity() {
    @javax.persistence.Transient
    private val SINGLE_CONTENT: String = "single"

    @javax.persistence.Transient
    private val MULTIPLE_CONTENT: String = "multiple"

    @Id
    @GeneratedValue
    open var id: Long? = null

    lateinit var mode: String
    lateinit var name: String
    lateinit var parseResult: List<String>

    constructor(_name: String, _parsedResult: List<String>) : this() {
        mode = MULTIPLE_CONTENT
        name = _name
        parseResult = _parsedResult
    }

    constructor(_name: String, _parsedResult: String) : this() {
        mode = SINGLE_CONTENT
        name = _name
        parseResult = listOf(_parsedResult)
    }
}