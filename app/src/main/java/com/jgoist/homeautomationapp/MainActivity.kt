package com.jgoist.homeautomationapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.AutoCompleteTextView

class MainActivity : AppCompatActivity() {

    private val mEmailView: AutoCompleteTextView? = null
    private val mPasswordView: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
