package fr.utt.if26.agenda;

import android.os.Parcel;
import android.os.Parcelable;

public class Agenda_list implements Parcelable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Agenda_list(Parcel in){
        id = in.readInt();
        name = in.readString();
    }

    public Agenda_list(){
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Agenda_list> CREATOR = new Creator<Agenda_list>() {
        @Override
        public Agenda_list createFromParcel(Parcel in) {
            return new Agenda_list(in);
        }

        @Override
        public Agenda_list[] newArray(int size) {
            return new Agenda_list[size];
        }
    };
}