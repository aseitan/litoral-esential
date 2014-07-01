package com.litoralesential;

class Utils
{
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//STATIC STRINGS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static final String TAG_CATEGORIES = "categories";
public static final String TAG_OBJECTIVES = "objectives";

public static final String TAG_CATEGORY_ID = "categoryID";
public static final String TAG_CATEGORY_NAME = "categoryName";
public static final String TAG_CATEGORY_IMAGEURL = "categoryurlImagine";
public static final String TAG_CATEGORY_WEBSITE = "categorywebsite";

public static final String TAG_OBJECTIVE_ID = "objectiveID";
public static final String TAG_OBJECTIVE_CATEGORYID = "categoryID";
public static final String TAG_OBJECTIVE_NAME = "objectiveName";
public static final String TAG_OBJECTIVE_IMAGEURL = "objectiveImageURL";
public static final String TAG_OBJECTIVE_DESCRIPTION = "objectiveDescription";
public static final String TAG_OBJECTIVE_WEBSITE = "objectiveWebsite";
public static final String TAG_OBJECTIVE_TELEPHONE = "ObjectivePhone";
public static final String TAG_OBJECTIVE_GPSPOSITION = "ObjectiveGPSPosition";

public static String userFile = "data.txt";

public static String jsonSiteURL = "http://www.reporterntv.ro/litoral_esential/appStream";
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//SOME "STRUCTS"
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Category
{
    public int id;
    public String name, imageURL, website;

    public Category()
    {
        id = -1;
        name = imageURL = website = "";
    }
    public Category(int aidi, String n, String url, String site)
    {
        id = aidi;
        name = n;
        imageURL = url;
        website = site;
    }
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Objective
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//DUMP
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////