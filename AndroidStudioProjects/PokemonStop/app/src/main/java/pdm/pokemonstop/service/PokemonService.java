package pdm.pokemonstop.service;

import pdm.pokemonstop.model.Pokemon;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokemonService {

    @GET("pokemon/{id}/")
    Call<Pokemon> getPokemon(@Path("id") int id);
}
