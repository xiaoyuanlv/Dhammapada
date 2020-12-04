package com.senlasy.dhammapada.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.senlasy.dhammapada.model.Category
import com.senlasy.dhammapada.model.Dhamma
import com.senlasy.dhammapada.utility.SharePrefHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DBHelper(private val context: Context) {

    companion object {
        private val DB_NAME = "dhammapada.db"
        private val TBL_CATEGORY = "tbl_category"
        private val TBL_DHAMA = "tbl_dhamma"
    }

    fun openDatabase(): SQLiteDatabase {
        val dbFile = context.getDatabasePath(DB_NAME)

        val currentDBVersion : Boolean =  SharePrefHelper().checkCurrentDB(context)

        if (!dbFile.exists() || !currentDBVersion) {
            try {
                val checkDB = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null)

                checkDB?.close()
                copyDatabase(dbFile)

            } catch (e: IOException) {
                throw RuntimeException("Error creating source database", e)
            }

        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    @SuppressLint("WrongConstant")
    private fun copyDatabase(dbFile: File) {
        val `is` = context.assets.open(DB_NAME)
        val os = FileOutputStream(dbFile)

        val buffer = ByteArray(1024)
        while (`is`.read(buffer) > 0) {
            os.write(buffer)
            Log.d("#DB", "writing>>")
        }

        os.flush()
        os.close()
        `is`.close()
        SharePrefHelper().saveDBVersion(context)

        Log.d("#DB", "completed..")
    }



    fun getFavList(): List<Dhamma> {
        val lstData = ArrayList<Dhamma>()

        val selectQuery = "SELECT  * FROM " + TBL_DHAMA +
                " where fav=1 ORDER BY " +
                " id ASC"

        val db = this.openDatabase()
        val cursor = db.rawQuery(selectQuery, null)


        if (cursor.moveToFirst()) {
            do {
                val dhamma = Dhamma(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("message")),
                    cursor.getString(cursor.getColumnIndex("mm_message")),
                    cursor.getString(cursor.getColumnIndex("pali_message")),
                    cursor.getInt(cursor.getColumnIndex("category_id")),
                    cursor.getInt(cursor.getColumnIndex("fav")),
                    cursor.getString(cursor.getColumnIndex("pali_roman"))
                )

                lstData.add(dhamma)
            } while (cursor.moveToNext())
        }

        // close db connection
        cursor.close()
        db.close()
        return lstData
    }

    fun getFirstNumberOfDhamma(category_id: Int) : Int {
        var first_number = 0

        var query = "SELECT tbl_dhamma.id from tbl_dhamma " +
                " where tbl_dhamma.category_id = " + category_id +
                " order by tbl_dhamma.id " +
                " limit 1 "

        val db = this.openDatabase()
        val cursor = db.rawQuery(query, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                first_number = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return first_number
    }

    fun getDhammaTotalCountByCategory(id: Int) : Int {
        var total_count = 0

        var query = "SELECT COUNT(tbl_dhamma.id) as total_count from tbl_dhamma\n" +
                "where tbl_dhamma.category_id = " + id

        val db = this.openDatabase()
        val cursor = db.rawQuery(query, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                total_count = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return total_count

    }

    fun getCategory(id : Int): List<Category> {
        val lstCategory = ArrayList<Category>()

        // Select All Query
        val selectQuery = "SELECT  * FROM " + TBL_CATEGORY + " where " +
                " id = " + id

        val db = this.openDatabase()
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                var catid  = cursor.getInt(cursor.getColumnIndex("id"))
                var firstNumber = getFirstNumberOfDhamma(catid)
                var totalCount = getDhammaTotalCountByCategory(catid)
                var lastNumber = firstNumber + totalCount - 1
                var mark = firstNumber.toString() + " - " + lastNumber

                val category = Category(catid,
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("mm_title")),
                    cursor.getString(cursor.getColumnIndex("pali_title")),
                    mark,
                    cursor.getString(cursor.getColumnIndex("pali_roman")))

                lstCategory.add(category)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lstCategory
    }

    fun getAllCategory(): List<Category> {
        val lstCategory = ArrayList<Category>()

        // Select All Query
        val selectQuery = "SELECT  * FROM " + TBL_CATEGORY + " ORDER BY " +
                " id ASC"

        val db = this.openDatabase()
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                var catid  = cursor.getInt(cursor.getColumnIndex("id"))
                var firstNumber = getFirstNumberOfDhamma(catid)
                var totalCount = getDhammaTotalCountByCategory(catid)
                var lastNumber = firstNumber + totalCount - 1
                var mark = firstNumber.toString() + " - " + lastNumber

                val category = Category(catid,
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("mm_title")),
                    cursor.getString(cursor.getColumnIndex("pali_title")),
                    mark,
                    cursor.getString(cursor.getColumnIndex("pali_roman")))

                lstCategory.add(category)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lstCategory
    }

    fun getCategoryCount(): Int {
        val countQuery = "SELECT  * FROM " + TBL_CATEGORY
        val db = this.openDatabase()
        val cursor = db.rawQuery(countQuery, null)

        val count = cursor.count
        cursor.close()
        db.close()

        return count
    }

    fun getDhammapada(id: Int): Dhamma? {

        val db = this.openDatabase()

        val cursor = db.query(
            TBL_DHAMA,
            arrayOf("id", "message", "category_id", "fav"),
            "id=?",
            arrayOf(id.toString()), null, null, null, null
        )

        if (cursor != null) {

            if(cursor.moveToFirst()) {
                val dhamma = Dhamma(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("message")),
                    cursor.getString(cursor.getColumnIndex("mm_message")),
                    cursor.getString(cursor.getColumnIndex("pali_message")),
                    cursor.getInt(cursor.getColumnIndex("category_id")),
                    cursor.getInt(cursor.getColumnIndex("fav")),
                    cursor.getString(cursor.getColumnIndex("pali_roman"))
                )
                return dhamma
            }

        }

        cursor.close()
        db.close()

        return null
    }

    fun getAllDhammaByCategory(category_id : Int): List<Dhamma> {
        val lstData = ArrayList<Dhamma>()

        val selectQuery = "SELECT  * FROM " + TBL_DHAMA +
                " where category_id=" + category_id + " ORDER BY " +
                " id ASC"

        val db = this.openDatabase()
        val cursor = db.rawQuery(selectQuery, null)


        if (cursor.moveToFirst()) {
            do {
                val dhamma = Dhamma(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("message")),
                    cursor.getString(cursor.getColumnIndex("mm_message")),
                    cursor.getString(cursor.getColumnIndex("pali_message")),
                    cursor.getInt(cursor.getColumnIndex("category_id")),
                    cursor.getInt(cursor.getColumnIndex("fav")),
                    cursor.getString(cursor.getColumnIndex("pali_roman"))
                )

                lstData.add(dhamma)
            } while (cursor.moveToNext())


        }

        // close db connection
        cursor.close()
        db.close()
        return lstData
    }

    fun getDhammaCount(category_id : Int): Int {
        val countQuery = "SELECT  * FROM " + TBL_DHAMA + " where category_id=" + category_id
        val db = this.openDatabase()
        val cursor = db.rawQuery(countQuery, null)

        val count = cursor.count
        cursor.close()
        db.close()

        return count
    }

    fun saveFav(id : Int, fav :  Int) : Int{
        val db = this.openDatabase()

        val values = ContentValues()
        values.put("fav", fav)


        val uid = db.update(TBL_DHAMA, values, "id=?", arrayOf(id.toString()))

        db.close()

        return uid
    }

}