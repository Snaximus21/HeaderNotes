 package com.rg.headernotes

import android.content.Context
import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test


 class ElapsedTimeIdlingResource(private val waitingTime: Long) : IdlingResource {
     private val startTime: Long
     private var resourceCallback: ResourceCallback? = null

     init {
         startTime = System.currentTimeMillis()
     }

     override fun getName(): String {
         return ElapsedTimeIdlingResource::class.java.name + ":" + waitingTime
     }

     override fun isIdleNow(): Boolean {
         val elapsed = System.currentTimeMillis() - startTime
         val idle = elapsed >= waitingTime
         if (idle) {
             resourceCallback!!.onTransitionToIdle()
         }
         return idle
     }

     override fun registerIdleTransitionCallback(
         resourceCallback: ResourceCallback
     ) {
         this.resourceCallback = resourceCallback
     }
 }

class ApplicationTest  {
    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val context = InstrumentationRegistry.getInstrumentation().context
    private val isSignIn = context.getSharedPreferences("viewPager2Start", Context.MODE_PRIVATE).getBoolean("Finished", false)

    @Test
    fun signInTest(){
        Log.e("ApplicationTest", "User signIn - $isSignIn")
        if(isSignIn){
            onView(withId(R.id.floatingActionButtonTasks)).perform(click())
        }else{
            onView(withId(R.id.nextTV)).check(
                matches(
                    allOf(
                        withText(R.string.next),
                        isDisplayed()
                    )
                )
            ).perform(click())
            Thread.sleep(500)
            onView(withId(R.id.finishTV)).check(
                matches(
                    allOf(
                        withText(R.string.finish),
                        isDisplayed()
                    )
                )
            ).perform(click())
            Thread.sleep(5000)
            onView(withId(R.id.floatingActionButtonTasks)).check(matches(withId(R.id.floatingActionButtonTasks)))
            onView(withId(R.id.floatingActionButtonTasks)).perform(click())
            Thread.sleep(1500)
            onView(withId(R.id.editTextTaskName)).perform(ViewActions.typeText("Espresso task"))
            Thread.sleep(500)
            onView(withId(R.id.editTextTitle)).perform(ViewActions.typeText("There are example of espresso test."))
            Thread.sleep(500)
            onView(withId(R.id.buttonApply)).perform(click())
            Thread.sleep(1500)
            onView(withId(R.id.recyclerView)).check(matches(allOf(
                ViewMatchers.hasDescendant(withText("Espresso task"))
            )))
            Thread.sleep(1500)
        }
    }
}