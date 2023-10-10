package com.rg.headernotes.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Справедливо почти для всех классов в этом пакете:
 *
 * Очень много бойлерплейта из за parcelable, имхо тут можно вообще взять сериализацию,
 * тут никакого IPC нет, сериализации штука быстрая, ну либо взять какой нибудь parcelize плагин
 */
data class EmployerModel(
    val id: String = "",
    val fullName: String = "",
    val job: String = "",
    val age: String = "",
    val notesCount: String = "",
    val tasksCount: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(fullName)
        parcel.writeString(job)
        parcel.writeString(age)
        parcel.writeString(notesCount)
        parcel.writeString(tasksCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmployerModel> {
        override fun createFromParcel(parcel: Parcel): EmployerModel {
            return EmployerModel(parcel)
        }

        override fun newArray(size: Int): Array<EmployerModel?> {
            return arrayOfNulls(size)
        }
    }
}