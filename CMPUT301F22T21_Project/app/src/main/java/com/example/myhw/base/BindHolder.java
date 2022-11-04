package com.example.myhw.base;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class BindHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

    /**
     * This variable is a BindHolder extends from ViewBinding.
     */
    private VB vb;

    /**
     * Create a view binding holder
     * @param vb This is the viewBinding
     */
    public BindHolder(VB vb) {
        super(vb.getRoot());
        this.vb = vb;
    }

    /**
     * This returns a view binding
     * @return
     *      Return view binding
     */
    public VB getVb() {
        return vb;
    }
}
