package com.example.pokewiki;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class ListaPokemonsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pokemons);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString("URL");
            String nome = extras.getString("NOME");
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.barra_superior_layout);
            TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.nome_barra);
            textView.setText(getString(R.string.title_activity_lista_pokemons, nome));
            Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        }
    }

}
