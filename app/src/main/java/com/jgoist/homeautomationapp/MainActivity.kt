package com.jgoist.homeautomationapp

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText
import org.jetbrains.anko.design.textInputLayout
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().setContentView(this)
    }

    class MainActivityUI : AnkoComponent<MainActivity> {
        override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
            verticalLayout {
                lparams(width = matchParent, height = matchParent)
                gravity = Gravity.CENTER_HORIZONTAL
                padding = dip(16)

                var email: EditText? = null
                var pass: EditText? = null

                textInputLayout {
                    email = autoCompleteTextView {
                        width = matchParent
                        height = wrapContent
                        hintResource = R.string.prompt_email
                        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        maxLines = 1
                        singleLine = true
                        textColor = android.R.color.black
                    }
                }.lparams(width = matchParent, height = wrapContent)

                textInputLayout {
                    pass = textInputEditText {
                        width = matchParent
                        height = wrapContent
                        hintResource = R.string.prompt_password
                        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                        maxLines = 1
                        singleLine = true
                        textColor = android.R.color.black
                    }
                }.lparams(width = matchParent, height = wrapContent)

                button {
                    textResource = R.string.store_cred_button_text
                    onClick {
                        val prefs = ctx.getSharedPreferences(
                            ctx.resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE
                        ).edit()

                        prefs.putString(ctx.resources.getString(R.string.arlo_email), email!!.text.toString())
                        prefs.putString(ctx.resources.getString(R.string.arlo_pass), pass!!.text.toString())

                        prefs.apply()
                    }
                }.lparams {
                    topMargin = dip(16)
                    width = matchParent
                    height = wrapContent
                }

                button {
                    textResource = R.string.clear_cred_button_text
                    onClick {
                        val prefs = ctx.getSharedPreferences(
                            ctx.resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE
                        ).edit()

                        prefs.remove(ctx.resources.getString(R.string.arlo_email))
                        prefs.remove(ctx.resources.getString(R.string.arlo_pass))

                        prefs.apply()

                    }
                }.lparams {
                    topMargin = dip(16)
                    width = matchParent
                    topMargin = dip(16)
                }
            }.applyRecursively {
                when (it) {
                    is Button -> it.typeface = Typeface.DEFAULT_BOLD
                }
            }
        }
    }
}
