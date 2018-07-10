package com.codepath.alveera.parstagram.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    // list the attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String likeCount;
    public String followersCount;
    public String followingCount;
    public String tweetCount;


    // deserialize the JSON
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        // extract and fill the values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");
        user.likeCount = json.getString("favourites_count");
        user.followersCount = json.getString("followers_count");
        user.followingCount = json.getString("friends_count");
        user.tweetCount = json.getString("statuses_count");


        return user;
    }
}
