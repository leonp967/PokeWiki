package com.example.pokewiki.views;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pokewiki.Constantes;
import com.example.pokewiki.Dialogs;
import com.example.pokewiki.R;
import com.example.pokewiki.RestController;
import com.example.pokewiki.Utils;
import com.example.pokewiki.adapters.PokemonsAdapter;
import com.example.pokewiki.models.PokemonHeader;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ListaPokemonsActivity extends AppCompatActivity {

    private String urlPokemons;
    private RecyclerView listaPokemons;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            urlPokemons = extras.getString("URL");
            String nome = extras.getString("NOME");
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.barra_superior_layout);
            TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.nome_barra);
            textView.setText(getString(R.string.title_activity_lista_pokemons, nome));
        }
        listaPokemons = findViewById(R.id.lista);
        listaPokemons.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listaPokemons.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(Utils.existeConexaoInternet(ListaPokemonsActivity.this))
                montarListaPokemons();
            else
                swipeRefreshLayout.setRefreshing(false);
        });
        if(Utils.existeConexaoInternet(ListaPokemonsActivity.this))
            montarListaPokemons();
        else
            swipeRefreshLayout.setRefreshing(false);
    }

    private void montarListaPokemons() {
        RestController.get(urlPokemons,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(adapter != null){
                    ((PokemonsAdapter)adapter).clear();
                }
                List<PokemonHeader> tiposPokemons = Utils.processarPokemons(response);
                if(tiposPokemons.isEmpty()) {
                    findViewById(R.id.text_erro).setVisibility(View.VISIBLE);
                    findViewById(R.id.img_pokebola).setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                if(adapter == null) {
                    adapter = new PokemonsAdapter(tiposPokemons, ListaPokemonsActivity.this);
                    listaPokemons.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else
                    ((PokemonsAdapter)adapter).addAll(tiposPokemons);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Dialogs.mostrarDialogErro(R.string.erro_rest, ListaPokemonsActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                if(Utils.existeConexaoInternet(ListaPokemonsActivity.this))
                    montarListaPokemons();
                else
                    swipeRefreshLayout.setRefreshing(false);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constantes.PERMISSOES_REQUEST_ID: {
                for(int result : grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        Dialogs.mostrarDialogErro(R.string.erro_permissoes, this);
                        return;
                    }
                }
            }
        }
    }
}
