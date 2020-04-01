package com.ellen_savude_mpd.s1638842_ellen_savude.Model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
//getters and setters for the data being parsed from the DataLoader through Inflator model

public class MappingModel implements Serializable {
    private String title ,description, link, category,location;
    private Date pubDate;
    private double lat;
    private double lon;
    private Date originDate;
    private int depth;
    private double magnitude;

    public MappingModel(String title, String description, String link, String pubDate, String category, double lat, double lon, String originDate, String location, int depth, double magnitude) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = parseDate(pubDate);
        this.category = category;
        this.lat = lat;
        this.lon = lon;
        this.originDate = parseDate(originDate);
        this.location = location;
        this.depth = depth;
        this.magnitude = magnitude;
    }
    public MappingModel() {

    }

    public Date getOriginDate() {
        return originDate;
    }

    public String getLocation() {
        return location;
    }

    public int getDepth() {
        return depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        String[] tokens = description.split(";");
        this.originDate = parseDate(tokens[0].substring(tokens[0].indexOf(":")+2,tokens[0].length()-1));
        this.location = tokens[1].substring(tokens[1].indexOf(":")+2,tokens[1].length()-1);
        this.depth = Integer.parseInt(tokens[3].substring(tokens[3].indexOf(":")+2,tokens[3].length()-4));
        this.magnitude = Double.parseDouble(tokens[4].substring(tokens[4].indexOf(":")+2));
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(String date) {
        this.pubDate = parseDate(date);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MappingModel item = (MappingModel) o;
        return Double.compare(item.lat, lat) == 0 &&
                Double.compare(item.lon, lon) == 0 &&
                depth == item.depth &&
                Double.compare(item.magnitude, magnitude) == 0 &&
                Objects.equals(title, item.title) &&
                Objects.equals(description, item.description) &&
                Objects.equals(link, item.link) &&
                Objects.equals(pubDate, item.pubDate) &&
                Objects.equals(category, item.category) &&
                Objects.equals(originDate, item.originDate) &&
                Objects.equals(location, item.location);
    }

    private Date parseDate(String pubDate) {
        try {
            return new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").parse(pubDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
