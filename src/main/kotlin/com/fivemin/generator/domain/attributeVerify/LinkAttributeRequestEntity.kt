package com.fivemin.generator.domain.attributeVerify

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class LinkAttributeRequestEntity() {
    constructor(_name: String, _queryStr: String, _hostUri: String, _html: String, _uriRegex: String?) : this() {
        name = _name
        queryStr = _queryStr
        hostUri = _hostUri
        html = _html
        uriRegex = _uriRegex
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var name: String
    lateinit var queryStr: String
    lateinit var hostUri: String

    var uriRegex: String? = null

    lateinit var html: String
}
