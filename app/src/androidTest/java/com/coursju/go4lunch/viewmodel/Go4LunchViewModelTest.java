package com.coursju.go4lunch.viewmodel;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.coursju.go4lunch.controler.MainActivity;
import com.coursju.go4lunch.controler.MapsFragment;
import com.coursju.go4lunch.controler.RestaurantListFragment;
import com.coursju.go4lunch.controler.WorkmatesListFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class Go4LunchViewModelTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void isViewModelObjectsCorrect(){
        Go4LunchViewModel go4LunchViewModel = new ViewModelProvider(mainActivityTestRule.getActivity()).get(Go4LunchViewModel.class);
        assertTrue(go4LunchViewModel.getmMapsFragment() instanceof MapsFragment);
        assertTrue(go4LunchViewModel.getmRestaurantListFragment() instanceof RestaurantListFragment);
        assertTrue(go4LunchViewModel.getmWorkmatesListFragment() instanceof WorkmatesListFragment);
        assertTrue(go4LunchViewModel.getBottomNavItem() instanceof Integer);
        assertTrue(go4LunchViewModel.getSearchZoneVisible() instanceof Boolean);
        assertTrue(go4LunchViewModel.getRestaurantsList().isEmpty());
        assertTrue(go4LunchViewModel.getWorkmateList().isEmpty());
    }
}