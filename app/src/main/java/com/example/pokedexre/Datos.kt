package com.example.pokedexre

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.pokedexre.pojo.Pokemon
import com.example.pokedexre.utilities.NetworkUtils
import kotlinx.android.synthetic.main.datos.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class Datos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.datos)

        val uri:String = this.intent.extras.getString("clave")
        Log.d("uri", uri)
        FetchTask().execute(uri)
    }


    fun init(pokemon: Pokemon){
        nombre.text = pokemon.nombre
        tipo.text = pokemon.ftype
        tipo2.text = pokemon.stype
        peso.text = pokemon.weight + " " +  "g"
    }

    private inner class FetchTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg query: String): String {
            if (query.isNullOrEmpty()) return ""

            val url = query[0]
            val pokeApi = Uri.parse(url).buildUpon().build()
            val finalurl = try {
                URL(pokeApi.toString())
            } catch (e: MalformedURLException) {
                URL("")
            }

            return try {
                 NetworkUtils().getResponseFromHttpUrl(finalurl)

            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }

        override fun onPostExecute(result: String) {
            val pokemon = if (!result.isEmpty()) {
                Log.d("json", result)
                val root = JSONObject(result)
                val types = root.getJSONArray("types")
                val ftype = JSONObject(types[0].toString()).getString("type")
                val stype = try { JSONObject(types[1].toString()).getString("type") } catch (e: JSONException) {""}
                val weight = root.getString("weight")

                Pokemon(
                    root.getString("name").capitalize(),
                    "N/A" ,
                    JSONObject(ftype).getString("name").capitalize(),
                    if(stype.isEmpty()) "N/A" else JSONObject(stype).getString("name").capitalize(),
                    weight
                )
            } else {
                Pokemon("N/A", "N/A")
            }
            Log.d("lista", pokemon.nombre)
            init(pokemon)
        }
    }

}
