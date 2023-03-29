package com.lnct.bhopal.ac.`in`.idealab.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.lnct.bhopal.ac.`in`.idealab.R
import com.lnct.bhopal.ac.`in`.idealab.models.EventModel
import com.lnct.bhopal.ac.`in`.idealab.models.QuestionModel


class QuizWelcomeFragment : Fragment() {

    lateinit var btnStartQuiz: Button
    lateinit var ruletv: TextView
    var event_id: String = "xyz"
    var db: FirebaseFirestore? = null
    val list: ArrayList<QuestionModel> = ArrayList<QuestionModel>()
    var ready = false
    var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz_welcome, container, false)

        db = FirebaseFirestore.getInstance();

        bundle = arguments
        event_id = (bundle!!.getSerializable("event") as EventModel?)?.getId() ?: ""

        btnStartQuiz = view.findViewById(R.id.btnStartQuiz)
        ruletv = view.findViewById(R.id.ruletv)
        btnStartQuiz.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                if (ready) {
//                    val ques_bundle = Bundle()
//                    ques_bundle.putSerializable("event_ques", list)
//                    ques_bundle.putString("event_id", event_id)
                    bundle!!.putSerializable("event_ques", list)
                    findNavController(view).navigate(R.id.assesmentFrgment, bundle)
                } else {
                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show()
                }
            }
        })
        getQuestions()

        return view
    }

    fun getQuestions() {
        db?.collection("events")?.document(event_id)?.get()?.addOnCompleteListener(
            OnCompleteListener<DocumentSnapshot> { task ->
                if (task.isSuccessful) {
                    val data = task.result
                    Log.i("---data---", data["questions"].toString())
                    val questionList = data["questions"] as ArrayList<Map<String, String>>?
                    for (mp in questionList!!) {
                        Log.i("---ques---", mp["question"]!!)
                        if (if (mp["questionType"] == "mcq") true else false) {
                            val opt = ArrayList<String>()
                            val op1 = mp["optionA"]
                            val op2 = mp["optionB"]
                            val op3 = mp["optionC"]
                            val op4 = mp["optionD"]
                            if (op1 != null) opt.add(op1)
                            if (op2 != null) opt.add(op2)
                            if (op3 != null) opt.add(op3)
                            if (op4 != null) opt.add(op4)
                            list.add(QuestionModel(mp["question"], true, opt, mp["qImage"], mp["id"]))
                        } else {
                            list.add(QuestionModel(mp["question"], false, null, mp["qImage"], mp["id"]))
                        }
                    }
                    val sb = StringBuilder()
                    sb.append("This test contains " + list.size + " questions\n\n")
                    sb.append(ruletv.text)
                    ruletv.setText(sb)
                    ready = true
                    Toast.makeText(context, "You can now start the test", Toast.LENGTH_SHORT).show()
                    btnStartQuiz.setText("Start Test")
                } else {
                    findNavController(requireView()).popBackStack()
                    findNavController(requireView()).popBackStack()
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()
                    findNavController(requireView()).navigate(R.id.event2)
                }
            })
    }
}