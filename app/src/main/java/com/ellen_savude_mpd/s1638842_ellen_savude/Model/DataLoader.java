package com.ellen_savude_mpd.s1638842_ellen_savude.Model;


import android.util.Log;

import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.Url;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;


// Name: Ellen Savude Ngatia
// Student ID: S1638842
//loads data from url and parses them with pull parser

public class DataLoader {

    private String result, urlSource;
    private InflatorModel channel;
    private Url url;

    public DataLoader() {
        returnThread();
    }
    public Thread returnThread() {
        return new Thread(new Task(urlSource));
    }

    public InflatorModel getLoadedChannel() {
        url=new Url();
        urlSource = url.RSS_URL;
        Thread t = new Thread(new Task(urlSource));
        t.start();
        while(channel == null) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        return channel;
    }

    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }

        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            result = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                in.readLine();
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
                ae.printStackTrace();
            }

            channel = parseXML(result);

        }

        public InflatorModel parseXML(String data) {

            MappingModel item = null;
            InflatorModel myChannel;
            BitmapModel myImage = null;
            myChannel = new InflatorModel();
            myChannel.setItems(new LinkedList<MappingModel>());

            try {

                XmlPullParserFactory myFact = XmlPullParserFactory.newInstance();
                myFact.setNamespaceAware(true);
                XmlPullParser parser = myFact.newPullParser();
                parser.setInput(new StringReader(data));
                int event = parser.getEventType();
                String currentType = null;

                while (event != XmlPullParser.END_DOCUMENT) {
                    if (event == XmlPullParser.START_TAG) {
                        if (parser.getName().equalsIgnoreCase("channel")) {
                            myChannel = new InflatorModel();
                            currentType = "channel";
                        } else if (parser.getName().equalsIgnoreCase("title") && currentType == "channel") {
                            myChannel.setTitle(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("link") && currentType == "channel") {
                            myChannel.setLink(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("description") && currentType == "channel") {
                            myChannel.setDescription(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("language")) {
                            myChannel.setLanguage(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("lastBuildDate")) {
                            myChannel.setLastBuildDate(parser.nextText());
                        }  else if (parser.getName().equalsIgnoreCase("image")) {
                            myImage = new BitmapModel();
                    currentType = "image";
                        } else if (parser.getName().equalsIgnoreCase("title") && currentType == "image") {
                            myImage.setTitle(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("url") && currentType == "image") {
                            myImage.setURL(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("link") && currentType == "image") {
                            myImage.setLink(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("item")) {
                            item = new MappingModel();
                            currentType = "item";
                        } else if (parser.getName().equalsIgnoreCase("title") && currentType == "item") {
                            item.setTitle(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("description") && currentType == "item") {
                            item.setDescription(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("link") && currentType == "item") {
                            item.setLink(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(parser.nextText());
                        } else if (parser.getName().equalsIgnoreCase("category")) {
                            item.setCategory(parser.nextText());
                        } else if (parser.getPrefix() != null) {
                            if (parser.getName().equalsIgnoreCase("lat")) {
                                item.setLat(Double.parseDouble(parser.nextText()));
                            } else {
                                item.setLon(Double.parseDouble(parser.nextText()));

                            }
                        }
                    } else if (event == XmlPullParser.END_TAG) {
                        if (parser.getName().equalsIgnoreCase("image")) {
                            myChannel.setImage(myImage);
                        } else if (parser.getName().equalsIgnoreCase("item")) {
                            myChannel.addItem(item);
                        } else if (parser.getName().equalsIgnoreCase("channel")) {
                        }
                    }
                    event = parser.next();
                }
            } catch (XmlPullParserException xme) {
                Log.e("myTag",xme.toString());
            } catch (IOException ioe) {
                Log.e("myTag", ioe.toString());
            }
            return myChannel;
        }
    }
}
