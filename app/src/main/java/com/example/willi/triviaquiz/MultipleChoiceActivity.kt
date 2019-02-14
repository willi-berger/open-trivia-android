package com.example.willi.triviaquiz

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.willi.triviaquiz.connector.OpenTrivia
import kotlinx.android.synthetic.main.activity_multiple_choice.*
import java.lang.ref.WeakReference

private const val TAG = "MultipleChoiceActivity"

class  MultipleChoiceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MultipleChoiceActivity.onCreate")
        val categoryId = intent.getIntExtra(EXTRA_MESSAGE, 0)
        Log.d(TAG, "categoryId $categoryId")
        setContentView(R.layout.activity_multiple_choice)

        //load multiple choice for category and difficulty in asynch task
        val difficulty = "easy"
        val task = AsynchRetrieveMultipleChoice(this)
        task.execute(categoryId)
    }


    companion object {
        class AsynchRetrieveMultipleChoice internal constructor (activity : MultipleChoiceActivity) : AsyncTask<Int, Int, Int>() {

            private val activityRef : WeakReference<MultipleChoiceActivity> = WeakReference(activity)

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
                // ToDo use categoryId from params
                val multipleChoice = OpenTrivia().getMutltipleChoiceQuestion(1)

                //TODo populate UI with question and answer
                activity.question.text = multipleChoice.correctAnswer
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
