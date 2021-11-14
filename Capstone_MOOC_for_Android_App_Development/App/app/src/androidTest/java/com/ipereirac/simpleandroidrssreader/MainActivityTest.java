package com.ipereirac.simpleandroidrssreader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;



import android.content.Context;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import junit.framework.TestCase;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends TestCase {

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {@link androidx.test.rule.ActivityTestRule}.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testMainActivity() {
        onView(withId(R.id.sample_content_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.sample_content_fragment))
                .perform(click());

        onView(isRoot()).perform(waitFor(5000));

        onView(withId(R.id.recyclerView))
                .perform(actionOnItemAtPosition(0, click()));


    }



    @Test
    public void testGotoFavActivity() {
        onView(withId(R.id.button_favorites)).check(matches(isDisplayed()));
        onView(withId(R.id.button_favorites))
                .perform(click());

        onView(withId(R.id.favorites_content_fragment)).check(matches(isDisplayed()));
    }


    public static ViewAction waitFor(long delay) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return ViewMatchers.isRoot();
            }

            @Override public String getDescription() {
                return "wait for " + delay + "milliseconds";
            }

            @Override public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }

//    @Test
//    public void testMainActivity() {
//        onView(withId(R.id.sample_content_fragment)).check(matches(isDisplayed()));
//        onView(withId(R.id.sample_content_fragment))
//                .perform(click());
//    }


}