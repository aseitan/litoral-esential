package com.litoralesential;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class Utils
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

    public static final String userFile = "data.txt";
    public static final String categoryImageFolder = "category_images";

	public static final String CATEGORY_IMAGE_PREFIX = "cat_img_";
	public static final String OBJECTIVE_IMAGE_PREFIX = "obj_img_";

	public static int SCREEN_SIZE = 0;
	public static String OBJECTIVE_IMAGES_URL = "";

    public static String externalPathRoot = "";

    public static final String jsonSiteURL = "http://www.reporterntv.ro/litoral_esential/appStream";
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean writeToFile(String content, String path)
    {
        if(content != null)
        {
            try
            {
                File localFile = new File(path);
                if(localFile != null)
                {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(localFile));
                    bufferedWriter.write(content);
                    bufferedWriter.close();
                }
                else
                {
                    return false;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    public static boolean writeToFile(byte[] content, String path)
    {
        if(content != null)
        {
            try
            {
                File localFile = new File(path);
                if(localFile != null)
                {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));
                    bos.write(content);
                    bos.flush();
                    bos.close();
                }
                else
                {
                    return false;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            return false;
        }

        return true;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//DUMP
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////