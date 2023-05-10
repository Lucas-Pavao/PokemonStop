package pdm.pokemonStop;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    private static final String [] pokemons = {"Bulbasaur", "Ivysaur", "Venusaur",
            "Charmander", "Charmeleon", "Charizard", "Squirtle", "Wartortle",
            "Blastoise", "Caterpie", "Metapod","Butterfree",
            "Weedle", "Kakuna"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedex_list);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new ArrayAdapter<>(this,
                R.layout.pokemon_listitem,
                R.id.pokemon_name, pokemons
                )
        );

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(parent.getContext(), DetailsActivity.class);
            intent.putExtra("pokemon", pokemons[position]);
            startActivity(intent);
        });

    }
}





