package com.phearom.assignment.viewmodel;

import android.databinding.BaseObservable;

import com.phearom.assignment.model.Book;

/**
 * Created by phearom on 5/31/16.
 */
public class BookLoadViewModel extends BookViewModel {
    public BookLoadViewModel() {
        super(new Book());
    }
}
