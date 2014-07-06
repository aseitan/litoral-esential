package com.litoralesential.downloadable;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korbul on 7/4/2014.
 */
public class Downloadable {
    private FlowType type;
    private String url;
    private byte[] responseBody;
    private HttpMethod httpMethod;
    private List<NameValuePair> params;
	private int StatusCode;

    public Downloadable(){
        type = FlowType.NONE;
        httpMethod = HttpMethod.GET;
        url = "";
        responseBody = null;
        params = null;
    }

    public FlowType getType() {
        return type;
    }

    public void setType(FlowType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void addParam(String key, String value) {
        if(params == null)
        {
            params = new ArrayList<NameValuePair>();
        }
        params.add(new BasicNameValuePair(key, value));
    }

    public org.apache.http.HttpEntity getPOSTParams() throws UnsupportedEncodingException {
        return new UrlEncodedFormEntity(params);
    }

    public String getUrlWithParams() {
        String paramString = URLEncodedUtils.format(params, "utf-8");
        String tempUrl = url;
        tempUrl += "?" + paramString;
        return tempUrl;
    }

    public boolean hasParameters() {
        return (params != null);
    }

	public int getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(int StatusCode) {
		this.StatusCode = StatusCode;
	}

	public enum FlowType {
        NONE,
        APP_DATA,
        CATEGORY_IMAGE,
        OBJECTIVE_IMAGE
    }
}
