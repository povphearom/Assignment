package com.phearom.assignment.ui;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.phearom.api.core.binder.CompositeItemBinder;
import com.phearom.api.core.binder.ItemBinder;
import com.phearom.api.core.listener.ClickHandler;
import com.phearom.api.core.listener.LoadMoreHandler;
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
    private int mOffset = 0;
    private int mCount = 20;
    private LoadMoreHandler loadMoreHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mBinding.toolbarMain);
        setTitle("Home");

        mBinding.swipeRefresh.setColorSchemeColors(Color.DKGRAY, Color.CYAN, Color.GRAY);
        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mOffset = 0;
                mBooksViewModel.items.clear();
                getListBook(mOffset);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recycler.setLayoutManager(linearLayoutManager);
        loadMoreHandler = new LoadMoreHandler(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                getListBook(mOffset);
                loadMoreHandler.setLoaded();
            }
        };
        mBinding.recycler.addOnScrollListener(loadMoreHandler);

        mBooksViewModel = new BooksViewModel();
        mBinding.setBooksViewModel(mBooksViewModel);
        mBinding.setView(this);
        mOffset = 0;
        getListBook(mOffset);
    }

    private void getListBook(int offset) {
        setLoading(true);
        final RequestGetBook requestGetBook = new RequestGetBook(this);
        requestGetBook.setOffset(offset);
        requestGetBook.setCount(mCount);
        requestGetBook.setOnResponseCallback(new ResponseCallback<List<Book>>() {
            @Override
            public void onSuccess(List<Book> response) {
                mBinding.swipeRefresh.setRefreshing(false);
                try {
                    if (response.size() > 0)
                        mOffset = mOffset + mCount;
                    for (Book b : response) {
                        mBooksViewModel.addItem(new BookViewModel(b));
                    }
                    setLoading(false);
                    response = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                setLoading(false);
                mBinding.swipeRefresh.setRefreshing(false);
                try {
                    Log.e("AllBook", error.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
