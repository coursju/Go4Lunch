package com.coursju.go4lunch.gui;


import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.coursju.go4lunch.R;
import com.coursju.go4lunch.controler.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class Go4LunchUITest {
    private UiDevice mUiDevice;
    // If your emulator is slow to show UI, and test failed, increase the sleep time
    private int sleepSpeed1 = 5000; // 10000 recommanded for Windows devices
    private int sleepSpeed2 = 10000; // 20000 recommanded for Windows devices

    @Before
    public void before() throws Exception {
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void authentificationActivityTest() throws InterruptedException, UiObjectNotFoundException {

        if (!mActivityTestRule.getActivity().isCurrentUserLogged()) {
            onView(allOf(withId(R.id.email_login_button),
                            isDisplayed())).perform(click());

            if (mUiDevice.findObject(new UiSelector().text("NONE OF THE ABOVE")).exists()) {
                UiObject mText = mUiDevice.findObject(new UiSelector().text("NONE OF THE ABOVE"));
                mText.click();
            }

        onView(allOf(withId(R.id.email)))
                .perform(scrollTo(), replaceText("db@db.db"), closeSoftKeyboard());
        onView(allOf(withId(R.id.button_next)))
                .perform(scrollTo(), click());
        Thread.sleep(sleepSpeed1);
        onView(allOf(withId(R.id.password)))
                .perform(scrollTo(), replaceText("dbdbdb"), closeSoftKeyboard());
        onView(allOf(withId(R.id.button_done)))
                .perform(scrollTo(), click());
        Thread.sleep(sleepSpeed1);
        }
    }

    @Test
    public void drawerHeaderTest() throws InterruptedException {
        Thread.sleep(sleepSpeed1);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(allOf(withId(R.id.drawer_user_name))).check(matches(withText("db")));
        onView(allOf(withId(R.id.drawer_user_email))).check(matches(withText("db@db.db")));
    }

    @Test
    public void setupExist() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.activity_main_drawer_settings)).perform( click());
        Thread.sleep(sleepSpeed1);
        onView(withId(R.id.delete_user_button))
                .check(matches(withText(mActivityTestRule.getActivity().getResources().getString(R.string.setting_delete_account))));
    }

    @Test
    public void bottomNavigationContent() throws InterruptedException {
        Thread.sleep(sleepSpeed2);
        onView(withId(R.id.action_listview)).perform(click());
        onView(withId(R.id.action_workmates)).perform(click());
        onView(withId(R.id.action_map)).perform(click());
    }

    @Test
    public void detailsExist() throws InterruptedException {
        Thread.sleep(sleepSpeed2);
        onView(withId(R.id.action_listview))
                .perform(click());
        onView(allOf(withId(R.id.list), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.details_floatingActionButton))
                .check(matches(isDisplayed()));
    }

    @Test
    public void workmateListContentOne() throws InterruptedException {
        Thread.sleep(sleepSpeed2);
        onView(withId(R.id.action_workmates)).perform(click());
        Thread.sleep(sleepSpeed1);
        onView(allOf(withId(R.id.list), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void searchButtonExist() throws InterruptedException {
        Thread.sleep(sleepSpeed2);
        onView(withId(R.id.app_bar_search)).check(matches(isClickable())).perform(click());
        onView(withId(R.id.input_search)).check(matches(isDisplayed()));
    }
}
