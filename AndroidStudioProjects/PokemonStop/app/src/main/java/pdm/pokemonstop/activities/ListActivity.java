package pdm.pokemonstop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pdm.pokemonstop.R;
import pdm.pokemonstop.RecyclerTouchListener;
import pdm.pokemonstop.model.Pokemon;
import pdm.pokemonstop.model.PokemonAdapter;
import pdm.pokemonstop.repository.PokemonRepository;
import pdm.pokemonstop.service.PokemonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    List<Pokemon> pokemonList = new ArrayList<>();
    RecyclerView recyclerView;
    PokemonAdapter pokemonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.rv_pokemons);

        pokemonAdapter = new PokemonAdapter(pokemonList);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pokemonAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(ListActivity.this, DetailActivity.class);
                i.putExtra("ID", pokemonList.get(position).getId());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        addData();
    }

    private void addData() {
        PokemonService pokemonService = PokemonRepository.getClient().create(PokemonService.class);

        for(int i = 1; i <= 30; i++) {
            Call<Pokemon> call = pokemonService.getPokemon(i);
            call.enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    if(response.isSuccessful()) {
                        Pokemon pokemon = response.body();

                        pokemonList.add(pokemon);
                        Collections.sort(pokemonList, new Comparator<Pokemon>() {
                            @Override
                            public int compare(Pokemon p1, Pokemon p2) {
                                return Integer.compare(p1.getId(), p2.getId());
                            }
                        });
                        pokemonAdapter.notifyDataSetChanged();

                        Log.i("POKEMON", "Name: " + pokemon.getName());
                        Log.i("POKEMON", "Numero: " + pokemon.getId());
                        Log.i("POKEMON", "Height: " + pokemon.getHeight());
                        Log.i("POKEMON", "Weight: " + pokemon.getWeight());

                    }

                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                }
            });
        }
    }
}