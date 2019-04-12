package com.jgoist.homeautomationapp.models

enum class BasestationMode(val apiName: String, val displayName: String) {
    Disarmed("mode0", "Disarmed") {
        override fun next() = Armed
    },
    Armed("mode1", "Armed") {
        override fun next() = Scheduled
    },
    Scheduled("schedule.1", "Scheduled") {
        override fun next() = Disarmed
    },
    Unknown("unknown", "Unknown") {
        override fun next() = Disarmed
    };

    abstract fun next(): BasestationMode

    companion object {
        fun fromApiName(apiName: String): BasestationMode {
            return when (apiName) {
                Disarmed.apiName -> Disarmed
                Armed.apiName -> Armed
                Scheduled.apiName -> Scheduled
                else -> Unknown
            }
        }
    }
}