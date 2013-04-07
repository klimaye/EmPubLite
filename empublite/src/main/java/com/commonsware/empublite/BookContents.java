package com.commonsware.empublite;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: klimaye
 * Date: 4/7/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookContents {

    JSONObject raw = null;
    JSONArray chapters;

    public BookContents(JSONObject raw) {
        this.raw = raw;
        chapters = raw.optJSONArray("chapters");
    }

    int getChapterCount() {
        return chapters.length();
    }

    String getChapterFile(int position) {
        JSONObject chapter = chapters.optJSONObject(position);
        return chapter.optString("file");
    }

    String getTitle() {
        return raw.optString("title");
    }
}
