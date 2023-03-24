package com.lnct.bhopal.ac.in.idealab.models;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionModel implements Serializable {

    private String q;
    private boolean mcq;
    private ArrayList<String> options;
    private String image_url;
    private String qid;

    public QuestionModel(String q, boolean mcq, ArrayList<String> options, String image_url, String qid) {
        this.q = q;
        this.mcq = mcq;
        this.options = options;
        this.image_url = image_url;
        this.qid = qid;
    }

    public String getQ() {
        return q;
    }

    public boolean isMcq() {
        return mcq;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getQid() {
        return qid;
    }
}
