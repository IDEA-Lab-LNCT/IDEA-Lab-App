package com.lnct.bhopal.ac.in.idealab.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lnct.bhopal.ac.in.idealab.Constants;
import com.lnct.bhopal.ac.in.idealab.R;
import com.lnct.bhopal.ac.in.idealab.Utils;
import com.lnct.bhopal.ac.in.idealab.auth.LoginActivity;
import com.lnct.bhopal.ac.in.idealab.models.EventModel;
import com.lnct.bhopal.ac.in.idealab.quiz.AssesmentDescriptionActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FullScreenEvent extends AppCompatActivity {

    ImageView event_image;
    TextView title, date, desc, btn, edit_btn, cont_btn;
    EventModel event;
    ImageView back;

    boolean registered = false;
    boolean user_available = false;

    String s;
    String title_, desc_, start_date_, end_date, id_;
    JSONArray id_list;

    View dialog_view;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_event);

        Intent intent = getIntent();

        EventModel model = (EventModel) getIntent().getSerializableExtra("data");

        title_ = model.getTitle();
        desc_ = model.getDesc();
        start_date_ = model.getStart_date();
        end_date = model.getEnd_date();

        dialog_view = getLayoutInflater().inflate(R.layout.check_profile_dialog, null);
        edit_btn = dialog_view.findViewById(R.id.edit_btn);
        cont_btn = dialog_view.findViewById(R.id.continue_btn);

        dialog = new AlertDialog.Builder(this)
                .setView(dialog_view)
                .create();

//        title_ = intent.getStringExtra("title");
//        desc_ = intent.getStringExtra("desc");
//        date_ = intent.getStringExtra("date");
//        id_ = intent.getStringExtra("id");
//
//        try {
//            id_list = new JSONArray(intent.getStringExtra("event_id"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        event_image = findViewById(R.id.event_imageholder);
        title = findViewById(R.id.event_titleholder);
        date = findViewById(R.id.event_dateholder);
        desc = findViewById(R.id.event_descholder);
        back = findViewById(R.id.backbtn);
        btn = findViewById(R.id.registerbtn);

        Glide.with(this)
                .load(model.getImage_uri())
                .placeholder(R.drawable.idea_lab_sq_logo)
                .error(R.drawable.app_logo)
                .into(event_image);

        if(model.isPast_event()) btn.setVisibility(View.GONE);

        if(Utils.isUserPresent(this)) {
            user_available = true;
            String uid = Utils.getPrefs(this).getString("USER_ID", "000");
            for(int i=0; i<id_list.length(); i++) {
                try {
                    if(id_list.get(i).equals(uid)) {
                        btn.setText("Registered");
                        btn.setBackgroundColor(getResources().getColor(R.color.gray, getTheme()));
                        registered = true;
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        File f = Utils.getImageCacheDir(this);
        File image = new File(f,  File.separator + id_ + ".jpeg");
        FileInputStream in = null;
        try {
             in = new FileInputStream(image);
            Bitmap bmp = BitmapFactory.decodeStream(in);
            event_image.setImageBitmap(bmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        for(EventModel ev: Constants.event_list) {
//            if(ev.getId().equals(s)) {
//                this.event = ev;
//                break;
//            }
//        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
//                TODO: uncomment the following lines for user availblity and registered list
//                if(user_available) {
//                    if(!registered) {
//                        dialog.show();
//                    }
//                }
//                else {
//                    Toast.makeText(FullScreenEvent.this, "Login before registering", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(FullScreenEvent.this, LoginActivity.class);
//                    startActivity(i);
//                    finishAffinity();
//                    finish();
//                }
            }
        });

        edit_btn.setOnClickListener(vw -> {
//            TODO: add code for profile activity
            Toast.makeText(this, "add intent for profile activity", Toast.LENGTH_SHORT).show();
        });

        cont_btn.setOnClickListener(vw -> {
            Intent intent1 = new Intent(FullScreenEvent.this, AssesmentDescriptionActivity.class);
            startActivity(intent1);
            if(dialog.isShowing()) dialog.dismiss();
        });

        title.setText(title_);
        desc.setText(desc_);
        date.setText(start_date_ + " | " + end_date);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}