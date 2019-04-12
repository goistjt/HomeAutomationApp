package com.jgoist.homeautomationapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store_cred_button.setOnClickListener {
            val prefs = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            ).edit()

            prefs.putString(getString(R.string.arlo_email), email.text.toString())
            prefs.putString(getString(R.string.arlo_pass), password.text.toString())

            prefs.apply()
        }

        clear_cred_button.setOnClickListener {
            val prefs = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            ).edit()

            prefs.remove(getString(R.string.arlo_email))
            prefs.remove(getString(R.string.arlo_pass))

            prefs.apply()
        }
    }
}
