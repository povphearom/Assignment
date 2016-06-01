package com.phearom.assignment.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.phearom.api.base.BaseActivity;
import com.phearom.api.server.ResponseCallback;
import com.phearom.assignment.R;
import com.phearom.assignment.databinding.ActivityBookDetailsBinding;
import com.phearom.assignment.model.Book;
import com.phearom.assignment.request.RequestGetBookDetails;
import com.phearom.assignment.utils.K;
import com.phearom.assignment.viewmodel.BookViewModel;

public class BookDetailsActivity extends BaseActivity {
    private ActivityBookDetailsBinding mBinding;

    public static void launch(Activity activity, BookViewModel bookViewModel) {
        if (null != bookViewModel) {
            Intent detailIntent = new Intent(activity, BookDetailsActivity.class);
            detailIntent.putExtra(K.Id, bookViewModel.getId());
            activity.startActivity(detailIntent);
        } else {
            Toast.makeText(activity, "This book don't have details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_details);
        getBookDetail();
    }

    private void setLoading(boolean loading) {
        mBinding.progressbar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void getBookDetail() {
        setLoading(true);
        RequestGetBookDetails requestGetBook = new RequestGetBookDetails(this);
        requestGetBook.setId(getBookId());
        requestGetBook.setOnResponseCallback(new ResponseCallback<Book>() {
            @Override
            public void onSuccess(Book response) {
                try {
                    mBinding.setBookViewModel(new BookViewModel(response));
                    generateToolbarColor(response.getImage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                setLoading(false);
                try {
                    Log.e(BookDetailsActivity.class.getSimpleName(), error.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        requestGetBook.execute();
    }

    private void generateToolbarColor(String urlImage) {
        Glide.with(this)
                .load(urlImage)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(5, 5) {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation glideAnimation) {
                        try {
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    Palette.Swatch swatch = palette.getLightMutedSwatch();
                                    int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
                                    if (swatch != null)
                                        if (swatch.getRgb() != 0)
                                            color = swatch.getRgb();
                                        else if (swatch.getBodyTextColor() != 0)
                                            color = swatch.getBodyTextColor();
                                        else if (swatch.getTitleTextColor() != 0)
                                            color = swatch.getTitleTextColor();
                                    setTaskBarColored(color);
                                    setLoading(false);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private String getBookId() {
        return getIntent().getExtras().getString(K.Id, "0");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.unbind();
    }

    public void setTaskBarColored(int color) {
        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.setStatusBarColor(color);
        }
    }

    @Override
    public void onConnectionChange(boolean connected) {
        if (connected)
            getBookDetail();
    }
}
