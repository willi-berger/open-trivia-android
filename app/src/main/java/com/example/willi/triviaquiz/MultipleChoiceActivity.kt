package com.example.willi.triviaquiz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private const val TAG = "MultipleChoiceActivity"

class  MultipleChoiceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MultipleChoiceActivity.onCreate")
        val categoryId = intent.getIntExtra(EXTRA_MESSAGE, 0)
        Log.d(TAG, "categoryId $categoryId")
        setContentView(R.layout.activity_multiple_choice)
    }
}
