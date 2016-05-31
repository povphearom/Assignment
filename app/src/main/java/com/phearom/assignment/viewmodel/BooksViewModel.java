package com.phearom.assignment.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

/**
 * Created by phearom on 5/31/16.
 */
public class BooksViewModel extends BaseObservable {
    @Bindable
    public ObservableList<BookViewModel> items;

    public BooksViewModel() {
        items = new ObservableArrayList<>();
    }

    public void addItem(BookViewModel bookViewModel) {
        if (items.size() > 0) {
            int index = items.indexOf(bookViewModel);
            if (index > 0)
                items.set(index, bookViewModel);
            else
                items.add(bookViewModel);
        } else {
            items.add(bookViewModel);
        }
    }
}
