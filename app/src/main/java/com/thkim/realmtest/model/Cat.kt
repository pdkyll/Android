package com.thkim.realmtest.model

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects

/*
 * Created by Thkim on 12/5/20
 */
open class Cat : RealmObject() {
    var name: String? = null

    // You can define inverse relationships
    @LinkingObjects("cats")
    val owners: RealmResults<Person>? = null
}