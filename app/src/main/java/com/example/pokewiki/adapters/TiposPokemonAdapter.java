package com.example.pokewiki.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokewiki.R;
import com.example.pokewiki.Utils;
import com.example.pokewiki.models.TipoPokemon;
import com.example.pokewiki.views.ListaPokemonsActivity;

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

        if(tipo.getNome().equals("unknown") || tipo.getNome().equals("shadow")) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.imagem.getLayoutParams();
            int margemPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5,
                    resources.getDisplayMetrics()
            );
            params.setMargins(margemPx, margemPx, 0, margemPx);
            holder.imagem.setLayoutParams(params);
        }
        String nome = tipo.getNome();
        nome = Utils.primeiroCharMaiusculo(nome);
        holder.nome.setText(nome);
    }

    @Override
    public int getItemCount() {
        return tiposPokemons.size();
    }

    public void clear() {
        tiposPokemons.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<TipoPokemon> list) {
        tiposPokemons.addAll(list);
        notifyDataSetChanged();
    }

    private class TipoPokemonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RecyclerView recyclerView = context.findViewById(R.id.lista);
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
