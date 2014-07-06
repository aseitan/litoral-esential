package com.litoralesential;

/**
 * Created by Korbul on 7/4/2014.
 */
public class Objective
{
    public int id, categoryID;
    public String name, imageURL, description, telephone, GPSposition, website;
    Objective()
    {
        id = categoryID = -1;
        imageURL = description = telephone = GPSposition = "";
    }
    public Objective(int aidi, int catAidi, String n, String url, String d, String t, String gps, String site)
    {
        id = aidi;
        categoryID = catAidi;
        name = n;
        imageURL = url;
        description = d;
        telephone = t;
        GPSposition = gps;
        website = site;
    }
}
