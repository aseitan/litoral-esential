package com.litoralesential;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//IMPORTS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.litoralesential.Utils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DataManager
{
//VARIABLES
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private JSONArray categoriesArray = null;
    private JSONArray objectivesArray = null;

    private File mUserFile;

    private ArrayList<Category> localCategories;
    private ArrayList<Objective> localObjectives;
    private ArrayList<Category> serverCategories;
    private ArrayList<Objective> serverObjectives;
    private MainActivity mActivity;
    static String serverResponse = null;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Class implementation
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DataManager(){}
    public DataManager(MainActivity activity, File f)
    {
        mActivity = activity;
        mUserFile = f;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Category> GetLocalCategories()
    {
        return localCategories;
    }
    public ArrayList<Objective> GetLocalObjectives()
    {
        return localObjectives;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void writeToLocalFile(String s)
    {
        if(s != null)
        {
            try
            {
                File localFile = new File(mUserFile + File.separator + Utils.userFile);
                if(localFile != null)
                {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(mUserFile + File.separator + Utils.userFile)));
                    bufferedWriter.write(s);
                    bufferedWriter.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void fillLocalArraysFromLocalFile()
    {
        try
        {
            File localFile = new File(mUserFile + File.separator + Utils.userFile);
            if(localFile!=null && localFile.exists())
            {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(localFile));
                String read;
                StringBuilder builder = new StringBuilder("");

                while((read = bufferedReader.readLine()) != null)
                {
                    builder.append(read);
                }
                bufferedReader.close();

                if(builder.toString().length() != 0)
                {
                    JSONObject jsonObj = new JSONObject(builder.toString());
                    categoriesArray = jsonObj.getJSONArray(Utils.TAG_CATEGORIES);
                    objectivesArray = jsonObj.getJSONArray(Utils.TAG_OBJECTIVES);
                    localCategories = new ArrayList<Category>();
                    localObjectives = new ArrayList<Objective>();
                    for(int i = 0; i < categoriesArray.length(); i++)
                    {
                        JSONObject c = categoriesArray.getJSONObject(i);

                        String name = c.getString(Utils.TAG_CATEGORY_NAME);
                        String imageURL = c.getString(Utils.TAG_CATEGORY_IMAGEURL);
                        String website = c.getString(Utils.TAG_CATEGORY_WEBSITE);
                        int id = c.getInt(Utils.TAG_CATEGORY_ID);
                        localCategories.add(new Category(id, name, imageURL, website));
                    }
                    for(int i = 0; i < objectivesArray.length(); i++)
                    {
                        JSONObject c = objectivesArray.getJSONObject(i);

                        int id = c.getInt(Utils.TAG_OBJECTIVE_ID);
                        int categoryID = c.getInt(Utils.TAG_OBJECTIVE_CATEGORYID);
                        String name = c.getString(Utils.TAG_OBJECTIVE_NAME);
                        String imageURL = c.getString(Utils.TAG_OBJECTIVE_IMAGEURL);
                        String description = c.getString(Utils.TAG_OBJECTIVE_DESCRIPTION);
                        String telephone = c.getString(Utils.TAG_OBJECTIVE_TELEPHONE);
                        String GPSposition = c.getString(Utils.TAG_OBJECTIVE_GPSPOSITION);
                        String website = c.getString(Utils.TAG_OBJECTIVE_WEBSITE);

                        localObjectives.add(new Objective(id, categoryID, name, imageURL, description, telephone, GPSposition, website));
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void fillServerArraysFromString(String s )
    {
        if(s != null)
        {
            try
            {
                JSONObject jsonObj = new JSONObject(s);
                categoriesArray = jsonObj.getJSONArray(Utils.TAG_CATEGORIES);
                objectivesArray = jsonObj.getJSONArray(Utils.TAG_OBJECTIVES);
                serverCategories = new ArrayList<Category>();
                serverObjectives = new ArrayList<Objective>();
                for(int i = 0; i < categoriesArray.length(); i++)
                {
                    JSONObject c = categoriesArray.getJSONObject(i);
                    String name = c.getString(Utils.TAG_CATEGORY_NAME);
                    String imageURL = c.getString(Utils.TAG_CATEGORY_IMAGEURL);
                    String website = c.getString(Utils.TAG_CATEGORY_WEBSITE);
                    int id = c.getInt(Utils.TAG_CATEGORY_ID);
                    serverCategories.add(new Category(id, name, imageURL, website));
                }
                for(int i = 0; i < objectivesArray.length(); i++)
                {
                    JSONObject c = objectivesArray.getJSONObject(i);

                    int id = c.getInt(Utils.TAG_OBJECTIVE_ID);
                    int categoryID = c.getInt(Utils.TAG_OBJECTIVE_CATEGORYID);
                    String name = c.getString(Utils.TAG_OBJECTIVE_NAME);
                    String imageURL = c.getString(Utils.TAG_OBJECTIVE_IMAGEURL);
                    String description = c.getString(Utils.TAG_OBJECTIVE_DESCRIPTION);
                    String telephone = c.getString(Utils.TAG_OBJECTIVE_TELEPHONE);
                    String GPSposition = c.getString(Utils.TAG_OBJECTIVE_GPSPOSITION);
                    String website = c.getString(Utils.TAG_OBJECTIVE_WEBSITE);

                    serverObjectives.add(new Objective(id, categoryID, name, imageURL, description, telephone, GPSposition, website));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void fillServerArraysFromServer()
    {
        new ServiceHandler().execute();
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int updateFromLocalFile()
    {
        fillLocalArraysFromLocalFile();
        if( localCategories!=null && localCategories.size() > 0 && localObjectives!=null && localObjectives.size() > 0)
            return 1;
        else
            return 0;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean isLocalCategoriesUpdateRequired()
    {
        //checking
        if( serverCategories==null ||  localCategories==null) return true;
        for(int i=0; i < serverCategories.size(); i++)
        {
            boolean found = false;
            for(int j=0; j < localCategories.size(); j++)
            {
                Category serverCategory = serverCategories.get(i);
                Category localCategory = localCategories.get(j);
                if( serverCategory.id == localCategory.id ) found = true;
            }
            if(found==false) return true;
        }
        return false;
    }
    private boolean isLocalObjectivesUpdateRequired()
    {
        if(serverObjectives==null || localObjectives==null) return true;
        for(int i=0; i < serverObjectives.size(); i++)
        {
            boolean found = false;
            for(int j=0; j < localObjectives.size(); j++)
            {
                Objective serverObjective = serverObjectives.get(i);
                Objective localObjective = localObjectives.get(j);
                if( serverObjective.id == localObjective.id ) found = true;
            }
            if(found==false) return true;
        }
        return false;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//END
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ServiceHandler extends AsyncTask<Void, Void, Void>
    {
        public final static int GET = 1;
        public final static int POST = 2;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if(mActivity!= null)
            {
                mActivity.pDialog = new ProgressDialog(mActivity);
                mActivity.pDialog.setMessage("Please wait...");
                mActivity.pDialog.setCancelable(false);
                mActivity.pDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            // Creating service handler class instance
            ConnectionManager cm = new ConnectionManager();

            // Making a request to url and getting response
            String jsonStr = cm.makeServiceCall(Utils.jsonSiteURL, ServiceHandler.GET);

            return null;

            /*
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
            */
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if(mActivity!= null && mActivity.pDialog!=null)
            {
                if(mActivity.pDialog.isShowing())
                {
                    mActivity.pDialog.dismiss();
                }
            }

            //check if update is required and update
            fillServerArraysFromString(serverResponse);
            if(isLocalCategoriesUpdateRequired() || isLocalObjectivesUpdateRequired())
            {
                writeToLocalFile(serverResponse);
                fillLocalArraysFromLocalFile();
                if(mActivity!=null)
                    mActivity.reload();
            }
        }
    }

    class ConnectionManager
    {
        public final static int GET = 1;
        public final static int POST = 2;

        public String makeServiceCall(String url, int method)
        {
            return this.makeServiceCall(url, method, null);
        }

        public String makeServiceCall(String url, int method, List<NameValuePair> params)
        {
            try
            {
                // http client
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;

                // Checking http request method type
                if (method == POST)
                {
                    HttpPost httpPost = new HttpPost(url);
                    // adding post params
                    if (params != null)
                    {
                        httpPost.setEntity(new UrlEncodedFormEntity(params));
                    }
                    httpResponse = httpClient.execute(httpPost);
                }
                else if (method == GET)
                {
                    try
                    {
                        // appending params to url
                        if (params != null)
                        {
                            String paramString = URLEncodedUtils.format(params, "utf-8");
                            url += "?" + paramString;
                        }
                        HttpGet httpGet = new HttpGet(url);
                        httpResponse = httpClient.execute(httpGet);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                try
                {
                    httpEntity = httpResponse.getEntity();
                    serverResponse = EntityUtils.toString(httpEntity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return serverResponse;
        }
    }
}