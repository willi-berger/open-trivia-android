package com.example.willi.triviaquiz

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.willi.triviaquiz.connector.OpenTrivia
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

private const val TAG = "MainActivity";

const val EXTRA_MESSAGE = "com.example.willi.triviaquiz.MESSAGE";

/**
 * main activity
 */
class MainActivity : AppCompatActivity() {

    var categoriesToIdMap = HashMap<String, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // load categories with async Task
        val task = AsynchLoadCategories(this)
        task.execute("urlparam")

//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.planets_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
        // populate difficulty spinner
        val spinnerDiff : Spinner = findViewById(R.id.spinnerDifficulty)
        ArrayAdapter.createFromResource(
            this,
            R.array.difficulty_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerDiff.adapter = adapter
        }

    }

    fun startGame(view : View) {
        Log.d(TAG, "startGame - clicked")
        val spinner : Spinner = findViewById(R.id.spinnerCategory)
        val selectedItem = spinner.selectedItem
        Log.d(TAG, "selected item $selectedItem")
        val selectedId = categoriesToIdMap.get(selectedItem)
        Log.d(TAG, "selected Id = $selectedId")
        val intent = Intent(this, MultipleChoiceActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, selectedId)
        }
        startActivity(intent)
    }

    companion object {

        class AsynchLoadCategories internal constructor(context : MainActivity) : AsyncTask<String, String, String?>() {

            private val activityRef : WeakReference<MainActivity> = WeakReference(context)

            override fun onPreExecute() {
                val activity = activityRef.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: String?): String? {

                val activity = activityRef.get()
                if (activity == null || activity.isFinishing)
                    return null

                // fetch categories
                val categoriesArray : ArrayList<String> = ArrayList<String>();
                Log.d(TAG,"retrieve categories from OpenTrivia");
                for (cat in OpenTrivia().getCategories()) {
                    Log.d(TAG, "adding category ${cat.name} to list")
                    categoriesArray.add(cat.name)
                    activity.categoriesToIdMap.put(cat.name, cat.id)
                }

                // update spinner
                // populate category spinner
                // found in https://stackoverflow.com/questions/35449800/best-practice-to-implement-key-value-pair-in-android-spinner/35450251

                // important only ui threads my change the UI
                activity.runOnUiThread{
                    val categoriesSpinner: Spinner = activity.findViewById(R.id.spinnerCategory)
                    val categoriesAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, categoriesArray)
                    categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categoriesSpinner.adapter = categoriesAdapter
                }

                // hide progress bar
                return "done"
            }

            override fun onPostExecute(result: String?) {

                val activity = activityRef.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.GONE
            }



        }
    }
}
