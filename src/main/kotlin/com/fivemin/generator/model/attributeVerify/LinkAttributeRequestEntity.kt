package com.fivemin.generator.model.attributeVerify

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

    var name: String? = null
    var queryStr: String? = null
    var hostUri: String? = null

    var uriRegex: String? = null

    var html: String? = null
}
