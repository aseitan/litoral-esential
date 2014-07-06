package com.litoralesential;

/**
 * Created by Korbul on 7/4/2014.
 */
public class Category
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
