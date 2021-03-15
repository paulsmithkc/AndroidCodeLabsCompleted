package edu.ranken.prsmith.whowroteit;

import android.app.Application;

import edu.ranken.prsmith.whowroteit.model.BookDataSource;

public class WhoWroteIt extends Application {
    private static final String LOG_TAG = "WhoWroteIt";

    private BookDataSource bookDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        bookDataSource = new BookDataSource();
    }

    public BookDataSource getBookDataSource() {
        return bookDataSource;
    }

}
