package com.senlasy.dhammapada.model

import android.os.Parcel
import android.os.Parcelable

class Dhamma() : Parcelable  {

     var id  : Int = 0
     var message  : String? = ""
     var mm_message : String? = ""
     var pali_message : String? = ""
     var categoryid : Int = 0
     var fav : Int = 0
     var paliroman : String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        message = parcel.readString()
        mm_message = parcel.readString()
        pali_message = parcel.readString()
        categoryid = parcel.readInt()
        fav = parcel.readInt()
        paliroman = parcel.readString()
    }


    constructor(id : Int, message : String?, mm_message : String?, pali_message : String?, categoryid : Int, fav : Int, paliroman : String?) : this() {
        this.id = id
        this.message = message
        this.mm_message = mm_message
        this.pali_message = pali_message
        this.categoryid = categoryid
        this.fav = fav
        this.paliroman = paliroman
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(message)
        parcel.writeString(mm_message)
        parcel.writeString(pali_message)
        parcel.writeInt(categoryid)
        parcel.writeInt(fav)
        parcel.writeString(paliroman)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dhamma> {
        override fun createFromParcel(parcel: Parcel): Dhamma {
            return Dhamma(parcel)
        }

        override fun newArray(size: Int): Array<Dhamma?> {
            return arrayOfNulls(size)
        }
    }


}