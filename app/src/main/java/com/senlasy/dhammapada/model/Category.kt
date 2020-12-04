package com.senlasy.dhammapada.model

import android.os.Parcel
import android.os.Parcelable

class Category() : Parcelable  {
     var id : Int = 0
     var title : String? = ""
     var mmtitle : String? = ""
     var palititle : String? = ""
     var markNumber : String? = ""
     var paliroman : String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        mmtitle = parcel.readString()
        palititle = parcel.readString()
        markNumber = parcel.readString()
        paliroman = parcel.readString()
    }


    constructor(id : Int, title : String, mmtitle : String, palititle : String, markNumber: String, paliroman : String) : this() {
        this.id = id
        this.title = title
        this.mmtitle = mmtitle
        this.palititle = palititle
        this.markNumber = markNumber
        this.paliroman = paliroman
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(mmtitle)
        parcel.writeString(palititle)
        parcel.writeString(markNumber)
        parcel.writeString(paliroman)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }


}