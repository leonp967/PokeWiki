package com.example.pokewiki.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokewiki.Dialogs;
import com.example.pokewiki.R;
import com.example.pokewiki.RestController;
import com.example.pokewiki.Utils;
import com.example.pokewiki.models.Pokemon;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PokemonActivity extends AppCompatActivity {

    private String urlPokemon;
    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            urlPokemon = extras.getString("URL");
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.barra_superior_layout);
            TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.nome_barra);
            textView.setText(getString(R.string.title_activity_pokemon));
        }
        if(Utils.existeConexaoInternet(PokemonActivity.this))
            popularDadosPokemon();
        else
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

    private void popularDadosPokemon() {
        RestController.get(urlPokemon, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pokemon = Utils.converterJsonPokemon(response.toString());

                String texto = getString(R.string.text_nome_pokemon, pokemon.getNome());
                ((TextView) findViewById(R.id.text_nome)).setText(Utils.getTextoNegritoIntervalo(texto));

                texto = getString(R.string.text_altura_pokemon, pokemon.getAltura() * 10);
                ((TextView) findViewById(R.id.text_altura)).setText(Utils.getTextoNegritoIntervalo(texto));

                texto = getString(R.string.text_peso_pokemon, pokemon.getPeso());
                ((TextView) findViewById(R.id.text_peso)).setText(Utils.getTextoNegritoIntervalo(texto));

                texto = getString(R.string.text_especie_pokemon, pokemon.getEspecie());
                ((TextView) findViewById(R.id.text_especie)).setText(Utils.getTextoNegritoIntervalo(texto));

                texto = getString(R.string.text_experiencia_pokemon, pokemon.getExperienciaBase());
                ((TextView) findViewById(R.id.text_experiencia)).setText(Utils.getTextoNegritoIntervalo(texto));

                texto = Utils.getListaConcatenada(pokemon.getHabilidades());
                texto = getString(R.string.text_habilidades_pokemon, texto);
                ((TextView) findViewById(R.id.lista_habilidades)).setText(Utils.getTextoNegritoIntervalo(texto));

                texto = Utils.getListaConcatenada(pokemon.getMovimentos());
                ((TextView) findViewById(R.id.lista_movimentos)).setText(texto);
                ((TextView) findViewById(R.id.lista_movimentos)).setMovementMethod(new ScrollingMovementMethod());
                findViewById(R.id.text_movimentos).setVisibility(View.VISIBLE);

                texto = Utils.getListaConcatenada(pokemon.getFormas());
                texto = getString(R.string.text_formas_pokemon, texto);
                ((TextView) findViewById(R.id.lista_formas)).setText(Utils.getTextoNegritoIntervalo(texto));

                texto = Utils.getListaConcatenada(pokemon.getTipos());
                texto = getString(R.string.text_tipos_pokemon, texto);
                ((TextView) findViewById(R.id.lista_tipos)).setText(Utils.getTextoNegritoIntervalo(texto));

                if(pokemon.getImagemUrl() != null){
                    RestController.get(pokemon.getImagemUrl(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            Bitmap imagem = BitmapFactory.decodeByteArray(response, 0, response.length);
                            ((ImageView) findViewById(R.id.img_pokemon)).setImageBitmap(imagem);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            Dialogs.mostrarDialogErro(R.string.erro_rest, PokemonActivity.this);
                        }
                    });
                }

                findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Dialogs.mostrarDialogErro(R.string.erro_rest, PokemonActivity.this);
            }
        });
    }

    private void resetarInterface() {
        findViewById(R.id.lista_movimentos).scrollTo(0, 0);
        ((TextView) findViewById(R.id.lista_movimentos)).setText("");
        ((ImageView) findViewById(R.id.img_pokemon)).setImageResource(R.drawable.pikachu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_refresh:
                findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
                if(Utils.existeConexaoInternet(PokemonActivity.this)) {
                    resetarInterface();
                    popularDadosPokemon();
                }
                else
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                break;
            case R.id.menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, pokemon.getResumo(this));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.text_share)));

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
