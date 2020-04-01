package com.ellen_savude_mpd.s1638842_ellen_savude.Model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
//Maps the getters and setters with the dataloader using pull parser

public class InflatorModel implements Serializable {

    private String title,link,description, language;
    private Date lastBuildDate;
    private BitmapModel image;
    private LinkedList<MappingModel> items;

    public InflatorModel() {
        items = new LinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }


    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = parseDate(lastBuildDate);
    }

    public BitmapModel getImage() {
        return image;
    }
    public void setImage(BitmapModel image) {
        this.image = image;
    }

    public LinkedList<MappingModel> getItems() {
        return items;
    }

    public void setItems(LinkedList<MappingModel> items) {
        this.items = items;
    }

    public void addItem(MappingModel item) {
        items.add(item);
    }

    public void removeItem(int index) { items.remove(index); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InflatorModel channel = (InflatorModel) o;
        return Objects.equals(title, channel.title) &&
                Objects.equals(link, channel.link) &&
                Objects.equals(description, channel.description) &&
                Objects.equals(language, channel.language) &&
                Objects.equals(lastBuildDate, channel.lastBuildDate) &&
                Objects.equals(image, channel.image) &&
                Objects.equals(items, channel.items);
    }

    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
