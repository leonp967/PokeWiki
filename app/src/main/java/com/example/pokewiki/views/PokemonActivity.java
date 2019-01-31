package com.example.pokewiki.views;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.pokewiki.R;

public class PokemonActivity extends AppCompatActivity {

    private String urlPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            urlPokemon = extras.getString("URL");
            String nome = extras.getString("NOME");
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.barra_superior_layout);
            TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.nome_barra);
            textView.setText(getString(R.string.title_activity_pokemon, nome));
        }
    }
}
