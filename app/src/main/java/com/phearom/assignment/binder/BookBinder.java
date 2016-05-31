package com.phearom.assignment.binder;

import com.phearom.api.core.binder.ConditionalDataBinder;
import com.phearom.assignment.viewmodel.BookViewModel;

/**
 * Created by phearom on 5/31/16.
 */
public class BookBinder extends ConditionalDataBinder<BookViewModel> {
    public BookBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(BookViewModel model) {
        return true;
    }
}
