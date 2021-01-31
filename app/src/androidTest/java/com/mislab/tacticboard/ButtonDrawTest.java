package com.mislab.tacticboard;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import junit.framework.TestCase;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ButtonDrawTest extends TestCase{

    // 指名要測試的activity
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule=new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void UICheck(){
        System.out.println("UI Check");

        //check button
        onView(withId(R.id.button_clear)).perform(click()).check(matches(isDisplayed()));

        //check playertimeline display
        onView(withId(R.id.image_p1)).perform(click());
        onView(withId(R.id.player1_timeline_wrap)).perform(click()).check(matches(isDisplayed()));
        //onView(withId(R.id.player2_timeline_wrap)).perform(click()).check(matches(isDisplayed())); //fail

    }
}
