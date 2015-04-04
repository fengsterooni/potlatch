package org.coursera.androidcapstone.potlatch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

public class User implements Parcelable {
    //private long uid;
    private String name;
    private long numGifts;
    private long touchedCount;

    User() {
    }

    public User(String name, int numGifts, int touchedCount) {
        super();
        //this.uid = uid;
        this.name = name;
        this.numGifts = numGifts;
        this.touchedCount = touchedCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumGifts() {
        return numGifts;
    }

    public void setNumGifts(int numGifts) {
        this.numGifts = numGifts;
    }

    public long getTouchedCount() {
        return touchedCount;
    }

    public void setTouchedCount(int touchedCount) {
        this.touchedCount = touchedCount;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, numGifts, touchedCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            return
                    Objects.equal(name, other.name);
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.numGifts);
        dest.writeLong(this.touchedCount);
    }

    private User(Parcel in) {
        this.name = in.readString();
        this.numGifts = in.readLong();
        this.touchedCount = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
