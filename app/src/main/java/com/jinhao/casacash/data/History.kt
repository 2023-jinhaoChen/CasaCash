package com.jinhao.casacash.data

data class History(
    val id : Int,
    val family : Family,
    val month : String,
    val startBudget : Double,
    val remainBudget : Double
)