package com.rg.headernotes.models

import android.os.Parcel
import android.os.Parcelable

data class NoteModel(
    val noteTitle: String = "",
    val noteSubTitle: String = "",
    val noteDateTime: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(noteTitle)
        parcel.writeString(noteSubTitle)
        parcel.writeString(noteDateTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteModel> {
        override fun createFromParcel(parcel: Parcel): NoteModel {
            return NoteModel(parcel)
        }

        override fun newArray(size: Int): Array<NoteModel?> {
            return arrayOfNulls(size)
        }
    }
}