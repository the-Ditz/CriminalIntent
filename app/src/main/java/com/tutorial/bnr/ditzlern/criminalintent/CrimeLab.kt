package com.tutorial.bnr.ditzlern.criminalintent

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.tutorial.bnr.ditzlern.criminalintent.database.CrimeBaseHelper
import com.tutorial.bnr.ditzlern.criminalintent.database.CrimeCursorWrapper
import com.tutorial.bnr.ditzlern.criminalintent.database.CrimeTable
import java.lang.IllegalStateException
import java.util.*

class CrimeLab private constructor(context: Context) {

    private val crimes = mutableListOf<Crime>()
    private val database = CrimeBaseHelper(context).writableDatabase

//
//    //Provides random crimes
//    init {
//        for (i in 0 until 100) {
//            val crime = Crime()
//            crime.title = "Crime #$i"
//            crime.isSolved = i % 2 == 0
//            crimes += crime
//        }
//    }

    fun getCrimes(): List<Crime> {
        val crimes = mutableListOf<Crime>()
        val cursor = queryCrimes(null, null)
        cursor.use {
            it.moveToFirst()
            while (!it.isAfterLast) {
                crimes.add(it.getCrime())
                it.moveToNext()
            }
        }
        return crimes
    }

    fun addCrime(crime: Crime){
        val values = getContentValues(crime)
        database.insert(CrimeTable.NAME, null, values)
    }

    fun updateCrime(crime: Crime) {
        val uuidString = crime.id.toString()
        val values = getContentValues(crime)

        database.update(CrimeTable.NAME, values, "${CrimeTable.Cols.UUID} = ?",
                arrayOf(uuidString))
    }

    private fun queryCrimes(whereClause: String?,
                            whereArgs: Array<String>?) : CrimeCursorWrapper {

        // A cursor is a cursor in the traditional sense, in that it does 'point' to a specific entry,
        // but in this case the Cursor object contains the entiretiy of what was returned from the query
        // so we get everything
        val cursor = database.query(
                CrimeTable.NAME,
                null, // null selects all columns
                whereClause,
                whereArgs,
                null, // group by
                null, //having
                null) //order by
        return CrimeCursorWrapper(cursor)
    }

    fun getCrime(id: UUID): Crime? {
        val cursor =  queryCrimes("${CrimeTable.Cols.UUID} = ?", arrayOf(id.toString()))

        cursor.use {
            if (it.count == 0) {
                return null
            }

            cursor.moveToFirst()
            return cursor.getCrime()
        }
    }

    private fun getContentValues(crime: Crime) : ContentValues {
        val solved = if (crime.isSolved) {
            1
        } else {
            0
        }

        return ContentValues().apply {
            put(CrimeTable.Cols.UUID, crime.id.toString())
            put(CrimeTable.Cols.TITLE, crime.title)
            put(CrimeTable.Cols.DATE, crime.date.time)
            put(CrimeTable.Cols.SOLVED, solved)
            put(CrimeTable.Cols.SUSPECT, crime.suspect)
        }
    }

    companion object {
        private var INSTANCE: CrimeLab? = null

        fun initialize(context: Context) {
            INSTANCE = CrimeLab(context)
        }

        fun get(): CrimeLab {
            return INSTANCE ?:
                    throw IllegalStateException("CrimeLab must be initialized.")
        }
    }
}

