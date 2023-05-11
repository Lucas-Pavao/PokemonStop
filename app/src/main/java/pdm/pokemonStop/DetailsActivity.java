package pdm.pokemonStop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Botão de voltar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializando elementos do layout

        //Imagem
        ImageView imageView = findViewById(R.id.pokemon_image);
        imageView.setImageResource(R.drawable.pokemon_image);

        //Nome
        TextView nomeTextView = findViewById(R.id.pokemon_name);
        nomeTextView.setText("Nome do Pokémon");

        //Número
        TextView numeroTextView = findViewById(R.id.pokemon_number);
        numeroTextView.setText("Número do Pokémon");

        //Tipo
        TextView tipoTextView = findViewById(R.id.pokemon_type);
        tipoTextView.setText("Tipo do Pokémon");

    }

    //Método do botão de voltar
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}