package com.example.pokedexre

import android.content.Intent
import android.content.res.Configuration
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
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), list_fragment.PokeListener {

    private lateinit var mainFragment : list_fragment
    private lateinit var secondaryFragment : fragment_content
    var pokelist = ArrayList<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FetchPokemonTask().execute("")
        pokelist = savedInstanceState?.getParcelableArrayList(AppConstants.salvar) ?: ArrayList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.salvar, pokelist)
        super.onSaveInstanceState(outState)
    }

    fun initFragment(){
        mainFragment = list_fragment.newInstance(pokelist)
        val source = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) R.id.pmain
        else {/*
            secondaryFragment = fragment_content.newInstance(Pokemon())
            supportFragmentManager.beginTransaction().replace(R.id.mainsecond, secondaryFragment).commit()
            R.id.lmain*/
            R.id.pmain
        }
        supportFragmentManager.beginTransaction().replace(source, mainFragment).commit()
    }


    override fun managePortraitItemClick(item: Pokemon){
        val pokemonBundle = Bundle()
        pokemonBundle.putParcelable("POKEMON", item)
        startActivity(Intent(this, Datos::class.java).putExtra("clave", item.url))
    }

    override fun manageLandscapeItemClick(pokemon: Pokemon) {
        /*secondaryFragment = fragment_content(pokemon)
        supportFragmentManager.beginTransaction().replace(R.id.mainsecond, secondaryFragment).commit()*/
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
                            result.getString("url")
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

}
