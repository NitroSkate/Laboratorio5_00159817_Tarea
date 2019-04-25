package com.example.pokedexre.fragments


import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokedexre.R
import com.example.pokedexre.pojo.Pokemon
import com.example.pokedexre.utilities.NetworkUtils
import kotlinx.android.synthetic.main.fragment_fragment_content.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class fragment_content : Fragment() {

    var pokemon = Pokemon()

    companion object {
        fun newInstance(pokemon: Pokemon): fragment_content{
            val newFragment = fragment_content()
            newFragment.pokemon = pokemon
            return newFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fragment_content, container, false)
        bindData(view)
        return view
    }


    fun bindData(view: View){
            view.t1.text = pokemon.nombre
            view.t2.text = pokemon.ftype
            view.t3.text = pokemon.stype
            view.t4.text = pokemon.weight + " "  + "g"
    }


}
