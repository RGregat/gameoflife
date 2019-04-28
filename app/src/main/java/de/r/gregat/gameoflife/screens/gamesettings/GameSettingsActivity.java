package de.r.gregat.gameoflife.screens.gamesettings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import de.r.gregat.gameoflife.R;
import de.r.gregat.gameoflife.screens.game.GameActivity;

public class GameSettingsActivity extends AppCompatActivity {

    private Button mStartSimulation;
    private EditText input_cell_size, input_activated_cells, input_evolving_time, input_rules_to_survive, input_rules_to_be_reborn;
    private CheckBox sandbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_cell_size = findViewById(R.id.input_cell_size);
        input_activated_cells = findViewById(R.id.input_activated_cells);
        input_evolving_time = findViewById(R.id.input_evolving_time);
        input_rules_to_survive = findViewById(R.id.input_rules_to_survive);
        input_rules_to_be_reborn = findViewById(R.id.input_rules_to_be_reborn);

        mStartSimulation = findViewById(R.id.button);
        mStartSimulation.setOnClickListener((v) -> {
            int cellSize = 0;
            int activatedCells = 0;
            int evolvingTime = 0;
            String rulesToSurvice = "23";
            String rulesToBeReborn = "3";

            Intent intent = new Intent(this, GameActivity.class);
            Bundle bundle = new Bundle();

            String cellSizeString = input_cell_size.getText().toString();
            if (!cellSizeString.isEmpty())
                cellSize = Integer.parseInt(cellSizeString);

            String activatedCellsString = input_activated_cells.getText().toString();
            if (!activatedCellsString.isEmpty())
                activatedCells = Integer.parseInt(activatedCellsString);

            String evolvingTimeString = input_evolving_time.getText().toString();
            if (!evolvingTimeString.isEmpty())
                evolvingTime = Integer.parseInt(evolvingTimeString);

            String rulesToSurviceTemp = input_rules_to_survive.getText().toString();
            if(!rulesToSurviceTemp.isEmpty()){
                rulesToSurvice = rulesToSurviceTemp;
            }

            String rulesToBeRebornTemp = input_rules_to_be_reborn.getText().toString();
            if(!rulesToBeRebornTemp.isEmpty()){
                rulesToBeReborn = rulesToBeRebornTemp;
            }

            bundle.putInt("cellSize", cellSize);
            bundle.putInt("activatedCells", activatedCells);
            bundle.putInt("evolvingTime", evolvingTime);
            bundle.putString("rulesToSurvive", rulesToSurvice);
            bundle.putString("rulesToBeReborn", rulesToBeReborn);

            intent.putExtras(bundle);

            startActivity(intent);
        });

        sandbox = findViewById(R.id.sandbox);
        sandbox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if(isChecked) {
                input_activated_cells.setEnabled(false);
                input_activated_cells.setText("0");
            } else {
                input_activated_cells.setEnabled(true);
            }
        });
    }
}
