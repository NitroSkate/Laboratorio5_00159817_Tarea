package com.example.pokedexre.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokedexre.R
import com.example.pokedexre.pojo.Pokemon


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class list_fragment : Fragment() {
    var listaP = mutableListOf<Pokemon>()


    companion object {
        fun newInstance (lista : MutableList<Pokemon>) : list_fragment{
            var pokeFrag = list_fragment()
            pokeFrag.listaP = lista
            return pokeFrag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_fragment, container, false)
    }


}
