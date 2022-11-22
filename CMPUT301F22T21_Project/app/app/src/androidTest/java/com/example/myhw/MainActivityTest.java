package com.example.myhw;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions.*;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.WildcardType;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {



    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testAddIngredient(){
        onView(withId(R.id.ingredient)).perform(click());
        onView(withId(R.id.add)).perform(click()); //click add button

        onView(withId(R.id.et_description)).perform(ViewActions.typeText("tomato for test"));
        onView(withId(R.id.et_category)).perform(ViewActions.typeText("fruit"));
        onView(withId(R.id.et_unit_cost)).perform(click());
        onView(withText("g")).perform(click());
        onView(withId(R.id.et_location)).perform(ViewActions.typeText("Fridge"));
        onView(withId(R.id.et_count)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.btn_date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        Espresso.closeSoftKeyboard();//close key board
        onView(withId(R.id.btn_save)).perform(click());

    }

    @Test
    public void testDeleteIngredient(){
        //test delete a ingredient by first add a ingredient
        testAddIngredient();
//        onView(withId(R.id.ingredient)).perform(click());
        onView(allOf(ViewMatchers.withId(R.id.rv_data), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));//delete the first item in list
        onView((withId(R.id.btn_delete))).perform(click());
    }


    @Test
    public void testIngredientStorageName() {
        onView(withId(R.id.ingredient)).perform(click());
        onView(withText("INGREDIENT STORAGE")).check(matches(isDisplayed())); //Check the name on the screen
    }

    @Test
    public void testMealPlanName() {
        onView(withId(R.id.mealPlan)).perform(click());
        onView(withText("MEAL PLAN")).check(matches(isDisplayed())); //Check the name on the screen
    }

    @Test
    public void testRecipesName() {
        onView(withId(R.id.recipes)).perform(click());
        onView(withText("RECIPES")).check(matches(isDisplayed())); //Check the name on the screen
    }

    @Test
    public void testShoppingListName() {
        onView(withId(R.id.shoppingList)).perform(click());
        onView(withText("SHOPPING LIST")).check(matches(isDisplayed())); //Check the name on the screen
    }


    @Test
    public void testAddMealPlanFromIngredient(){
        // test add meal plan from ingredient staorage
        //first make sure there is something the ingredient staorage
        onView(withId(R.id.mealPlan)).perform(click());
        onView((withId(R.id.title))).perform(click());//select a date
        onView(withId(android.R.id.button1)).perform(click());//press ok on the dialog
        onView(allOf(ViewMatchers.withId(R.id.add), isDisplayed())).perform(click());//add
        onView(withText("Add from ingredient")).perform(click());

        onView(allOf(ViewMatchers.withId(R.id.rv_data), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));//first item in list
        //use the default value 10 as input count
        onView(withId(android.R.id.button1)).perform(click());




    }
//
    @Test
    public void testAddMealPlanFromRecipe(){
        onView(withId(R.id.mealPlan)).perform(click());
        onView((withId(R.id.title))).perform(click());//select a date
        onView(withId(android.R.id.button1)).perform(click());//press ok on the dialog
        //now we have select current date
        onView(allOf(ViewMatchers.withId(R.id.add), isDisplayed())).perform(click());//add
        onView(withText("Add from recipe")).perform(click());

        onView(allOf(ViewMatchers.withId(R.id.rv_data), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));//first item in list




    }

    @Test
    public void testAddRecipe(){
        //pick ingredient
        onView(withId(R.id.recipes)).perform(click());
        onView(allOf(ViewMatchers.withId(R.id.add), isDisplayed())).perform(click());
        onView(withId(R.id.et_preparation_time)).perform(ViewActions.typeText("30"));
        onView(withId(R.id.et_number)).perform(ViewActions.typeText("1"));
        onView(withId(R.id.et_category)).perform(ViewActions.typeText("dinner"));
        onView(withId(R.id.et_comments)).perform(ViewActions.typeText("dinner for tonight"));
        onView(withId(R.id.et_title)).perform(ViewActions.typeText("meet ball"));
        onView(allOf(ViewMatchers.withId(R.id.iv_image), isDisplayed())).perform(click());
        onView(withText("Camera")).perform(click());
        //not yet sure how to take a picture here, so must do it manually to give permission and take a picture
        Espresso.closeSoftKeyboard();//close key board
        onView(withId(R.id.btn_add_ingredient)).perform(click());
        //pick an ingredient


    }

    @Test
    public void testRecipeSort(){

        onView(withId(R.id.recipes)).perform(click());
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        onView(withId(R.id.menu_edit)).perform(click());
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by title")).perform(click());
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by time")).perform(click());
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by number")).perform(click());
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by category")).perform(click());



    }


    @Test
    public void testShoppingListSort(){

        onView(withId(R.id.recipes)).perform(click());
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by description")).perform(click());
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by category")).perform(click());


    }

    @Test
    public void testIngredientStorageSort(){

        onView(withId(R.id.recipes)).perform(click());

//        Espresso.openContextualActionModeOverflowMenu();
//        onView(withText("SORT")).perform(click());
//        onView(allOf(ViewMatchers.withText("Sort by description"), isDisplayed())).perform(click());
//
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by category")).perform(click());
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by best before date")).perform(click());
        Espresso.openContextualActionModeOverflowMenu();
        onView(withText("SORT")).perform(click());
        onView(withText("Sort by location")).perform(click());


    }



    @Test
    public void testShoppingList(){
        onView(withId(R.id.shoppingList)).perform(click());
        //check the list item is matched,
//        onView(withText("banana")).perform(isDisplayed());


    }





}

