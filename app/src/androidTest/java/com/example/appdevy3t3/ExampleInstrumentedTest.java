package com.example.appdevy3t3;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static Matcher<View> hasDrawable(){
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (!(item instanceof ImageView)) {
                    return false;
                }
                ImageView imageView = (ImageView) item;
                return imageView.getDrawable() != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("The image view has drawable set");
            }
        };
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.appdevy3t3", appContext.getPackageName());
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void pictureIsDisplayedInImageView() {
        // intents
        Intents.init();

        // Create a test bitmap
        Bitmap testBitmap = BitmapFactory.decodeResource(
                InstrumentationRegistry.getInstrumentation().getTargetContext().getResources(),
                R.drawable.dog);

        // put pic in intent
        Intent resultData = new Intent();
        resultData.putExtra("data", testBitmap);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // dont open the camera, immediately try the dog.png for testing
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        // Click camera button
        onView(withId(R.id.btncamera_id)).perform(click());

        // check if device did image capture
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));

        // delay a little bit
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // chekc im there is pic in image view
        onView(withId(R.id.image)).check(matches(isDisplayed())).check(matches(hasDrawable()));


        Intents.release();
    }
}