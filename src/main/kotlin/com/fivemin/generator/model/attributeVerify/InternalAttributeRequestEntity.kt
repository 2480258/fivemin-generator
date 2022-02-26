package com.fivemin.generator.model.attributeVerify

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class InternalAttributeRequestEntity() {
    @Id
    @GeneratedValue
    open var id: Long? = null

    var name: String? = null
    var queryStr: String? = null
    var parseMode: String? = null

    var html: String? = null

    constructor(name: String, queryStr: String, parseMode: String, html: String) : this() {
        this.name = name
        this.queryStr = queryStr
        this.parseMode = parseMode

        this.html = html
    }
}
