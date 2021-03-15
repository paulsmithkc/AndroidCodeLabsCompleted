package edu.ranken.prsmith.whowroteit.model;

import java.io.Serializable;
import java.util.List;

public class BookApiResponse implements Serializable {
    public String kind;
    public int totalItems;
    public List<Book> items;
}
