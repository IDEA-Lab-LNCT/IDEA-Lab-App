package com.lnct.bhopal.ac.in.idealab.frgments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lnct.bhopal.ac.in.idealab.R;
import com.lnct.bhopal.ac.in.idealab.Utils;
import com.lnct.bhopal.ac.in.idealab.activity.FullScreenEvent;
import com.lnct.bhopal.ac.in.idealab.models.EventModel;
import com.lnct.bhopal.ac.in.idealab.quiz.AssesmentDescriptionActivity;

import org.json.JSONArray;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullscreenEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullscreenEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView event_image;
    TextView title, date, desc, btn, edit_btn, cont_btn;
    EventModel event;
    ImageView back;

    boolean registered = false;
    boolean user_available = false;

    String s;
    String title_, desc_, start_date_, end_date, id_;
    JSONArray id_list;
    Bundle bundle;
    EventModel model;

    View dialog_view;
    AlertDialog dialog;

    public FullscreenEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullscreenEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FullscreenEventFragment newInstance(String param1, String param2) {
        FullscreenEventFragment fragment = new FullscreenEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fullscreen_event, container, false);

        bundle = getArguments();
        model = (EventModel) bundle.getSerializable("event");

        title_ = model.getTitle();
        desc_ = model.getDesc();
        start_date_ = model.getStart_date();
        end_date = model.getEnd_date();

        dialog_view = getLayoutInflater().inflate(R.layout.check_profile_dialog, null);
        edit_btn = dialog_view.findViewById(R.id.edit_btn);
        cont_btn = dialog_view.findViewById(R.id.continue_btn);

        dialog = new AlertDialog.Builder(getContext())
                .setView(dialog_view)
                .create();

        event_image = view.findViewById(R.id.event_imageholder);
        title = view.findViewById(R.id.event_titleholder);
        date = view.findViewById(R.id.event_dateholder);
        desc = view.findViewById(R.id.event_descholder);
        btn = view.findViewById(R.id.registerbtn);

        Glide.with(this)
                .load(model.getImage_uri())
                .placeholder(R.drawable.idea_lab_sq_logo)
                .error(R.drawable.app_logo)
                .into(event_image);

        if(model.isPast_event()) btn.setVisibility(View.GONE);

        if(Utils.isUserPresent(getContext())) {
            user_available = true;
            String uid = Utils.getUser(getContext()).get_id();
        }

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
            Navigation.findNavController(view).navigate(R.id.profileFragment);
            dialog.dismiss();
//            Toast.makeText(getContext(), "add intent for profile activity", Toast.LENGTH_SHORT).show();
        });

        cont_btn.setOnClickListener(vw -> {
//            TODO: add navgraph for quiz activity
            Intent intent1 = new Intent(getContext(), AssesmentDescriptionActivity.class);
            startActivity(intent1);
            if(dialog.isShowing()) dialog.dismiss();
        });

        title.setText(title_);
        desc.setText(desc_);
        date.setText(start_date_ + " | " + end_date);

        return view;
    }
}