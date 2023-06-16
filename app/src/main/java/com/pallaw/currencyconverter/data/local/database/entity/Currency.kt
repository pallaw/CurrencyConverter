package com.pallaw.currencyconverter.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}