package com.jgoist.homeautomationapp.models

import org.junit.Assert.assertEquals
import org.junit.Test

class BasestationModeTest {

    @Test
    operator fun next() {
        assertEquals(BasestationMode.Scheduled, BasestationMode.Armed.next())
        assertEquals(BasestationMode.Disarmed, BasestationMode.Scheduled.next())
        assertEquals(BasestationMode.Armed, BasestationMode.Disarmed.next())
    }

    @Test
    fun getApiName() {
        assertEquals("mode0", BasestationMode.Disarmed.apiName)
        assertEquals("mode1", BasestationMode.Armed.apiName)
        assertEquals("schedule.1", BasestationMode.Scheduled.apiName)
    }

    @Test
    fun getDisplayName() {
        assertEquals("Disarmed", BasestationMode.Disarmed.displayName)
        assertEquals("Armed", BasestationMode.Armed.displayName)
        assertEquals("Scheduled", BasestationMode.Scheduled.displayName)
    }
}