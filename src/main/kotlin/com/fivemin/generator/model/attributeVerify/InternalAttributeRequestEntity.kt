package com.fivemin.generator.model.attributeVerify

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

    constructor(name: String, queryStr: String, parseMode: String, html: String) : this() {
        this.name = name
        this.queryStr = queryStr
        this.parseMode = parseMode

        this.html = html
    }
}
