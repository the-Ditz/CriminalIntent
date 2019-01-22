package com.tutorial.bnr.ditzlern.criminalintent

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import java.util.*

const val EXTRA_CRIME_ID = "com.tutorial.bnr.ditzlern.criminalintent.crime_id"

class CrimeActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return CrimeFragment()
    }

    companion object {
        fun newIntent(context: Context, crimeId: UUID): Intent {
            return Intent(context, CrimeActivity::class.java).apply {
                putExtra(EXTRA_CRIME_ID, crimeId)
            }
        }
    }
}
