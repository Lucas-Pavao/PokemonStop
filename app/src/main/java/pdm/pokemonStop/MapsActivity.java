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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import pdm.pokemonStop.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int FINE_LOCATION_REQUEST = 100;
    private boolean fine_location;
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
                    Toast.makeText(MapsActivity.this,
                            "Indo para a sua localização", Toast.LENGTH_SHORT).show();
                    return false;
                });
        mMap.setMyLocationEnabled(this.fine_location);

    }



}