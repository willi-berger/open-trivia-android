package com.example.willi.triviaquiz

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.willi.triviaquiz.connector.Difficulty
import com.example.willi.triviaquiz.connector.MultipleChoice
import com.example.willi.triviaquiz.connector.OpenTrivia
import kotlinx.android.synthetic.main.activity_multiple_choice.*
import java.lang.ref.WeakReference

private const val TAG = "MultipleChoiceActivity"

class  MultipleChoiceActivity : AppCompatActivity() {


    private var iRightAnswer : Int = 0

    private var multipleChoice : MultipleChoice = MultipleChoice("", 4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MultipleChoiceActivity.onCreate")
        val categoryId = intent.getIntExtra(EXTRA_MESSAGE, 0)
        Log.d(TAG, "categoryId $categoryId")
        setContentView(R.layout.activity_multiple_choice)

        //load multiple choice for category and difficulty in asynch task
        val difficulty = "easy"
        val task = AsynchRetrieveMultipleChoice(this, categoryId)
        task.execute(categoryId)
    }

    fun radioButtonClicked(view : View) {
        Log.d(TAG, "answer radio clicked")
        if (view is RadioButton) {
            val isChecked = view.isChecked
            var ok = false
            when (view.id) {
                R.id.answer0 ->  if (iRightAnswer == 0) ok = true
                R.id.answer1 ->  if (iRightAnswer == 1) ok = true
                R.id.answer2 ->  if (iRightAnswer == 2) ok = true
                R.id.answer3 ->  if (iRightAnswer == 3) ok = true
            }
            if (ok) {
                Toast.makeText(this, "Correct :)", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Wrong :) -- Right answer is ${multipleChoice.correctAnswer}", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        class AsynchRetrieveMultipleChoice internal constructor (activity : MultipleChoiceActivity, categoryId : Int) : AsyncTask<Int, Int, Int>() {

            private val activityRef : WeakReference<MultipleChoiceActivity> = WeakReference(activity)
            private val categoryId = categoryId

            override fun onPreExecute() {
                val activity = activityRef.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.VISIBLE
            }
            override fun doInBackground(vararg params: Int?): Int {
                Log.d(TAG, "AsynchRetrieveMultipleChoice.doInBackground ..")
                 val activity = activityRef.get()
                if (activity == null || activity.isFinishing)
                    return -1  // err

                //retrieve multiple choice
                try {
                    val multipleChoice = OpenTrivia().getMutltipleChoiceQuestion(categoryId, Difficulty.easy)
                    activity.multipleChoice = multipleChoice
                    // populate UI with question and answer
                    activity.runOnUiThread {
                        activity.question.text = multipleChoice.question
                        val iRight = Math.floor(Math.random() * 4).toInt()
                        var ii = 0
                        activity.iRightAnswer = iRight

                        activity.answer0.text = if (iRight == 0) {
                            multipleChoice.correctAnswer
                        } else {
                            multipleChoice.wrongAnswers[ii++]
                        }
                        activity.answer1.text = if (iRight == 1) {
                            multipleChoice.correctAnswer
                        } else {
                            multipleChoice.wrongAnswers[ii++]
                        }
                        activity.answer2.text = if (iRight == 2) {
                            multipleChoice.correctAnswer
                        } else {
                            multipleChoice.wrongAnswers[ii++]
                        }
                        activity.answer3.text = if (iRight == 3) {
                            multipleChoice.correctAnswer
                        } else {
                            multipleChoice.wrongAnswers[ii++]
                        }
                    }

                }
                catch (e: Exception) {  // ToDo catch only OpenTrivia Exc here
                    Log.e(TAG, "Unexpected exc retrieving multiple choice. {$e.message}", e)
                    activity.runOnUiThread(Runnable {
                        Toast.makeText(activity, "An error occurred please retry later: ${e.message}", Toast.LENGTH_LONG).show()
                    })
                    return -1
                }
                return 0
            }
            override fun onPostExecute(result: Int) {

                val activity = activityRef.get()
                if (activity == null || activity.isFinishing)
                    return
                activity.progressBar.visibility = View.GONE
            }
        }
    }
}
