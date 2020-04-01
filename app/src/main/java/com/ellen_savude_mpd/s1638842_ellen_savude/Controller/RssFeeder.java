package com.ellen_savude_mpd.s1638842_ellen_savude.Controller;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.ellen_savude_mpd.s1638842_ellen_savude.Model.MappingModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.BitmapModel;

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
// Invokes the use of the pull parser to get xml content from the url

public class RssFeeder extends Service {


    private static RssFeeder instance = null;
    public Intent globalIn, updateIn, startFinishIn;
    private String result, urlSource;

    private Url url;
    public static final String UPD = "", REFRESH = "";


    public static boolean isInstanceCreated() {
        return instance != null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        globalIn = new Intent();
        updateIn = new Intent();
        startFinishIn = new Intent();
        startFinishIn.setAction(REFRESH);
        updateIn.setAction(REFRESH);
        globalIn.setAction(UPD);
        url= new Url();
        urlSource = url.RSS_URL;

    }

    @Override
    public void onDestroy() {
        instance = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Task().execute();
        return super.onStartCommand(intent, flags, startId);
    }

    private class Task extends AsyncTask<String, Integer, Void> {

        final Object lock = new Object();

        @Override
        public Void doInBackground(String... strings) {
            while (true) {
                startFinishIn.removeExtra("method");
                startFinishIn.putExtra("method", "start");
                sendBroadcast(startFinishIn);
                URL aurl;
                URLConnection yc;
                BufferedReader in = null;
                String inputLine = "";
                result = "";
                Log.e("MyTag", "in run");
                try {
                    Log.e("MyTag", "in try");
                    aurl = new URL(urlSource);
                    yc = aurl.openConnection();
                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    in.readLine();
                    while ((inputLine = in.readLine()) != null) {
                        result = result + inputLine + "\n";
                        Log.e("MyTag", inputLine);
                    }
                    in.close();
                } catch (IOException ae) {
                    Log.e("MyTag", "ioexception");
                    ae.printStackTrace();
                }

                MappingModel item = null;
                InflatorModel myChannel = null;
                BitmapModel myImage = null;
                myChannel = new InflatorModel();
                myChannel.setItems(new LinkedList<MappingModel>());

                //Parse the data using a pull parser to get the rss data
                try {

                    XmlPullParserFactory myFact = XmlPullParserFactory.newInstance();
                    myFact.setNamespaceAware(true);
                    XmlPullParser parser = myFact.newPullParser();
                    parser.setInput(new StringReader(result));
                    int event = parser.getEventType();
                    String currentType = null;
                    int currentLine = 0;
                    int countOfLines = result.split("\n").length;
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
                            } else if (parser.getName().equalsIgnoreCase("image")) {
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
                        publishProgress((int) ((currentLine / (float) countOfLines) * 100));
                        currentLine++;
                        event = parser.next();
                    }
                } catch (XmlPullParserException xme) {
                    Log.e("myTag", xme.toString());
                } catch (IOException ioe) {
                    Log.e("myTag", ioe.toString());
                }
              ChannelPersister  p = new ChannelPersister(myChannel);
                globalIn.putExtra("persister", p);
                sendBroadcast(globalIn);
                synchronized (this) {
                    try {
                        this.wait(300000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onProgressUpdate(Integer... progress) {
            updateIn.putExtra("method", progress[0].toString());
            sendBroadcast(updateIn);
        }

        @Override
        protected void onPostExecute(Void v) {
        }

        @Override
        protected void onPreExecute() {
        }
    }
}