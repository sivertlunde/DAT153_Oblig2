package com.example.oblig1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
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
public class DatabaseTest {

    @Rule
    public ActivityTestRule<MainActivity> startMain = new ActivityTestRule<>(MainActivity.class);

    private ImageRepository repo;


    @Before
    public void onBefore() {
        repo = new ImageRepository(startMain.getActivity().getApplication());
        deleteAllFromDb();
        Bitmap bmDwight = BitmapFactory.decodeResource(startMain.getActivity().getResources(), R.drawable.dwight);
        Image dwight = new Image("Dwight", bmDwight);
        addImageToDB(dwight);
        startMain.getActivity().dbIsEmpty = false;
    }

    @Test
    public void checkAdd(){
        try {
            onView(withId(R.id.button)).perform(click());

        } catch (NoMatchingViewException e) {
            onView(withId(999)).perform(typeText("Tester"));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.button)).perform(click());
        }
        Database databaseActivity = (Database) getActivityInstance();
        int countBefore = databaseActivity.getCount();
        onView(withId(R.id.button5)).perform(click());

        //Creating and adding 3 images to the database
        Bitmap bmCreed = BitmapFactory.decodeResource(databaseActivity.getResources(), R.drawable.creed);
        Bitmap bmJim = BitmapFactory.decodeResource(databaseActivity.getResources(), R.drawable.jim);
        Bitmap bmMichael = BitmapFactory.decodeResource(databaseActivity.getResources(), R.drawable.michael);
        Image creed = new Image("Creed", bmCreed);
        Image jim = new Image("Jim", bmJim);
        Image michael = new Image("Michael", bmMichael);
        addImageToDB(creed);
        addImageToDB(jim);
        addImageToDB(michael);
        onView(withId(R.id.button)).perform(click());
        Database databaseActivity2 = (Database) getActivityInstance();
        int countAfter = databaseActivity2.getCount();
        assertEquals(countAfter, countBefore+3);

    }

    @Test
    public void checkDelete(){
        try {
            onView(withId(R.id.button)).perform(click());

        } catch (NoMatchingViewException e) {
            onView(withId(999)).perform(typeText("Tester"));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.button)).perform(click());
        }
        Database databaseActivity = (Database) getActivityInstance();
        int countBefore = databaseActivity.getCount();
        onView(withText("Dwight")).perform(longClick());
        onView(withText("Yes")).perform(click());
        int countAfter = databaseActivity.getCount();
        assertEquals(countAfter, countBefore-1);

    }

    private void addImageToDB(Image image){

        final Image toAdd = image;
        class SaveImage extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //adding to database
                repo.getImageDao().addImage(toAdd);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        SaveImage si = new SaveImage();
        si.execute();
    }

    private void deleteAllFromDb(){
        class DeleteAll extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //Deleting all from database
                repo.getImageDao().nukeTable();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        DeleteAll da = new DeleteAll();
        da.execute();
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
