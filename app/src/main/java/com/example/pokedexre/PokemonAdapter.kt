package com.example.pokedexre

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import com.example.pokedexre.pojo.Pokemon
import kotlinx.android.synthetic.main.elements.view.*

class PokemonAdapter (val items : List<Pokemon>, val clickListener: (Pokemon) -> Unit) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.elements, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], clickListener)



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Pokemon, clickListener: (Pokemon) -> Unit) = with(itemView){
            pokemon_name.text = item.nombre
            pokemon_type.text = item.tipo
            this.setOnClickListener{clickListener(item)}
        }
    }

}