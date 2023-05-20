package pdm.pokemonStop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import pdm.pokemonStop.model.Pokemon;

public class PokemonAdapter extends ArrayAdapter<Pokemon> {
    private Pokemon [] pokemons;

    public PokemonAdapter(Context context, int resource, Pokemon [] pokemons) {
        super(context, resource, pokemons);
        this.pokemons = pokemons;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View listItem = null;
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItem = inflater.inflate(R.layout.pokemon_listitem, null, true);
            holder = new ViewHolder();
            holder.pokemonName = listItem.findViewById(R.id.pokemon_name);
            holder.pokemonNumber = listItem.findViewById(R.id.pokemon_number);
            listItem.setTag(holder);
        } else {
            listItem = view;
            holder = (ViewHolder)view.getTag();
        }
        holder.pokemonName.setText(pokemons[position].getName());
        holder.pokemonNumber.setText(pokemons[position].getNumber());
        return listItem;
    }
}
