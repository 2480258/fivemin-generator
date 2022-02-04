package com.fivemin.generator.model

import com.fivemin.generator.service.TagFlag
import java.util.EnumSet
import javax.persistence.* // ktlint-disable no-wildcard-imports

@Entity
class TagResultEntity() {
    lateinit var name: String
    lateinit var value: String
    lateinit var flag: EnumSet<TagFlag>
    @Id
    @GeneratedValue
    var id: Long? = null

    constructor(_name: String, _value: String, _flag: EnumSet<TagFlag>) : this() {
        name = _name
        value = _value
        flag = _flag
    }
}

@Entity
class TagRequestEntity() {
    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var name: String
    lateinit var url: String
    lateinit var tagRegex: String

    constructor(_name: String, _url: String, _tagRegex: String) : this() {
        name = _name
        url = _url
        tagRegex = _tagRegex
    }
}
