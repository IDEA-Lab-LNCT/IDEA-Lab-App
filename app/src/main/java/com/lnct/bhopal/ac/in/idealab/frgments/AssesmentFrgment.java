package com.lnct.bhopal.ac.in.idealab.frgments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.lnct.bhopal.ac.in.idealab.R;
import com.lnct.bhopal.ac.in.idealab.Utils;
import com.lnct.bhopal.ac.in.idealab.models.EventModel;
import com.lnct.bhopal.ac.in.idealab.models.QuestionModel;
import com.lnct.bhopal.ac.in.idealab.quiz.AssesmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssesmentFrgment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssesmentFrgment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView q_rv;
    LinearLayout view_holder;
    TextView submit_btn;
    RadioGroup rg;
    SnapHelper snap_helper;
    EditText aet;
    ImageView imageView;
    AlertDialog dialog;

    TextView qtv, btn;

    private int ques_pos;
    ArrayList<QuestionModel> model_list;
    Map<String, String> response;

    String[] ans;
    RadioButton[] rblist;
    FirebaseFirestore db;
    String event_id;
    String uid;
    Bundle bundle;
    Context c;

    ViewGroup.LayoutParams parm;

    public AssesmentFrgment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssesmentFrgment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssesmentFrgment newInstance(String param1, String param2) {
        AssesmentFrgment fragment = new AssesmentFrgment();
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
        View v =  inflater.inflate(R.layout.fragment_assesment_frgment, container, false);
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.event_register_anim_layout, null, false);

        db = FirebaseFirestore.getInstance();
        uid = Utils.getUser(getContext()).get_id();
        c = getContext();

        bundle = getArguments();
        model_list = (ArrayList<QuestionModel>) bundle.getSerializable("event_ques");
        event_id = ((EventModel) bundle.getSerializable("event")).getId();

        Log.i("----size--list-", model_list.size()+"");

        rg = v.findViewById(R.id.rg);
        aet = v.findViewById(R.id.aet);
        btn = v.findViewById(R.id.save_btn);
        qtv = v.findViewById(R.id.qtv);
        imageView = v.findViewById(R.id.imageView);

        rblist = new RadioButton[]{new RadioButton(getContext()), new RadioButton(getContext()), new RadioButton(getContext()), new RadioButton(getContext()), new RadioButton(getContext())};
        response = new HashMap<>();
        dialog = new AlertDialog.Builder(getContext())
                .setView(dialog_view)
                .setCancelable(false)
                .create();

        for(RadioButton rbn: rblist) {
            rbn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            rbn.setTextColor(getResources().getColor(R.color.logo_blue, getContext().getTheme()));
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model_list.get(ques_pos).getImage_url() != null && model_list.get(ques_pos).getImage_url().length() > 5) {
                    Glide.with(getContext())
                            .load(Uri.parse(model_list.get(ques_pos).getImage_url()))
                            .placeholder(R.drawable.cloud3png_load1)
                            .error(R.drawable.cloud3png_error)
                            .into(imageView);
                }
                else {
                    imageView.setImageDrawable(getContext().getDrawable(R.drawable.cloud3png));
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(model_list.get(ques_pos).isMcq() && ques_pos < model_list.size()) {
                    int id = rg.getCheckedRadioButtonId();
                    int idx = rg.indexOfChild(rg.findViewById(id));
                    if(idx != -1) response.put(model_list.get(ques_pos).getQid(), model_list.get(ques_pos).getOptions().get(idx));
                    else response.put(model_list.get(ques_pos).getQid(), "-1: no response");
                }
                else {
                    response.put(model_list.get(ques_pos).getQid(), aet.getText().toString());
                }

                ques_pos++;
                if(ques_pos < model_list.size()) {
                    loadQuestion(ques_pos);
                }
                else {
                    dialog.show();
                    update(view);
                }
            }
        });

        ques_pos = 0;
        loadQuestion(ques_pos);

        return v;
    }


    public void loadQuestion(int i) {
        if(model_list.get(i).getImage_url() != null && model_list.get(i).getImage_url().length() > 5) {
            Glide.with(getContext())
                    .load(Uri.parse(model_list.get(i).getImage_url()))
                    .placeholder(R.drawable.cloud3png_load1)
                    .error(R.drawable.cloud3png_error)
                    .into(imageView);
        }
        else {
            imageView.setImageDrawable(getContext().getDrawable(R.drawable.cloud3png));
        }
        if(model_list.get(i).isMcq()) {
            aet.setVisibility(View.GONE);
            rg.setVisibility(View.VISIBLE);

            rg.removeAllViews();

            ArrayList<String> opt = model_list.get(i).getOptions();

            for(int j=0; j<opt.size(); j++) {
                RadioButton rb = rblist[j];
                rb.setChecked(false);
                if(rb.getParent() != null) ((ViewGroup)rb.getParent()).removeView(rb);
                rb.setText(opt.get(j));
                rg.addView(rb, j, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }

        }
        else {
            aet.setVisibility(View.VISIBLE);
            rg.setVisibility(View.GONE);
            aet.setText("");
        }

        qtv.setText("Q" + (i+1) + ". " + model_list.get(i).getQ());
    }

    public void update(View v) {

        ArrayList<String> lst = ((EventModel) bundle.getSerializable("event")).getId_list();
        lst.add(Utils.getUser(getContext()).get_id());

        Map<String, Map> res_data = new HashMap<>();
        res_data.put(uid, response);

        db.collection("assesments").document(event_id).set(res_data, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    db.collection("events").document(event_id).update("ids", lst).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(c, "Event registration sucessful", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(c, "Registration failed, some error occured", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(c, "Registration failed, some error occured", Toast.LENGTH_LONG).show();
                }
                Navigation.findNavController(v).popBackStack();Navigation.findNavController(v).popBackStack();Navigation.findNavController(v).popBackStack();Navigation.findNavController(v).popBackStack();
                Navigation.findNavController(v).navigate(R.id.event2);
                dialog.dismiss();
            }
        });


    }

}