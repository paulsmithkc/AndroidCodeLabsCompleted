package edu.ranken.prsmith.whowroteit.model;

import java.io.Serializable;

public class Book implements Serializable {
    public String kind;
    public String id;
    public String etag;
    public String selfLink;
    public VolumeInfo volumeInfo;
    public SearchInfo searchInfo;
}
