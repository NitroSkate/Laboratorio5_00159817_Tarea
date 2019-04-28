package com.example.pokedexre

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.pokedexre.fragments.fragment_content
import com.example.pokedexre.fragments.list_fragment
import com.example.pokedexre.pojo.AppConstants
import com.example.pokedexre.pojo.Pokemon
import com.example.pokedexre.utilities.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity(), list_fragment.PokeListener {

    private lateinit var mainFragment : list_fragment
    private lateinit var secondaryFragment : fragment_content
    var pokelist = ArrayList<Pokemon>()
    //Funcional

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //FetchPokemonTask().execute("")
        if(savedInstanceState != null){
            pokelist = savedInstanceState?.getParcelableArrayList(AppConstants.salvar) ?: ArrayList()
            initFragment()
        }
        else{
            FetchPokemonTask().execute("")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.salvar, pokelist)
        super.onSaveInstanceState(outState)
    }

    fun initFragment(){
        mainFragment = list_fragment.newInstance(pokelist)
        val source = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) R.id.pmain
        else {
            secondaryFragment = fragment_content.newInstance(Pokemon())
            supportFragmentManager.beginTransaction().replace(R.id.mainsecond, secondaryFragment).commit()
            R.id.lmain
        }
        supportFragmentManager.beginTransaction().replace(source, mainFragment).commit()
    }


    override fun managePortraitItemClick(item: Pokemon){
        val pokemonBundle = Bundle()
        pokemonBundle.putParcelable("POKEMON", item)
        startActivity(Intent(this, Datos::class.java).putExtra("clave", item.url))
    }

    override fun manageLandscapeItemClick(pokemon: Pokemon) {
        Log.d("url12", pokemon.url)
        /*secondaryFragment = fragment_content.newInstance(pokemon.url)
        supportFragmentManager.beginTransaction().replace(R.id.mainsecond, secondaryFragment).commit()*/
        QueryPokemonTask().execute(pokemon.url)
    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg query: String): String {
            if (query.isNullOrEmpty()) return ""

            val ID = query[0]
            val pokeAPI = NetworkUtils().buildUrl("pokemon", ID)

            return try {
                NetworkUtils().getResponseFromHttpUrl(pokeAPI)

            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }

        override fun onPostExecute(pokemonInfo: String){
            if (!pokemonInfo.isEmpty()) {
                val root = JSONObject(pokemonInfo)
                val results = root.getJSONArray("results")
                for (i in 0..900) {
                    val result = JSONObject(results[i].toString())
                    pokelist.add(
                        Pokemon(
                            result.getString("name").capitalize(),
                            result.getString("url"),
                            "N/A",
                            "N/A",
                            "N/A"
                        )
                    )
                    Log.d("alv", pokelist[i].nombre)
                }
            } else {
                MutableList(20) { i ->
                    Pokemon(
                        "N/A",
                        "N/A"
                    )
                }
            }
            initFragment()
        }
    }


    private inner class QueryPokemonTask : AsyncTask<String, Void, String>() {
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
                    "N/A",
                    JSONObject(ftype).getString("name").capitalize(),
                    if(stype.isEmpty()) "N/A" else JSONObject(stype).getString("name").capitalize(),
                    weight
                )
            } else {
                Pokemon("N/A", "N/A")
            }
            Log.d("lista", pokemon.nombre)
            secondaryFragment = fragment_content.newInstance(pokemon)
            supportFragmentManager.beginTransaction().replace(R.id.mainsecond, secondaryFragment).commit()

        }
    }

}
