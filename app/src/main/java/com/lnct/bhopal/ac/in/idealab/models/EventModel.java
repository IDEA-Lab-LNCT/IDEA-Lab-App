package com.lnct.bhopal.ac.in.idealab.models;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventModel implements Serializable {

    private String id;
    private String image_uri;
    private String title, start_date, desc, end_date, last_reg_date;
    private boolean past_event;
    ArrayList<String> id_list;
    private JSONArray ids;

    public EventModel(String id, String image_uri, String title, String start_date, String desc, String end_date, String last_reg_date, boolean past_event, ArrayList<String> id_list) {
        this.id = id;
        this.image_uri = image_uri;
        this.title = title;
        this.start_date = start_date;
        this.desc = desc;
        this.end_date = end_date;
        this.last_reg_date = last_reg_date;
        this.past_event = past_event;
        this.id_list = id_list;
    }

    public EventModel(String id, String image_uri, String title, String start_date, String desc, String end_date, boolean past_event, JSONArray ids) {
        this.id = id;
        this.image_uri = image_uri;
        this.title = title;
        this.start_date = start_date;
        this.desc = desc;
        this.end_date = end_date;
        this.past_event = past_event;
        this.ids = ids;
    }


    public String getId() {
        return id;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public String getTitle() {
        return title;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isPast_event() {
        return past_event;
    }

    public JSONArray getIds() {
        return ids;
    }

    public static EventModel objToEventModel(JSONObject obj) throws JSONException {


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String cur = dtf.format(now);
//        System.out.println(dtf.format(now));

        EventModel model = new EventModel(obj.getString("_id"), obj.getString("imgUrl"), obj.getString("title"), obj.getString("startDate").substring(0, 10), obj.getString("description"), obj.getString("endDate").substring(0, 10) ,cur.compareTo(obj.getString("startDate").substring(0, 10))<0?false:true, obj.getJSONArray("studentEnroll"));
//        EventModel model = new EventModel("https://www.adobe.com/express/create/media_104b5f1f25bd4d236d5cefc971e8192c7fe6f9318.jpeg?width=400&format=jpeg&optimize=medium", obj.getString("title"), obj.getString("startDate").substring(0, 10), obj.getString("description"), cur.compareTo(obj.getString("startDate").substring(0, 10))<0?false:true, obj.getJSONArray("studentEnroll"));
        return model;


    }

    public static EventModel objToEventModel(QueryDocumentSnapshot document) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");

        String last_reg_date = sdf.format(document.getDate("last_reg_date"));
        String start_date = sdf2.format(document.getDate("start_date"));
        String end_date = sdf2.format(document.getDate("end_date"));

        String cur = sdf.format(Calendar.getInstance().getTime());
        boolean past_event1 = last_reg_date.compareTo(cur)>=0?false:true;

        EventModel model = new EventModel(document.getId(), document.getString("image_url"), document.getString("title"), start_date, document.getString("desc"), end_date, last_reg_date, past_event1, (ArrayList<String>) document.get("ids"));

        return model;
    }

}