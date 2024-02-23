package com.jinhao.casacash

import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class SpendingListActivityTest {

    @Test
    fun testParseData() {
        val dateparser = DateParser()

        // Test with a valid date string
        val validDateString = "2022-02-21"
        val validDate = dateparser.parseDate(validDateString)
        val expectedValidDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(validDateString)
        assertEquals(expectedValidDate, validDate)

        val validDateString2 = "2024-09-10"
        val validDate2 = dateparser.parseDate(validDateString2)
        val expectedValidDate2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(validDateString2)
        assertEquals(expectedValidDate2, validDate2)

        val validDateString3 = "2023-12-02"
        val validDate3 = dateparser.parseDate(validDateString3)
        val expectedValidDate3 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(validDateString3)
        assertEquals(expectedValidDate3, validDate3)

    }
}