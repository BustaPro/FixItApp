package com.example.fixitapp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.fixitapp.activities.ServiceListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ServiceListActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(ServiceListActivity::class.java)

    @Test
    fun screenLoadsCorrectly() {
        onView(withId(R.id.btnAddService)).check(matches(isDisplayed()))
    }
}
