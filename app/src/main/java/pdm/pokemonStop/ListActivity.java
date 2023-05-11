package pdm.pokemonStop;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    public static final Pokemon [] pokemons = {
            new Pokemon("Bulbasaur", "#001"),
            new Pokemon("Ivysaur", "#002"),
            new Pokemon("Venusaur", "#003"),
            new Pokemon("Charmander", "#004"),
            new Pokemon("Charmeleon","#005"),
            new Pokemon("Charizard","#006"),
            new Pokemon("Squirtle","#007"),
            new Pokemon("Wartortle", "#008"),
            new Pokemon("Blastoise","#009"),
            new Pokemon("Caterpie","#010"),
            new Pokemon("Metapod","#011"),
            new Pokemon("Butterfree","#012"),
            new Pokemon("Weedle","#013"),
            new Pokemon("Kakuna","#014"),
            new Pokemon("Metapod","#015"),
            new Pokemon("Pidgey","#016"),
            new Pokemon("Pidgeotto","#017"),
            new Pokemon("Pidgeot","#018"),
            new Pokemon("Rattata","#019"),
            new Pokemon("Raticate","#020"),
            new Pokemon("Spearow","#021"),
            new Pokemon("Fearow","#022"),
            new Pokemon("Ekans","#023"),
            new Pokemon("Arbok","#024"),
            new Pokemon("Pikachu", "#025")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedex_list);

        //Cria o botão de voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new PokemonAdapter(this,
                        R.layout.pokemon_listitem, pokemons
                )
        );

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
            intent.putExtra("name", pokemons[position].getName());
            startActivity(intent);
        });

    }

    //Método do botão de voltar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}





