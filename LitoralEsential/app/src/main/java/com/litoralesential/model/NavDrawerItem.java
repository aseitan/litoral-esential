package com.litoralesential.model;

public class NavDrawerItem
{
	private String title;
    private int ID;
    private boolean updatedImage = false;

	public NavDrawerItem(){}

	public NavDrawerItem(int ID, String title)
    {
        this.ID = ID;
		this.title = title;
	}

    public int getID(){
        return this.ID;
    }

	public String getTitle(){
		return this.title;
	}

    public boolean isUpdatedImage() {
        return updatedImage;
    }

    public void setUpdatedImage(boolean updatedImage) {
        this.updatedImage = updatedImage;
    }
}