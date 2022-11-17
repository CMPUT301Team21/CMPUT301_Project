package com.example.myhw.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class BaseBindingActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T viewBinder;
    protected ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type type = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        Class<T> tClass = (Class<T>) actualTypeArguments[0];
        try {
            Method method = tClass.getMethod("inflate", LayoutInflater.class);
            viewBinder = (T) method.invoke(null, getLayoutInflater());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        beforeSetContentView();
        setContentView(viewBinder.getRoot());
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        initData();
        initListener();
    }

    protected void showLoading() {
        dialog.show();
    }

    protected void dismissLoading() {
        dialog.dismiss();
    }

    public void beforeSetContentView() {
    }

    public interface IntentApply {
        void apply(Intent intent);
    }

    public <T extends Activity> void startActivity(Class<T> tClass, IntentApply intentApply) {
        Intent intent = new Intent(this, tClass);
        if (intentApply != null) {
            intentApply.apply(intent);
        }
        startActivity(intent);
    }

    public <T extends Activity> void startActivityForResult(Class<T> tClass, IntentApply intentApply, int requestCode) {
        Intent intent = new Intent(this, tClass);
        if (intentApply != null) {
            intentApply.apply(intent);
        }
        startActivityForResult(intent, requestCode);

    }

    public <T extends Activity> void startActivity(Class<T> tClass) {
        startActivity(tClass, null);
    }


    protected abstract void initListener();

    protected abstract void initData();


    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
