package com.phearom.assignment.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.phearom.api.core.server.ResponseCallback;
import com.phearom.assignment.R;
import com.phearom.assignment.databinding.ActivityBookDetailsBinding;
import com.phearom.assignment.model.Book;
import com.phearom.assignment.request.RequestGetBookDetails;
import com.phearom.assignment.utils.K;
import com.phearom.assignment.viewmodel.BookViewModel;

import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {

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
        setSupportActionBar(mBinding.toolbarBook);
//        setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getBookDetail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLoading(boolean loading) {
        mBinding.progressbar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void getBookDetail() {
        RequestGetBookDetails requestGetBook = new RequestGetBookDetails(this);
        requestGetBook.setId(getBookId());
        requestGetBook.setOnResponseCallback(new ResponseCallback<Book>() {
            @Override
            public void onSuccess(Book response) {
                setLoading(false);
                mBinding.setBookViewModel(new BookViewModel(response));
            }

            @Override
            public void onError(VolleyError error) {
                setLoading(false);
                Toast.makeText(BookDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestGetBook.execute();
    }

    private void generateToolbarColor(Bitmap bitmap){
        Palette.from( bitmap ).generate( new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated( Palette palette ) {
                //work with the palette here

            }
        });
    }

    private String getBookId() {
        Bundle extras = getIntent().getExtras();
        if (null != extras)
            return extras.getString(K.Id);
        return "0";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.unbind();
        Glide.get(this).clearDiskCache();
        Glide.get(this).clearMemory();
    }
}
