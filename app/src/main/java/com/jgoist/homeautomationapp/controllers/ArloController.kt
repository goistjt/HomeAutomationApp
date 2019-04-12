package com.jgoist.homeautomationapp.controllers

import android.content.Context
import com.jgoist.homeautomationapp.R
import com.jgoist.homeautomationapp.models.BasestationMode
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.NoSuchElementException
import khttp.get as httpGet
import khttp.post as httpPost
import android.util.Log

class ArloController(context: Context) {
    private val prefs = context.getSharedPreferences(R.string.preference_file_key.toString(), Context.MODE_PRIVATE)
    private val apiRoot = "https://arlo.netgear.com/hmsweb"
    private val user = prefs.getString("arloEmail", null)
    private val pw = prefs.getString("arloPassword", null)

    fun cycleMode(): BasestationMode {
        try {
            val loginResponse = login()
            if (loginResponse == null) return BasestationMode.Unknown

            val currentMode = getBasestationMode(loginResponse["token"] as String)
            val basestation = getBasestation(loginResponse["token"] as String)
            if (basestation == null) return BasestationMode.Unknown

            return if (setBasestationMode(loginResponse, basestation, currentMode.next())) {
                currentMode.next()
            } else {
                currentMode
            }
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Failed to update the Arlo Basestation mode", e)
            return BasestationMode.Unknown
        }
    }

    private fun login(): JSONObject? {
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

    private fun getBasestation(token: String): JSONObject? {
        val resp = httpGet(
            url = "$apiRoot/users/devices",
            headers = mapOf(
                "Authorization" to token
            )
        ).jsonObject

        return sequenceOf(resp["data"] as JSONObject).firstOrNull { it["deviceType"] == "basestation" }
    }

    fun getBasestationMode(): BasestationMode {
        try {
            val loginResp = login() ?: return BasestationMode.Unknown
            return getBasestationMode(loginResp["token"] as String)
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Unable to get the current basestation mode", e)
            return BasestationMode.Unknown
        }
    }

    private fun getBasestationMode(token: String): BasestationMode {
        val resp = httpGet(
            url = "$apiRoot/users/devices/automation/active",
            headers = mapOf(
                "Content-Type" to "application/json;charset=UTF-8",
                "Authorization" to token,
                "schemaVersion" to "1"
            )
        ).jsonObject

        if (resp["success"] as Boolean) {
            val data = (resp["data"] as JSONArray)[0] as JSONObject
            val mode = (data["activeModes"] as Array<String> + data["activeSchedules"] as Array<String>)[0]
            return try {
                BasestationMode.fromApiName(mode)
            } catch (e: NoSuchElementException) {
                BasestationMode.Unknown
            }
        }
        return BasestationMode.Unknown
    }

    private fun setBasestationMode(loginResponse: JSONObject, basestation: JSONObject, mode: BasestationMode): Boolean {
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


}