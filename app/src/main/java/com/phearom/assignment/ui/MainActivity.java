package com.phearom.assignment.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.phearom.api.core.binder.CompositeItemBinder;
import com.phearom.api.core.binder.ItemBinder;
import com.phearom.api.core.listener.ClickHandler;
import com.phearom.api.core.server.ResponseCallback;
import com.phearom.assignment.BR;
import com.phearom.assignment.R;
import com.phearom.assignment.binder.BookBinder;
import com.phearom.assignment.databinding.ActivityMainBinding;
import com.phearom.assignment.model.Book;
import com.phearom.assignment.request.RequestGetBook;
import com.phearom.assignment.viewmodel.BookViewModel;
import com.phearom.assignment.viewmodel.BooksViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private BooksViewModel mBooksViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mBinding.toolbarMain);
        setTitle("Home");

        mBooksViewModel = new BooksViewModel();
        mBinding.setBooksViewModel(mBooksViewModel);
        mBinding.setView(this);
        getListBook();
    }

    private void getListBook() {
        RequestGetBook requestGetBook = new RequestGetBook(this);
        requestGetBook.setOnResponseCallback(new ResponseCallback<List<Book>>() {
            @Override
            public void onSuccess(List<Book> response) {
                for (Book b : response) {
                    mBooksViewModel.addItem(new BookViewModel(b));
                }
                setLoading(false);
                response = null;
            }

            @Override
            public void onError(VolleyError error) {
                setLoading(false);
                Log.e("AllBook", error.getMessage());
            }
        });
        requestGetBook.execute();
    }

    public ItemBinder<BookViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new BookBinder(BR.book, R.layout.item_book)
        );
    }

    public ClickHandler<BookViewModel> clickHandler() {
        return new ClickHandler<BookViewModel>() {
            @Override
            public void onClick(BookViewModel viewModel, View v) {
                BookDetailsActivity.launch(MainActivity.this, viewModel);
            }
        };
    }

    private void setLoading(boolean loading) {
        mBinding.progressbar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBooksViewModel.items.clear();
        mBooksViewModel = null;
        mBinding.unbind();
    }
}
