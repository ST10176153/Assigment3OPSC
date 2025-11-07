package com.example.assignment3opsc

// app/src/androidTest/java/.../LoginUiSmokeTest.kt
@RunWith(AndroidJUnit4::class)
class LoginUiSmokeTest {
    @get:Rule val rule = ActivityScenarioRule(LoginActivity::class.java)
    @Test fun has_email_password_fields() {
        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()))
    }
}
