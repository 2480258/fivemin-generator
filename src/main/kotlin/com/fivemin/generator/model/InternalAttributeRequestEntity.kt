package com.fivemin.generator.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class InternalAttributeRequestEntity() {
    @Id
    @GeneratedValue
    open var id: Long? = null

    lateinit var name: String
    lateinit var queryStr: String
    lateinit var parseMode: String

    lateinit var html: String

    constructor(_name: String, _queryStr: String, _parseMode: String, _html: String) : this() {
        name = _name
        queryStr = _queryStr
        parseMode = _parseMode

        html = _html
    }
}
