package de.r.gregat.gameoflife.screens.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.r.gregat.gameoflife.R;
import de.r.gregat.gameoflife.engine.GameOfLifeView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private GameOfLifeView mGameOfLifeView;
    private ImageButton image_button_play_control, image_button_restart, image_button_speed_up, image_button_speed_down;
    private TextView toolbar_text;
    private boolean isRunning = false;
    private boolean firstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        int cellSize = bundle.getInt("cellSize");
        int activatedCells = bundle.getInt("activatedCells");
        int evolvingTime = bundle.getInt("evolvingTime");
        String rulesToSurvive = bundle.getString("rulesToSurvive");
        String rulesToBeReborn = bundle.getString("rulesToBeReborn");

        setContentView(R.layout.game_layout);

        image_button_play_control = findViewById(R.id.image_button_play_control);
        image_button_restart = findViewById(R.id.image_button_restart);
        image_button_speed_up = findViewById(R.id.image_button_speed_up);
        image_button_speed_down = findViewById(R.id.image_button_speed_down);

        /*toolbar_text = findViewById(R.id.toolbar_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar_text!=null && toolbar!=null) {
            toolbar_text.setText("Generation 0");
            setSupportActionBar(toolbar);
        }*/


        image_button_play_control.setOnClickListener(this);
        image_button_restart.setOnClickListener(this);
        image_button_speed_up.setOnClickListener(this);
        image_button_speed_down.setOnClickListener(this);

        mGameOfLifeView = findViewById(R.id.game_of_life_view);
        mGameOfLifeView.setmListener(new GameOfLifeView.Listener() {
            @Override
            public void generationNumber(int generation) {
                //toolbar_text.setText("Generation " + generation);
            }
        });

        List<Integer> rulesToSurviveList = parseRuleSet(rulesToSurvive);
        List<Integer> rulesToBeRebornList = parseRuleSet(rulesToBeReborn);

        if (cellSize > 0)
            mGameOfLifeView.setCellSize(cellSize);

        mGameOfLifeView.setActivatedCells(activatedCells);

        if (evolvingTime > 0)
            mGameOfLifeView.setEvolvingTime(evolvingTime);

        mGameOfLifeView.setRulesToSurvive(rulesToSurviveList);
        mGameOfLifeView.setRulesToBeReborn(rulesToBeRebornList);
    }

    private List<Integer> parseRuleSet(String ruleSetString) {
        List<Integer> ruleSetList = new ArrayList<>();

        int characterCount = ruleSetString.length();

        int idxBefore = 0;
        for(int i = 1; i <= characterCount; i++) {
            ruleSetList.add(Integer.valueOf(ruleSetString.substring(idxBefore, i)));
            idxBefore = i;
        }

        return ruleSetList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!firstStart){
            mGameOfLifeView.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGameOfLifeView.stop();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.image_button_play_control:
                if(isRunning) {
                    mGameOfLifeView.toggleAllowGeneration();
                    image_button_play_control.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_24dp));
                }else{
                    if(firstStart){
                        mGameOfLifeView.initWorld();
                        mGameOfLifeView.start();
                        firstStart = false;
                    }else{
                        mGameOfLifeView.toggleAllowGeneration();
                    }
                    image_button_play_control.setImageDrawable(getDrawable(R.drawable.ic_pause_24dp));
                }
                isRunning = !isRunning;
                break;
            case R.id.image_button_restart:
                mGameOfLifeView.stop();
                mGameOfLifeView.restartWorld();
                mGameOfLifeView.start();

                //toolbar_text.setText("Generation 0");
                //image_button_play_control.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_24dp));
                //isRunning = true;
                break;
            case R.id.image_button_speed_up:
                mGameOfLifeView.increaseEvolveTime(50);
                break;
            case R.id.image_button_speed_down:
                mGameOfLifeView.decreaseEvolveTime(50);
                break;
        }
    }
}
