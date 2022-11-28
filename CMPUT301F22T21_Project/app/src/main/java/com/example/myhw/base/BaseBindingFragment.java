package com.example.myhw.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class BaseBindingFragment<T extends ViewBinding> extends Fragment {
    /**
     * This abstract variable is a customized BindingFragment activity extends from ViewBinding.
     */
    protected T viewBinder;
    protected ProgressDialog dialog;

    /**
     * This returns a view binder
     * @param inflater This is the inflater
     * @param container This is the container
     * @param savedInstanceState This is the saved instance state
     * @return viewBinder
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Type type = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        Class<T> tClass = (Class<T>) actualTypeArguments[0];
        try {
            Method method = tClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            viewBinder = (T) method.invoke(null, inflater, container, false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        initData();
        initListener();
        return viewBinder.getRoot();
    }

    /**
     * Display loading dialog
     */
    protected void showLoading() {
        dialog.show();
    }

    /**
     * dismiss loading dialog
     */
    protected void dismissLoading() {
        dialog.dismiss();
    }

    /**
     * Testing
     * @param msg  This is the display message
     */
    protected void toast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Initialize data
     */
    protected abstract void initData();

    /**
     * Transport data between activities
     */
    public interface IntentApply {
        void apply(Intent intent);
    }

    /**
     * The start of the activity
     * @param tClass This is the class
     * @param intentApply This is the intent
     * @param <T>  This is the parameter
     */
    public <T extends Activity> void startActivity(Class<T> tClass, IntentApply intentApply) {
        Intent intent = new Intent(getActivity(), tClass);
        if (intentApply != null) {
            intentApply.apply(intent);
        }
        startActivity(intent);
    }

    /**
     * The method that calculate the result
     * @param tClass This is the class
     * @param intentApply This is the content
     * @param requestCode This is the request code
     * @param <T> This is the parameter
     */
    public <T extends Activity> void startActivityForResult(Class<T> tClass, IntentApply intentApply, int requestCode) {
        Intent intent = new Intent(getActivity(), tClass);
        if (intentApply != null) {
            intentApply.apply(intent);
        }
        startActivityForResult(intent, requestCode);

    }

    /**
     * The start activity
     * @param tClass This is the class
     * @param <T> This is the parameter
     */
    public <T extends Activity> void startActivity(Class<T> tClass) {
        startActivity(tClass, null);
    }

    /**
     * Initialize listener
     */
    protected abstract void initListener();

}
