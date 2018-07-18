package com.riseintech.spulla.utils;

/**
 * Created by user on 10/12/2016.
 */

public class FactsDetails {
    public static String Gossipimg_url = "";
    public static String Gossip_date_time = "";
    public static String Gossip_topic = "";
    public static String Gossip_description = "";

    public static void setGossipImgurl(String imgurl) {
        Gossipimg_url = imgurl;
    }

    public static String getGossipImgUrl() {
        return Gossipimg_url;
    }

    public static void setGossipTime(String datetime) {
        Gossip_date_time = datetime;
    }

    public static String getGossipTime() {
        return Gossip_date_time;
    }
    public static void setGossipTopic(String topic) {
        Gossip_topic = topic;
    }

    public static String getGossipTopic() {
        return Gossip_topic;
    }
    public static void setGossipDesc(String desc) {
        Gossip_description = desc;
    }

    public static String getGossipDesc() {
        return Gossip_description;
    }
}
