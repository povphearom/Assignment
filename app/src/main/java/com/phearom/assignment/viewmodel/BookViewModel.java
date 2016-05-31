package com.phearom.assignment.viewmodel;

import android.databinding.BaseObservable;

import com.phearom.assignment.model.Book;

/**
 * Created by phearom on 5/31/16.
 */
public class BookViewModel extends BaseObservable {
    private final Book model;

    public BookViewModel(Book book) {
        this.model = book;
    }

    public Book getModel() {
        return this.model;
    }

    public String getId() {
        return this.model.getId();
    }

    public String getTitle() {
        return this.model.getTitle();
    }

    public String getImage() {
        return this.model.getImage();
    }

    public String getAuthor() {
        return this.model.getAuthor();
    }

    public double getPrice() {
        return this.model.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (null != o) {
            try {
                BookViewModel b = (BookViewModel) o;
                if (b.getId().equals(getId()))
                    return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
