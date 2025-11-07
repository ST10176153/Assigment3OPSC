package com.example.assignment3opsc

// app/src/test/java/.../MovieTests.kt
class MovieTests {
    @Test fun poster_is_nonEmpty_when_available() {
        val m = Movie("Title","2024","tt123","movie","url")
        assertTrue(m.poster.isNotBlank())
    }
}
