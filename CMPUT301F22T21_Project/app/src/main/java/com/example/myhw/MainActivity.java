package com.example.myhw;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

import com.example.myhw.Ingredient.IngredientFragment;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.databinding.ActivityMainBinding;
import com.example.myhw.plan.PlanFragment;
import com.example.myhw.recipes.RecipesFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void initListener() {

    }

    private int currentPage = 0;
    private Menu menu;

    /**
     * Initialize data
     */
    @Override
    protected void initData() {
        fragments.add(new IngredientFragment());
        fragments.add(new ShoppingListFragment());
        fragments.add(new RecipesFragment());
        fragments.add(new PlanFragment());
        changeFragment(fragments.get(currentPage));
<<<<<<< HEAD
        setTitle("INGREDIENT STORAGE");
=======
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
        viewBinder.bnv.setItemIconTintList(null);
        viewBinder.bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menu.clear();
                switch (item.getItemId()) {
                    case R.id.ingredient:
                        currentPage = 0;
                        setTitle("INGREDIENT STORAGE");
                        getMenuInflater().inflate(R.menu.menu_ingredient, menu);
                        break;
                    case R.id.shoppingList:
                        currentPage = 1;
                        setTitle("SHOPPING LIST");
                        getMenuInflater().inflate(R.menu.menu_shopping, menu);
                        break;
                    case R.id.recipes:
                        currentPage = 2;
                        setTitle("RECIPES");
                        getMenuInflater().inflate(R.menu.menu_recipes, menu);
                        break;
                    case R.id.mealPlan:
                        setTitle("MEAL PLAN");
                        getMenuInflater().inflate(R.menu.menu_plan, menu);
                        currentPage = 3;
                        break;
                }

                changeFragment(fragments.get(currentPage));
                return true;
            }
        });
    }

    /**
     * This records the current page and display corresponding menu.
     * @param menu This is the target menu
     * @return the option menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * This records which menu is selected.
     * @param item This is the selected item
     * @return If the item is clicked
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        for (Fragment fragment : fragments) {
            fragment.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This records the change of fragment.
     * @param fragment This is the fragment need to be changed
     */
    private void changeFragment(Fragment fragment) {

        FragmentManager supportFragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for (Fragment item : fragments) {
            fragmentTransaction.hide(item);
        }
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fcv, fragment).show(fragment);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commitNow();
    }


}