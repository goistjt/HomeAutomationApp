package com.jgoist.homeautomationapp.controllers

import android.content.Context
import com.jgoist.homeautomationapp.R
import org.json.JSONObject
import java.util.*
import khttp.get as httpGet
import khttp.post as httpPost

class ArloController(context: Context) {
    private val prefs = context.getSharedPreferences(R.string.preference_file_key.toString(), Context.MODE_PRIVATE)
    private val apiRoot = "https://arlo.netgear.com/hmsweb"
    private val user = prefs.getString("arloEmail", null)
    private val pw = prefs.getString("arloPassword", null)

    fun login(): JSONObject? {
        if (user == null || pw == null) {
            return null
        }

        return httpPost(
            url = "$apiRoot/login/v2",
            headers = mapOf(
                "Content-Type" to "application/json;charset=UTF-8"
            ),
            json = mapOf(
                "email" to user,
                "password" to pw
            )
        ).jsonObject
    }

    fun getBasestation(token: String): JSONObject? {
        val resp = httpGet(
            url = "$apiRoot/users/devices",
            headers = mapOf(
                "Authorization" to token
            )
        ).jsonObject

        return sequenceOf(resp["data"] as JSONObject).firstOrNull { it["deviceType"] == "basestation" }
    }

    fun setBasestationMode(loginResponse: JSONObject, basestation: JSONObject, mode: BasestationMode): Boolean {
        return httpPost(
            url = "$apiRoot/users/devices/notify/${basestation["deviceId"]}",
            headers = mapOf(
                "Content-Type" to "application/json;charset=UTF-8",
                "Authorization" to loginResponse["token"] as String,
                "xcloudid" to basestation["xCloudId"] as String
            ),
            json = mapOf(
                "from" to "${loginResponse["userId"]}_web",
                "to" to basestation["deviceId"] as String,
                "action" to "set",
                "resource" to "modes",
                "transId" to "mobile!${UUID.randomUUID()}",
                "publishResponse" to true,
                "properties" to mapOf(
                    "active" to mode.apiName
                )
            )
        ).jsonObject["success"] as Boolean
    }

    enum class BasestationMode(var apiName: String) {
        Disarmed("mode0"),
        Armed("mode1"),
    }
}