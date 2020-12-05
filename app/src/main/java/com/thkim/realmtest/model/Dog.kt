package com.thkim.realmtest.model

import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.RealmClass

/*
 * Created by Thkim on 12/5/20
 */
@RealmClass
open class Dog : RealmModel {
    var name: String? = null

    // You can define inverse relationships
    @LinkingObjects("dog")
    val owners: RealmResults<Person>? = null
}