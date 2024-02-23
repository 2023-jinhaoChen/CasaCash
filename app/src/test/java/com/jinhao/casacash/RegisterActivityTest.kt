package com.jinhao.casacash

import org.junit.Test
import org.junit.Assert.*

class RegisterActivityTest {
    @Test
    fun testIsValidPassword() {
        val passwordValidator = PasswordValidator()
        assertFalse(passwordValidator.isValidPassword("12345Aa")) // 5digits
        assertTrue(passwordValidator.isValidPassword("123456Aa")) // 6digits
        assertTrue(passwordValidator.isValidPassword("1234567Aa")) // 7digits

        assertFalse(passwordValidator.isValidPassword("SHORT1234567")) // No lowercase
        assertFalse(passwordValidator.isValidPassword("nopassword123456")) // No uppercase
        assertFalse(passwordValidator.isValidPassword("OnlyUpperCase")) // Only letters
        assertTrue(passwordValidator.isValidPassword("Only123456")) // 1uppercase + some lowercase + 6digits
    }
}