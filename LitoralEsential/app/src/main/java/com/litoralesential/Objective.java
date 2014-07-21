package com.litoralesential;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Korbul on 7/4/2014.
 */
public class Objective implements Parcelable
{
    public int id, categoryID;
    public String name, imageURL, description, telephone, GPSposition, website;

    Objective()
    {
        id = categoryID = -1;
        name = imageURL = description = telephone = GPSposition = website = "";
    }
    public Objective(int aidi, int catAidi, String n, String url, String d, String t, String gps, String site)
    {
        id = aidi;
        categoryID = catAidi;
        name = n;
        imageURL = String.format(Utils.OBJECTIVE_IMAGES_URL, id);
        description = d;
        telephone = t;
        GPSposition = gps;
        website = site;
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

	public int describeContents(){
		return 0;
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
