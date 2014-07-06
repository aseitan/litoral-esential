package com.litoralesential.downloadable;

import com.litoralesential.Category;

/**
 * Created by Korbul on 7/4/2014.
 */
public class CategoryImageDownloadable extends Downloadable {
    private Category category;

    public CategoryImageDownloadable() {
        setType(FlowType.CATEGORY_IMAGE);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
