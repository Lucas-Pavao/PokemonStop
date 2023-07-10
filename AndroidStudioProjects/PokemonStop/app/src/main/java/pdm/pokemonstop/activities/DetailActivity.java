package pdm.pokemonstop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    private BarChart barChart;

    private  int primaryTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        Toast.makeText(DetailActivity.this, i.getIntExtra("ID", 0) + "", Toast.LENGTH_SHORT).show();

        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvTypes = (TextView) findViewById(R.id.tv_detail_types);

        ivPokemon = (ImageView) findViewById(R.id.iv_detail_pokemon);

        barChart = findViewById(R.id.barChart_comparison);

        int[] attrs = new int[]{android.R.attr.textColorPrimary};
        TypedArray typedArray = obtainStyledAttributes(R.style.Theme_PokemonStop, attrs);
        primaryTextColor = typedArray.getColor(0, Color.BLACK);
        typedArray.recycle();

        int id = getIntent().getIntExtra("ID", 0);
        requestData(id);

        getSupportActionBar().hide();

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

                    Glide.with(DetailActivity.this)
                            .asGif()
                            .load(pokemon.getSprites().getVersions().getGenerationV().getBlackWhite().getAnimated().getFront_default())
                            .apply(RequestOptions.overrideOf(128, 128))
                            .into(ivPokemon);

                    // Dados para a comparação de altura
                    float pokemonHeight = pokemon.getHeight();
                    float averageHeightAdult = 180f;

                    setupBarChart(pokemonHeight * 10, averageHeightAdult);

                }



            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {

            }
        });
    }

    private void setupBarChart(float pokemonHeight, float averageHeightAdult) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, pokemonHeight));
        entries.add(new BarEntry(1f, averageHeightAdult));

        BarDataSet dataSet = new BarDataSet(entries, "Comparação de altura");

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.4f);

        barChart.setData(data);
        barChart.setFitBars(true);
        dataSet.setColors(Color.GREEN, Color.BLUE);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        xAxis.setTextColor(primaryTextColor);
        leftAxis.setTextColor(primaryTextColor);
        rightAxis.setTextColor(primaryTextColor);
        dataSet.setValueTextColor(primaryTextColor);




        // Criar as legendas personalizadas
        LegendEntry legendEntry1 = new LegendEntry();
        legendEntry1.label = "Altura do Pokémon";
        legendEntry1.formColor = Color.GREEN;

        LegendEntry legendEntry2 = new LegendEntry();
        legendEntry2.label = "Altura média do adulto";
        legendEntry2.formColor = Color.BLUE;

        // Adicionar as legendas personalizadas à LinearLayout
        LinearLayout legendLayout = findViewById(R.id.legendLayout);
        addLegendEntry(legendLayout, legendEntry1);
        addLegendEntry(legendLayout, legendEntry2);


        animateBars();

        barChart.invalidate();
    }

    private void addLegendEntry(LinearLayout legendLayout, LegendEntry entry) {
        View legendView = getLayoutInflater().inflate(R.layout.legend_entry, legendLayout, false);
        TextView labelTextView = legendView.findViewById(R.id.legendLabel);
        ImageView iconImageView = legendView.findViewById(R.id.legendIcon);
        View blockView = legendView.findViewById(R.id.legendBlock);

        labelTextView.setText(entry.label);
        iconImageView.setColorFilter(entry.formColor);
        blockView.setBackgroundColor(entry.formColor);

        legendLayout.addView(legendView);
    }
    private void animateBars() {
        barChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
    }
}