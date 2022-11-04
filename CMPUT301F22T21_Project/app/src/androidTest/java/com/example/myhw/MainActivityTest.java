package com.example.myhw;



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;



import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

/**
 * THE TEST CASES ARE NOT ENTIRELY DONE, FACE DIFFUTIUES OF CLICKING ITEM IN LIST,
 * AND THE SORT BUTTON
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testAddIngredient(){
        onView(withId(R.id.ingredient)).perform(click());
        onView(withId(R.id.menu_add_ingredient)).perform(click()); //click add button

        onView(withId(R.id.et_description)).perform(ViewActions.typeText("tomato for test"));
        onView(withId(R.id.et_category)).perform(ViewActions.typeText("fruit"));
        onView(withId(R.id.et_unit_cost)).perform(ViewActions.typeText("23"));
        onView(withId(R.id.et_location)).perform(ViewActions.typeText("Fridge"));
        onView(withId(R.id.et_count)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.btn_date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        Espresso.closeSoftKeyboard();//close key board
        onView(withId(R.id.btn_save)).perform(click());

    }

//    @Test
//    public void testAppName() {
//        onView(withText("CMPUT-301-CustomList-Thursday")).check(matches(isDisplayed())); //Check the name on the screen
//    }

    @Test
    public void testDeleteIngredient(){
        testAddIngredient();
        onView(withId(R.id.ingredient)).perform(click());
//
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItem(withText("specific string"), click()));



//        onView(withRecyclerView(R.id.scroll_view).atPosition(3)).perform(click());
        onView(withId(R.id.btn_delete)).perform(click());
    }

    @Test
    public void testAddMealPlan1(){
        onView(withId(R.id.mealPlan)).perform(click());
        onView(withId(R.id.menu_add_plan)).perform(click());
        onView(withText("Add from ingredient")).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
//        onView(withText()).atPosition(0).perform(click());




    }

    @Test
    public void testAddMealPlan2(){
        onView(withId(R.id.mealPlan)).perform(click());
        onView(withId(R.id.menu_add_plan)).perform(click());
        onView(withText("Add from recipe")).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
//        onView(withText()).atPosition(0).perform(click());




    }

    @Test
    public void testAddRecipe(){
        //pick ingredient
        onView(withId(R.id.recipes)).perform(click());
        onView(withId(R.id.menu_add_recipes)).perform(click());
        onView(withId(R.id.et_preparation_time)).perform(ViewActions.typeText("30"));
        onView(withId(R.id.et_number)).perform(ViewActions.typeText("1"));
        onView(withId(R.id.et_category)).perform(ViewActions.typeText("dinner"));
        onView(withId(R.id.et_comments)).perform(ViewActions.typeText("dinner for tonight"));
        onView(withId(R.id.et_title)).perform(ViewActions.typeText("meet ball"));
        //not yet sure how to take a picture here, skip this part
        Espresso.closeSoftKeyboard();//close key board
        onView(withId(R.id.btn_add_ingredient)).perform(click());
        //pick an ingredient


    }

    @Test
    public void testRecipeSort(){

        onView(withId(R.id.recipes)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        onView(withId(R.id.menu_edit)).perform(click());
        onView(withId(R.id.menu_sort)).perform(click());



    }

    @Test
    public void testShoppingList(){
        onView(withId(R.id.shoppingList)).perform(click());
        //check the list item is matched,
//        onView(withText("banana")).perform(isDisplayed());


    }

    @Test
    public void testShoppingListSort1(){
        onView(withId(R.id.shoppingList)).perform(click());
        //check the list item is matched,
        onView(withId(R.id.menu_sort_shopping)).perform(click());
        onView(withText("description")).perform(click());
    }

    @Test
    public void testShoppingListSort2(){
        onView(withId(R.id.shoppingList)).perform(click());
        //check the list item is matched,
        onView(withId(R.id.menu_sort_shopping)).perform(click());
        onView(withText("category")).perform(click());
    }





}
