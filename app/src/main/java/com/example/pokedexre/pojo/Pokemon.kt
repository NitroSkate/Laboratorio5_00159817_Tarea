package com.example.pokedexre.pojo

import android.os.Parcel
import android.os.Parcelable

data class Pokemon(val nombre: String = "N/A", val url: String = "N/A") : Parcelable {
    constructor(parcel: Parcel) : this(
        nombre = parcel.readString(),
        url = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(url)
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