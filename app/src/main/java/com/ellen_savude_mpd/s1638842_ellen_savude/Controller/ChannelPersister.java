package com.ellen_savude_mpd.s1638842_ellen_savude.Controller;

import android.os.Parcel;
import android.os.Parcelable;

import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
//This class is responsible for parcing the getter and setter from the inflator model

public class ChannelPersister implements Parcelable {
//create an instance of class Inflator model
    private InflatorModel channel = new InflatorModel();

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeValue(channel);
    }

    public ChannelPersister(Parcel p) {

        channel = (InflatorModel) p.readValue(channel.getClass().getClassLoader());
    }


    public ChannelPersister(InflatorModel channel) {
        this.channel = channel;
    }

    public InflatorModel getChannel() {
        return channel;
    }

    public static final Parcelable.Creator<ChannelPersister> CREATOR = new Parcelable.Creator<ChannelPersister>(){

        @Override
        public ChannelPersister createFromParcel(Parcel parcel) {
            return new ChannelPersister(parcel);
        }

        @Override
        public ChannelPersister[] newArray(int size) {
            return new ChannelPersister[0];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }
}
