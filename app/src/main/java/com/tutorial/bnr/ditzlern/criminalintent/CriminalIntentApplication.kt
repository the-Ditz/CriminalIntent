package com.tutorial.bnr.ditzlern.criminalintent

import android.app.Application

class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeLab.initialize(this)
    }
}