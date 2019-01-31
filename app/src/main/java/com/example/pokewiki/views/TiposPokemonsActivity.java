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
import android.widget.TextView;

import com.example.pokewiki.Constantes;
import com.example.pokewiki.Dialogs;
import com.example.pokewiki.R;
import com.example.pokewiki.RestController;
import com.example.pokewiki.Utils;
import com.example.pokewiki.adapters.TiposPokemonAdapter;
import com.example.pokewiki.models.TipoPokemon;
import com.example.pokewiki.models.TiposPokemonHeader;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TiposPokemonsActivity extends AppCompatActivity {

    private RecyclerView listaTipos;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.barra_superior_layout);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.nome_barra);
        textView.setText(getString(R.string.title_activity_lista_tipos));
        listaTipos = findViewById(R.id.lista);
        listaTipos.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listaTipos.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(Utils.existeConexaoInternet(TiposPokemonsActivity.this, swipeRefreshLayout))
                            montarListaTipos();
                    }
                }
        );
        Utils.verificarPermissoes(this);
        if(Utils.existeConexaoInternet(TiposPokemonsActivity.this, swipeRefreshLayout))
            montarListaTipos();
    }

    private void montarListaTipos() {
        RestController.get("https://pokeapi.co/api/v2/type", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(adapter != null){
                    ((TiposPokemonAdapter)adapter).clear();
                }
                TiposPokemonHeader header = Utils.converterParaClasse(response.toString(), TiposPokemonHeader.class);
                List<TipoPokemon> tiposPokemons = header.getTiposPokemons();
                if(adapter == null) {
                    adapter = new TiposPokemonAdapter(tiposPokemons, TiposPokemonsActivity.this);
                    listaTipos.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else
                    ((TiposPokemonAdapter)adapter).addAll(tiposPokemons);

                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
                if(Utils.existeConexaoInternet(TiposPokemonsActivity.this, swipeRefreshLayout))
                    montarListaTipos();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
