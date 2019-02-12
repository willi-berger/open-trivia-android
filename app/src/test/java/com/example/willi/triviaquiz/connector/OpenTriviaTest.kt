package com.example.willi.triviaquiz.connector

import org.junit.Test

import org.junit.Assert.*

class OpenTriviaTest {

    @Test
    fun getCategories() {
        val openTrivia: OpenTrivia = OpenTrivia()
        val categories = openTrivia.getCategories()

        assertNotNull(categories)
    }
}