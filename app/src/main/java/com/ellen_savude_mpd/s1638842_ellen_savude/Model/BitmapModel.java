package com.ellen_savude_mpd.s1638842_ellen_savude.Model;

import java.io.Serializable;

public class BitmapModel implements Serializable {

    String title, url,link;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
// Getters and setters of the image

    public BitmapModel() {

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
