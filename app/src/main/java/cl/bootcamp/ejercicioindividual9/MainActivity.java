package cl.bootcamp.ejercicioindividual9;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import cl.bootcamp.ejercicioindividual9.databinding.ActivityMainBinding;
import cl.bootcamp.ejercicioindividual9.fragments.BulbasaurFragment;
import cl.bootcamp.ejercicioindividual9.fragments.CharmanderFragment;
import cl.bootcamp.ejercicioindividual9.fragments.DefaultFragment;
import cl.bootcamp.ejercicioindividual9.fragments.SquirtleFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inicializando ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Configuracion inicial
        loadFragment(new DefaultFragment());
        binding.cancelButton.setVisibility(View.GONE);
        binding.confirmButton.setVisibility(View.GONE);

        //listener para el boton elegir
        binding.validateButton.setOnClickListener(v ->  {
            int selectId = binding.pokemonGroup.getCheckedRadioButtonId();
            if (selectId == -1) {
                Toast.makeText(MainActivity.this, "¡Selecciona un Pokémon!", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedPokemon = findViewById(selectId);
                String pokemonName = selectedPokemon.getText().toString();
                updateBackgroundFragment(pokemonName);
                showPokemonDialog(pokemonName);

                binding.validateButton.setVisibility(View.GONE);
                binding.cancelButton.setVisibility(View.VISIBLE);
                binding.confirmButton.setVisibility(View.VISIBLE);
            }
        });

        //listener para el boton cancelar
        binding.cancelButton.setOnClickListener (v -> {
            loadFragment(new DefaultFragment());
            binding.validateButton.setVisibility(View.VISIBLE);
            binding.cancelButton.setVisibility(View.GONE);
            binding.confirmButton.setVisibility(View.GONE);
        });

        //listener para el boton confirmar
        binding.confirmButton.setOnClickListener (v -> {
            Toast.makeText(MainActivity.this, "¡Pokémon confirmado!", Toast.LENGTH_SHORT).show();
        });
    }

    private void showPokemonDialog(String pokemonName) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pokemon, null);
        builder.setView(dialogView);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.findViewById(R.id.dialog_close_button).setOnClickListener(v -> dialog.dismiss());

        TextView title = dialogView.findViewById(R.id.dialog_title);
        TextView pokemonSelected = dialogView.findViewById(R.id.dialog_pokemon_name);

        title.setText("Pokémon Seleccionado");
        pokemonSelected.setText(pokemonName);

        if (pokemonName.equalsIgnoreCase("Charmander")) {
            pokemonSelected.setTextColor(getColorCompat(R.color.charmander));
        } else if (pokemonName.equalsIgnoreCase("Bulbasaur")) {
            pokemonSelected.setTextColor(getColorCompat(R.color.bulbasaur));
        } else if (pokemonName.equalsIgnoreCase("Squirtle")) {
            pokemonSelected.setTextColor(getColorCompat(R.color.squirtle));
        }
        dialog.show();
    }

    private void updateBackgroundFragment(String pokemonName) {
        Fragment fragment;

        switch (pokemonName.toLowerCase()) {
            case "charmander":
                fragment = new CharmanderFragment();
                break;
            case "bulbasaur":
                fragment = new BulbasaurFragment();
                break;
            case "squirtle":
                fragment = new SquirtleFragment();
                break;
            default:
                fragment = new DefaultFragment();
        }
        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.fragmentContainer.getId(), fragment);
        transaction.commit();
    }

    private int getColorCompat(int colorResId) {
        return getResources().getColor(colorResId, getTheme());
    }
}