package pdm.pokemonStop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.graphics.Color;
import android.location.Location;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.Random;

import pdm.pokemonStop.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int FINE_LOCATION_REQUEST = 100;
    private boolean fine_location;
    private PopupWindow popupWindow;
//    private Circle userCircle;


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

    private void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        this.fine_location = (permissionCheck == PackageManager.PERMISSION_GRANTED);
        if (this.fine_location) return;
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                FINE_LOCATION_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = (grantResults.length > 0) &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        this.fine_location = (requestCode == FINE_LOCATION_REQUEST) && granted;

        if (mMap != null) {
            mMap.setMyLocationEnabled(this.fine_location);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        popupWindow = new PopupWindow(this);

        //Código popup
        mMap.setOnMyLocationClickListener(location -> {
            View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

            ImageView imageView = popupView.findViewById(R.id.pokemon_image);
            TextView textView = popupView.findViewById(R.id.pokemon_name);
            Button button = popupView.findViewById(R.id.capture_button);

            imageView.setImageResource(R.drawable.pokemon_image); // substitua pela imagem do Pokémon
            textView.setText("Nome do Pokémon"); // substitua pelo nome do Pokémon

            button.setOnClickListener(v -> {
                // Coloque aqui o código para capturar o pokémon
                // ...
                popupWindow.dismiss(); // fecha a popup
            });

            PopupWindow popupWindow = new PopupWindow(popupView, getResources().getDimensionPixelSize(R.dimen.popup_width), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
        });

        mMap.setOnMyLocationButtonClickListener(
                () -> {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
                    Toast.makeText(MapsActivity.this,
                            "Indo para a sua localização", Toast.LENGTH_SHORT).show();
                    // Obter a última localização conhecida do usuário
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
                    float zoomLevel = 15.0f;
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));

                    // Gerar marcadores aleatórios próximos à local
                    double radius = 10; // Raio máximo em metros
                    int numMarkers = 5; // Número de marcadores a serem gerados

                    addRandomMarkers(userLocation, radius, numMarkers);
                }
            });
        }



            // Definir um Listener para quando a localização do usuário mudar
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    // Obter a localização atual do usuário
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Criar um objeto LatLng para a localização atual
                    LatLng userLocation = new LatLng(latitude, longitude);

                    // Mover a câmera do mapa para a localização atual do usuário
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                    // Definir o nível de zoom do mapa
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));

                    // Remover o Listener para evitar chamadas repetidas
                    mMap.setOnMyLocationChangeListener(null);

//                    if (userCircle != null) {
//                        userCircle.remove();
//                    }
//                    drawCircle(userLocation);
                    currentLocation();
                }
            });
        }





//    private void drawCircle(LatLng point){
//
//        // Instantiating CircleOptions to draw a circle around the marker
//        CircleOptions circleOptions = new CircleOptions();
//
//        // Specifying the center of the circle
//        circleOptions.center(point);
//
//        // Radius of the circle
//        circleOptions.radius(20);
//
//        // Border color of the circle
//        circleOptions.strokeColor(Color.BLUE);
//
//        // Fill color of the circle
//        circleOptions.fillColor(Color.parseColor("#500000FF"));
//
//        // Border width of the circle
//        circleOptions.strokeWidth(2);
//
//        // Adding the circle to the GoogleMap
//       userCircle = mMap.addCircle(circleOptions);
//
//    }

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

            // Adicionar o marcador no mapa
            mMap.addMarker(new MarkerOptions().position(markerLocation));
        }
    }

    public void currentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
//            if (location != null) {
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                // Verificar se o círculo inicial já foi criado
//                if (userCircle == null) {
//                    drawCircle(latLng);
//                } else {
//                    // Atualizar a posição do círculo inicial
//                    userCircle.setCenter(latLng);
//                }
//            }
        });

    }



}