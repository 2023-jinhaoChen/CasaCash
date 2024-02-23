package com.jinhao.casacash

class PasswordValidator {
    fun isValidPassword(password: String): Boolean{
        if (password.length < 8) return false
        if (password.count { it.isDigit() } < 6) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
        return true
    }
}