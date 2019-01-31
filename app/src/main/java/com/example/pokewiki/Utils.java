package com.example.pokewiki;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.pokewiki.models.Estatistica;
import com.example.pokewiki.models.Pokemon;
import com.example.pokewiki.models.PokemonHeader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static Gson gson = new Gson();

    public static boolean existeConexaoInternet(Activity tela, SwipeRefreshLayout swipeRefreshLayout) {
        ConnectivityManager cm = (ConnectivityManager) tela.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConectado = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConectado){
            swipeRefreshLayout.setRefreshing(false);
            Dialogs.mostrarDialogErro(R.string.erro_internet, tela);
        }
        return isConectado;
    }

    public static void verificarPermissoes(Activity tela) {
        if (ContextCompat.checkSelfPermission(tela, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(tela, Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(tela,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE},
                    Constantes.PERMISSOES_REQUEST_ID);
        }
    }

    public static String primeiroCharMaiusculo(String string){
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    private static List<String> getNomeProfundidade(JsonObject jobject, String atributoExterno, String atributoInterno){
        List<String> lista = new ArrayList<>();
        JsonArray abilities = jobject.getAsJsonArray(atributoExterno);
        for(JsonElement element : abilities){
            if(element instanceof JsonObject) {
                if(atributoInterno != null) {
                    JsonObject interno = (JsonObject) ((JsonObject) element).get(atributoInterno);
                    lista.add(primeiroCharMaiusculo(interno.get("name").getAsString()));
                } else
                    lista.add(primeiroCharMaiusculo(((JsonObject) element).get("name").getAsString()));
            }
        }
        return lista;
    }

    public static <T> T converterParaClasse(String json, Class classe){
        return (T) gson.fromJson(json, classe);
    }

    public static List<PokemonHeader> processarPokemons(JSONObject jsonResponse) {
        List<PokemonHeader> pokemons = new ArrayList<>();
        JSONArray array;
        try {
            array = jsonResponse.getJSONArray("pokemon");
            for(int i = 0; i < array.length(); i++){
                JSONObject objeto = array.getJSONObject(i);
                PokemonHeader header = gson.fromJson(objeto.getJSONObject("pokemon").toString(), PokemonHeader.class);
                pokemons.add(header);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pokemons;
    }

    public static Pokemon converterJsonPokemon(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Pokemon.class, new PokemonDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, Pokemon.class);
    }

    private static class PokemonDeserializer implements JsonDeserializer<Pokemon> {

        @Override
        public Pokemon deserialize(JsonElement json, Type type,
                                   JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            JsonObject jobject = (JsonObject) json;
            Pokemon pokemon = gson.fromJson(jobject.toString(), Pokemon.class);

            pokemon.setAltura(jobject.get("height").getAsInt());
            pokemon.setExperienciaBase(jobject.get("base_experience").getAsInt());
            pokemon.setNome(primeiroCharMaiusculo(jobject.get("name").getAsString()));
            pokemon.setPeso(jobject.get("weight").getAsInt());
            pokemon.setHabilidades(getNomeProfundidade(jobject, "abilities", "ability"));
            pokemon.setFormas(getNomeProfundidade(jobject, "forms", null));
            pokemon.setMovimentos(getNomeProfundidade(jobject, "moves", "move"));
            pokemon.setEspecie(primeiroCharMaiusculo(((JsonObject) jobject.get("species")).get("name").getAsString()));

            JsonElement urlElement = ((JsonObject) jobject.get("sprites")).get("front_default");
            if(urlElement != null && !urlElement.getAsString().equals("null"))
                pokemon.setImagemUrl(urlElement.getAsString());

            List<Estatistica> estatisticas = new ArrayList<>();
            JsonArray stats = jobject.getAsJsonArray("stats");
            for(JsonElement element : stats){
                Estatistica estatistica = new Estatistica();
                JsonObject objetoJson = ((JsonObject) element);
                estatistica.setValorBase(objetoJson.get("base_stat").getAsInt());
                String nome = ((JsonObject) objetoJson.get("stat")).get("name").getAsString();
                estatistica.setNome(primeiroCharMaiusculo(nome));
                estatisticas.add(estatistica);
            }
            pokemon.setEstatisticas(estatisticas);
            pokemon.setTipos(getNomeProfundidade(jobject, "types", "type"));
            return pokemon;
        }
    }
}