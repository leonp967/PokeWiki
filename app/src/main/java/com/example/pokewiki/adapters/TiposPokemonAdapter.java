package com.example.pokewiki.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokewiki.ListaPokemonsActivity;
import com.example.pokewiki.R;
import com.example.pokewiki.models.TipoPokemon;

import java.util.List;

public class TiposPokemonAdapter extends RecyclerView.Adapter<TiposPokemonAdapter.TiposViewHolder>{
    private final Activity context;
    private List<TipoPokemon> tiposPokemons;
    private final View.OnClickListener onClickListener = new TipoPokemonListener();

    public class TiposViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagem;
        public TextView nome;

        public TiposViewHolder(View view) {
            super(view);
            imagem = view.findViewById(R.id.img_tipo);
            nome = view.findViewById(R.id.nome_tipo);
        }
    }

    public TiposPokemonAdapter(List<TipoPokemon> tiposPokemons, Activity context) {
        this.tiposPokemons = tiposPokemons;
        this.context = context;
    }

    @Override
    public TiposViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_tipos_layout, parent, false);
        itemView.setOnClickListener(onClickListener);
        return new TiposViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TiposViewHolder holder, int position) {
        TipoPokemon tipo = tiposPokemons.get(position);
        Resources resources =  context.getResources();
        final int idImagem = resources.getIdentifier(tipo.getNome(), "drawable" ,
                context.getPackageName());
        holder.imagem.setImageResource(idImagem);
        String nome = tipo.getNome();
        nome = nome.substring(0,1).toUpperCase() + nome.substring(1);
        holder.nome.setText(nome);
    }

    @Override
    public int getItemCount() {
        return tiposPokemons.size();
    }

    private class TipoPokemonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RecyclerView recyclerView = context.findViewById(R.id.lista_tipos);
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            TipoPokemon tipo = tiposPokemons.get(itemPosition);
            Intent intent = new Intent(context, ListaPokemonsActivity.class);
            intent.putExtra("URL", tipo.getUrl());
            TextView nomeTextView = view.findViewById(R.id.nome_tipo);
            intent.putExtra("NOME", nomeTextView.getText());
            context.startActivity(intent);
        }
    }
}
