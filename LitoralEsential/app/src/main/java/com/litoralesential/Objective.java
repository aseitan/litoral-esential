package com.litoralesential;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Korbul on 7/4/2014.
 */
public class Objective implements Parcelable, Comparator<Objective>, Comparable<Objective>
{
    public int id, categoryID;
    public String name, imageURL, description, telephone, GPSposition, website;
    public boolean specialObjective;
    public Date dateCreated;

    Objective()
    {
        id = categoryID = -1;
        name = imageURL = description = telephone = GPSposition = website = "";
        specialObjective = false;
        dateCreated = new Date(); //default stuff
    }

    public Objective(int id, int categoryID, String name, String imageURL, String description, String telephone, String GPSposition, String website, Date dateCreated)
    {
        this.id = id;
        this.categoryID = categoryID;
        this.name = name;
        this.imageURL = String.format(Utils.OBJECTIVE_IMAGES_URL, id);
        this.description = description;
        this.telephone = telephone;
        this.GPSposition = GPSposition;
        this.website = website;
        this.specialObjective = false;
        this.dateCreated = dateCreated;
    }

	// Parcelling part
	public Objective(Parcel in)
    {
		this.id = in.readInt();
		this.categoryID = in.readInt();
		this.name = in.readString();
		this.imageURL = in.readString();
		this.description = in.readString();
		this.telephone = in.readString();
		this.GPSposition = in.readString();
		this.website = in.readString();
	}

	public int describeContents()
    {
		return 0;
	}

    // Overriding the compareTo method
    public int compareTo(Objective obj)
    {
        return (this.dateCreated).compareTo(obj.dateCreated);
    }

    // Overriding the compare method to sort the age
    public int compare(Objective obj1, Objective obj2)
    {
        return obj1.dateCreated.compareTo(obj2.dateCreated);
    }


    @Override
	public void writeToParcel(Parcel dest, int flags)
    {
		dest.writeInt(this.id);
		dest.writeInt(this.categoryID);
		dest.writeString(this.name);
		dest.writeString(this.imageURL);
		dest.writeString(this.description);
		dest.writeString(this.telephone);
		dest.writeString(this.GPSposition);
		dest.writeString(this.website);
	}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
		public Objective createFromParcel(Parcel in) {
			return new Objective(in);
		}

		public Objective[] newArray(int size) {
			return new Objective[size];
		}
	};
}
