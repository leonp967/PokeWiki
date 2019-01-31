package com.example.pokewiki;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pokewiki.adapters.TiposPokemonAdapter;
import com.example.pokewiki.models.TipoPokemon;
import com.example.pokewiki.models.TiposPokemonHeader;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TiposPokemonsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView listaTipos;
    private RecyclerView.Adapter adapter;
    private final int PERMISSOES_REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.barra_superior_layout);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.nome_barra);
        textView.setText(getString(R.string.title_activity_lista_tipos));
        progressBar = findViewById(R.id.progress_bar);
        listaTipos = findViewById(R.id.lista_tipos);
        listaTipos.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listaTipos.setLayoutManager(layoutManager);
        verificarPermissoes();
        if(existeConexaoInternet())
            montarListaTipos();
    }

    private boolean existeConexaoInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void verificarPermissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE},
                    PERMISSOES_REQUEST_ID);
        }
    }

    private void montarListaTipos() {
        RestController.get("https://pokeapi.co/api/v2/type", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                TiposPokemonHeader header = RestController.converterParaClasse(response.toString(), TiposPokemonHeader.class);
                List<TipoPokemon> tiposPokemons = header.getTiposPokemons();
                adapter = new TiposPokemonAdapter(tiposPokemons, TiposPokemonsActivity.this);
                listaTipos.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                listaTipos.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSOES_REQUEST_ID: {
                for(int result : grantResults){
                     if(result != PackageManager.PERMISSION_GRANTED){
                        return;
                     }
                }
            }
        }
    }
}
