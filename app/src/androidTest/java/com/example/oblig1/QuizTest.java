package com.example.oblig1;

import android.app.Activity;

import android.view.KeyEvent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class QuizTest {

    @Rule
    public ActivityTestRule<MainActivity> startMain = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void checkScoreAndTotal(){
        try {
            onView(withId(R.id.button2)).perform(click());
        } catch (NoMatchingViewException e) {
            onView(withId(999)).perform(typeText("Tester"));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.button2)).perform(click());
        }
        Quiz quizActivity = (Quiz) getActivityInstance();
        int testScore = 0;
        int testTotal = 0;
        for(int i=0; i<5; i++){
            Image correct = quizActivity.getCurrentImage();
            onView(withId(R.id.userInput)).perform(typeText("Feil")).perform(pressKey(KeyEvent.KEYCODE_ENTER));
            testTotal++;
            onView(withId(R.id.userInput)).perform(typeText("Feil igjen")).perform(pressKey(KeyEvent.KEYCODE_ENTER));
            testTotal++;
            onView(withId(R.id.userInput)).perform(typeText(correct.getName())).perform(pressKey(KeyEvent.KEYCODE_ENTER));
            testScore++;
            testTotal++;
        }
        int actualScore = quizActivity.getScore();
        int actualTotal = quizActivity.getTotal();
        assertEquals(testScore, actualScore);
        assertEquals(testTotal, actualTotal);

    }


    /**
     *
     * @return
     */
    private Activity getActivityInstance() {
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }

}
