package com.example.pokedexre.pojo

import android.os.Parcel
import android.os.Parcelable

data class Pokemon(val nombre: String = "N/A", val url: String = "N/A", val ftype: String = "N/A", val stype: String = "N/A",  val weight: String = "N/A") : Parcelable {
    constructor(parcel: Parcel) : this(
        nombre = parcel.readString(),
        url = parcel.readString(),
        ftype = parcel.readString(),
        stype = parcel.readString(),
        weight = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(url)
        parcel.writeString(ftype)
        parcel.writeString(stype)
        parcel.writeString(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<Pokemon>{
            override fun createFromParcel(parcel: Parcel): Pokemon = Pokemon(parcel)
            override fun newArray (size: Int): Array<Pokemon?> = arrayOfNulls(size)
        }
    }

}