package com.example.pokedexre.fragments


import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokedexre.PokemonAdapter
import com.example.pokedexre.R
import com.example.pokedexre.pojo.AppConstants
import com.example.pokedexre.pojo.Pokemon
import kotlinx.android.synthetic.main.fragment_list_fragment.view.*
import java.lang.RuntimeException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 *
 */
class list_fragment : Fragment() {

    private lateinit var pokemons : ArrayList<Pokemon>
    private lateinit var pokemonAdapter: PokemonAdapter

    var ListenerTool : PokeListener? = null


    companion object {
        fun newInstance (lista : ArrayList<Pokemon>) : list_fragment{
            var pokeFrag = list_fragment()
            pokeFrag.pokemons = lista
            return pokeFrag
        }
    }

    interface PokeListener{
        fun managePortraitItemClick(pokemon: Pokemon)
        fun manageLandscapeItemClick(pokemon: Pokemon)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_list_fragment, container, false)
        if(savedInstanceState != null) pokemons = savedInstanceState.getParcelableArrayList<Pokemon>(AppConstants.MAIN_LIST_KEY)
        initRecyclerView(resources.configuration.orientation, view)
        return view
    }

    fun initRecyclerView(orientation: Int, container: View){
        val linearLayoutManager = LinearLayoutManager(this.context)

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            pokemonAdapter = PokemonAdapter(pokemons, {pokemon:Pokemon->ListenerTool?.managePortraitItemClick(pokemon)})
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            pokemonAdapter = PokemonAdapter(pokemons, {pokemon:Pokemon ->ListenerTool?.manageLandscapeItemClick(pokemon)})
        }
        container.recycler_view.adapter = pokemonAdapter as PokemonAdapter

        container.recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PokeListener) {
            ListenerTool = context
        } else {
            //throw RuntimeException("Se necesita una implementacion de la interfaz")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.MAIN_LIST_KEY, pokemons)
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        ListenerTool = null
    }


}
