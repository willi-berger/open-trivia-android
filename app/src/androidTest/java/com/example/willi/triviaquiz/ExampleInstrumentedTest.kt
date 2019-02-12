package com.example.willi.triviaquiz

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.willi.triviaquiz.connector.OpenTrivia

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.willi.triviaquiz", appContext.packageName)
    }

    @Test
    fun getCategories() {
        val openTrivia: OpenTrivia = OpenTrivia()
        val categories = openTrivia.getCategories()
        assertNotNull(categories)
        assertTrue(categories.isNotEmpty())
    }
}
