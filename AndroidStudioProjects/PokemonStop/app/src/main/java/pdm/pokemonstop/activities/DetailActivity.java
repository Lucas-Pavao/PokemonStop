package pdm.pokemonstop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import pdm.pokemonstop.R;
import pdm.pokemonstop.model.Pokemon;
import pdm.pokemonstop.repository.PokemonRepository;
import pdm.pokemonstop.service.PokemonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvTypes;
    private ImageView ivPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        Toast.makeText(DetailActivity.this, i.getIntExtra("ID", 0) + "", Toast.LENGTH_SHORT).show();

        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvTypes = (TextView) findViewById(R.id.tv_detail_types);

        ivPokemon = (ImageView) findViewById(R.id.iv_detail_pokemon);

        int id = getIntent().getIntExtra("ID", 0);
        requestData(id);

    }

    private void requestData(int id) {
        final PokemonService apiService = PokemonRepository.getClient().create(PokemonService.class);

        Call<Pokemon> call = apiService.getPokemon(id);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon pokemon;

                if (response.isSuccessful()) {
                    pokemon = response.body();

                    tvName.setText(pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1));
                    tvTypes.setText(pokemon.getTypesToString());


                    Picasso.get()
                            .load(pokemon.getSprites().getFrontDefault())
                            .resize(128, 128)
                            .into(ivPokemon);

                }


            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {

            }
        });
    }
}