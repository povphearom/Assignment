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
    private int size = 0;

    public BooksViewModel() {
        items = new ObservableArrayList<>();
    }

    public void addItem(BookViewModel bookViewModel) {
        size = items.size();
        if (size > 0) {
            int index = items.indexOf(bookViewModel);
            if (index > 0)
                items.set(index, bookViewModel);
            else
                items.add(bookViewModel);
        } else {
            items.add(bookViewModel);
        }
    }

    public void addFooter() {
        size = items.size();
        if (size > 0) {
            if (items.get(size - 1) instanceof BookLoadViewModel)
                return;
            items.add(new BookLoadViewModel());
        }
    }

    public void removeFooter() {
        size = items.size();
        if (size > 0) {
            if (items.get(size - 1) instanceof BookLoadViewModel) {
                items.remove(size - 1);
            }
        }
    }
}
