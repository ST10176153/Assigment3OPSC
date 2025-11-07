package com.example.assignment3opsc

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUiSmokeTest {

    @get:Rule
    val rule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginScreenRenders() {
        // Verify key views exist
        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonLogin)).check(matches(withText("Login")))
    }
}
