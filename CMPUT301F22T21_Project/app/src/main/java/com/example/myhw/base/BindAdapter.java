package com.example.myhw.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class BindAdapter<VB extends ViewBinding, Data> extends RecyclerView.Adapter<BindHolder<VB>> {
    /**
     * This abstract variable is a customized BindingAdapter activity extends from ViewBinding.
     */
    private List<Data> data = new ArrayList<>();

    /**
     * This returns a data
     * @return
     *      Return a data
     */
    public List<Data> getData() {
        return data;
    }


    /**
     * Create a view binding holder
     * @param parent This is the parent of the listview
     * @param viewType This is the type of the list view item
     * @return the bindHolder
     */
    @NonNull
    @Override
    public BindHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BindHolder<>(createHolder(parent));
    }


    /**
     * Create a abstract view binding holder
     * @param parent This is the parent of the listview
     * @return viewBinding
     */
    public abstract VB createHolder(ViewGroup parent);


    /**
     * Connect bind view
     * @param holder This is the holder of the viewBinding
     * @param position This is the position of the item in the list view
     */
    @Override
    public void onBindViewHolder(@NonNull BindHolder<VB> holder, int position) {
        Data d = data.get(position);
        bind(holder.getVb(), d, position);
    }

    /**
     * Abstract method for bind
     * @param vb This is the viewBinding
     * @param data This is the data
     * @param position This is the position of the item in the list view
     */
    public abstract void bind(VB vb, Data data, int position);


    /**
     * This returns data size
     * @return
     *      Return the data size
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

}
