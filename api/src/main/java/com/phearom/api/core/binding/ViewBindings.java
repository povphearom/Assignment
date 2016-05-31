package com.phearom.api.core.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by phearom on 6/1/16.
 */
public class ViewBindings {
    @SuppressWarnings("unchecked")
    @BindingAdapter("bindSrc")
    public static void setImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).centerCrop().into(imageView);
    }
}
