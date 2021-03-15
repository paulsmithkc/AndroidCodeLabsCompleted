package edu.ranken.prsmith.whowroteit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class VolumeInfo implements Serializable {
    public String title;
    public String subtitle;
    public ArrayList<String> authors;
    public String publisher;
    public String publishedDate;
    public String description;
    public Integer pageCount;
    public String printType;
    public String mainCategory;
    public ArrayList<String> categories;
    public Double averageRating;
    public Integer ratingsCount;
    public String contentVersion;
    public HashMap<String, String> imageLinks;
    public String language;
    public String previewLink;
    public String infoLink;
    public String canonicalVolumeLink;
}
