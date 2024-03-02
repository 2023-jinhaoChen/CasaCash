package com.jinhao.casacash.data

import java.util.Date

data class Spending(
    val id : Int,
    val title : String,
    val amount : Double,
    val description : String,
    val date : Date,
    val image_uri : String,
    val userId: Int,
    val familyId: Int
)