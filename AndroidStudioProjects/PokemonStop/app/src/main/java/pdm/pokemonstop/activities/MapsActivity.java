package pdm.pokemonstop.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

import pdm.pokemonstop.R;
import pdm.pokemonstop.databinding.ActivityMapsBinding;
import pdm.pokemonstop.model.Pokemon;
import pdm.pokemonstop.repository.PokemonRepository;
import pdm.pokemonstop.service.PokemonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int FINE_LOCATION_REQUEST = 0;
    private boolean fine_location;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestPermission();

        Button button = (Button) findViewById(R.id.pokedex_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(
                () -> {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
                    Toast.makeText(MapsActivity.this,
                            "Indo para a sua localização.", Toast.LENGTH_SHORT).show();
                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                    Task<Location> task = fusedLocationProviderClient.getLastLocation();
                    task.addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            // Definir o nível de zoom desejado (ajuste conforme necessário)
                            float zoomLevel = 20.0f;

                            // Movimentar a câmera para a localização atual do usuário com o zoom desejado
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel));
                        }
                    });

                    return false;
                });

        mMap.setOnMyLocationClickListener(
                location -> Toast.makeText(MapsActivity.this,
                        "Você está aqui!", Toast.LENGTH_SHORT).show());

        mMap.setMyLocationEnabled(this.fine_location);

        if (mMap.isMyLocationEnabled()) {

            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    // Mover a câmera do mapa para a localização atual do usuário
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                    // Definir o nível de zoom do mapa
                    float zoomLevel = 20.0f;
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));

                    // Gerar marcadores aleatórios próximos à local
                    double radius = 10; // Raio máximo em metros
                    int numMarkers = 5; // Número de marcadores a serem gerados

                    addRandomMarkers(userLocation, radius, numMarkers);
                }
            });
        }

        mMap.setOnMarkerClickListener(marker -> {


            Pokemon pokemon = (Pokemon) marker.getTag();
            View popupView = getLayoutInflater().inflate(R.layout.fragment_popup_layout, null);
            ImageView imageView = popupView.findViewById(R.id.pokemon_image);
            TextView textView = popupView.findViewById(R.id.pokemon_name);
            Button button = popupView.findViewById(R.id.capture_button);

            Glide.with(MapsActivity.this)
                    .asGif()
                    .load(pokemon.getSprites().getVersions().getGenerationV().getBlackWhite().getAnimated().getFront_default())
                    .apply(RequestOptions.overrideOf(128, 128))
                    .into(imageView); // substitua pela imagem do Pokémon
            textView.setText(pokemon.getName()); // substitua pelo nome do Pokémon

            button.setOnClickListener(v -> {
                // Coloque aqui o código para capturar o pokémon
                // ...
                popupWindow.dismiss(); // fecha a popup
            });

            PopupWindow popupWindow = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.popup_width), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);

            int imageSize = getResources().getDimensionPixelSize(R.dimen.popup_image_size);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageSize, imageSize);
            layoutParams.gravity = Gravity.CENTER; // Centralizar o ImageView
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            return false;
        });

    }

    private void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        this.fine_location = (permissionCheck == PackageManager.PERMISSION_GRANTED);
        if (this.fine_location) return;
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                FINE_LOCATION_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = (grantResults.length > 0) &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        this.fine_location = (requestCode == FINE_LOCATION_REQUEST) && granted;
        if (mMap != null) {
            mMap.setMyLocationEnabled(this.fine_location);
        }

    }

    private void addRandomMarkers(LatLng userLocation, double radius, int numMarkers) {
        Random random = new Random();

        for (int i = 0; i < numMarkers; i++) {
            // Gerar um deslocamento aleatório em metros dentro do raio especificado
            double displacement = radius * Math.sqrt(random.nextDouble());
            double angle = 2 * Math.PI * random.nextDouble();

            // Calcular as coordenadas do marcador com base no deslocamento e na localização atual do usuário
            double latitude = userLocation.latitude + displacement * Math.cos(angle) / 111111;
            double longitude = userLocation.longitude + displacement * Math.sin(angle) / (111111 * Math.cos(userLocation.latitude));

            // Criar um objeto LatLng com as coordenadas geradas
            LatLng markerLocation = new LatLng(latitude, longitude);

            getPokemonImageUrl(markerLocation);

        }


    }

    private void getPokemonImageUrl(LatLng markerLocation) {
        Random random = new Random();
        int id = random.nextInt(31) + 1;
        final PokemonService apiService = PokemonRepository.getClient().create(PokemonService.class);
        Call<Pokemon> call = apiService.getPokemon(id);

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon pokemon;

                if (response.isSuccessful()) {
                    pokemon = response.body();

                    Glide.with(MapsActivity.this)
                            .asGif()
                            .load(pokemon.getSprites().getVersions().getGenerationV().getBlackWhite().getAnimated().getFront_default())
                            .into(new CustomTarget<GifDrawable>() {
                                @Override
                                public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                                    int markerSize = 128;
                                    Bitmap bitmap = Bitmap.createBitmap(markerSize, markerSize, Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(bitmap);
                                    resource.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                                    resource.draw(canvas);
                                    if (pokemon != null) {
                                        // Definir o objeto pokemon como tag do marcador
                                        MarkerOptions markerOptions = new MarkerOptions()
                                                .position(markerLocation)
                                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                     Marker marker =   mMap.addMarker(markerOptions);
                                     marker.setTag(pokemon);

                                    } else {
                                        // Tratar resposta não bem sucedida
                                        Toast.makeText(MapsActivity.this, "Erro ao receber informações do Pokémon", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });


                }


            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                // Tratar falha na chamada API
                Toast.makeText(MapsActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });

    }


}