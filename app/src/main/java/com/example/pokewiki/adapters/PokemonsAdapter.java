package com.example.pokewiki.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokewiki.R;
import com.example.pokewiki.models.PokemonHeader;
import com.example.pokewiki.views.PokemonActivity;

import java.util.List;

public class PokemonsAdapter extends RecyclerView.Adapter<PokemonsAdapter.PokemonsViewHolder>{
    private final Activity context;
    private List<PokemonHeader> pokemons;
    private final View.OnClickListener onClickListener = new PokemonListener();

    public class PokemonsViewHolder extends RecyclerView.ViewHolder {
        public TextView nome;

        public PokemonsViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.nome_pokemon);
        }
    }

    public PokemonsAdapter(List<PokemonHeader> pokemons, Activity context) {
        this.pokemons = pokemons;
        this.context = context;
    }

    @Override
    public PokemonsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_pokemons_layout, parent, false);
        itemView.setOnClickListener(onClickListener);
        return new PokemonsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PokemonsViewHolder holder, int position) {
        PokemonHeader pokemon = pokemons.get(position);
        String nome = pokemon.getNome();
        nome = nome.substring(0,1).toUpperCase() + nome.substring(1);
        holder.nome.setText(nome);
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void clear() {
        pokemons.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<PokemonHeader> list) {
        pokemons.addAll(list);
        notifyDataSetChanged();
    }

    private class PokemonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RecyclerView recyclerView = context.findViewById(R.id.lista);
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            PokemonHeader pokemon = pokemons.get(itemPosition);
            Intent intent = new Intent(context, PokemonActivity.class);
            intent.putExtra("URL", pokemon.getUrl());
            TextView nomeTextView = view.findViewById(R.id.nome_pokemon);
            intent.putExtra("NOME", nomeTextView.getText());
            context.startActivity(intent);
        }
    }
}
