package com.example.willi.triviaquiz.connector

class MultipleChoice constructor(question: String, nAnswers: Int)
{
    val question = question
    var wrongAnswers : ArrayList<String> = ArrayList(nAnswers)
    val nAnswers = nAnswers
    var correctAnswer : String = ""
    init {

    }
}