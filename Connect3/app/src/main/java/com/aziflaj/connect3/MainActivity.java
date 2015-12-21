package com.aziflaj.connect3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 0 => yellow
    // 1 => red
    int activePlayer = 0;
    boolean gameFinished = false;
    int counter = 0;

    // 2 stands for no player
    int[][] gameMap = new int[][]{
            {2, 3, 4},
            {3, 4, 5},
            {4, 5, 6}
    };

    Button restartButton;
    GridLayout gameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameLayout = (GridLayout) findViewById(R.id.game_grid);
        restartButton = (Button) findViewById(R.id.restart_button);
        restartButton.setAlpha(0f);
    }


    public void dropIn(View view) {
        if (gameFinished) {
            Toast.makeText(getApplicationContext(), "Game finished", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageView tile = (ImageView) view;
        String tag = tile.getTag().toString();
        int row = Integer.parseInt("" + tag.charAt(0));
        int col = Integer.parseInt("" + tag.charAt(1));

        if (gameMap[row][col] > 1) {
            tile.setTranslationY(-1000f);
            gameMap[row][col] = activePlayer;

            if (activePlayer == 0) {
                tile.setImageResource(R.drawable.yellow);
                counter++;
                activePlayer = 1;
            } else {
                tile.setImageResource(R.drawable.red);
                counter++;
                activePlayer = 0;
            }

            tile.animate().translationYBy(1000f).rotation(360).setDuration(300);

            switch (check()) {
                case -1:
                    gameFinished = true;
                    Toast.makeText(getApplicationContext(), "It's a tie!", Toast.LENGTH_SHORT).show();
                    restartButton.animate().alpha(1f).setDuration(500);
                    break;

                case 0:
                    gameFinished = true;
                    Toast.makeText(getApplicationContext(), "Yellow won!", Toast.LENGTH_SHORT).show();
                    restartButton.animate().alpha(1f).setDuration(500);
                    break;

                case 1:
                    gameFinished = true;
                    Toast.makeText(getApplicationContext(), "Red won!", Toast.LENGTH_SHORT).show();
                    restartButton.animate().alpha(1f).setDuration(500);
                    break;
            }

        } else {
            Toast.makeText(getApplicationContext(), "Taken", Toast.LENGTH_SHORT).show();
        }

    }

    public void restartGame(View view) {
        gameMap = new int[][]{
                {2, 3, 4},
                {3, 4, 5},
                {4, 5, 6}
        };

        for (int i = 0; i < gameLayout.getChildCount(); i++) {
            ((ImageView) gameLayout.getChildAt(i)).setImageResource(0); //empty image
        }

        gameFinished = false;
        view.setAlpha(0f);
    }

    private int check() {
        //check if tie
        if (counter == gameLayout.getChildCount()) {
            return -1;
        }

        // check rows
        if (gameMap[0][0] == gameMap[0][1] && gameMap[0][0] == gameMap[0][2]) {
            return gameMap[0][0];
        }

        if (gameMap[1][0] == gameMap[1][1] && gameMap[1][0] == gameMap[1][2]) {
            return gameMap[1][0];
        }

        if (gameMap[2][0] == gameMap[2][1] && gameMap[2][0] == gameMap[2][2]) {
            return gameMap[2][0];
        }

        // check columns
        if (gameMap[0][0] == gameMap[1][0] && gameMap[0][0] == gameMap[2][0]) {
            return gameMap[0][0];
        }

        if (gameMap[0][1] == gameMap[1][1] && gameMap[0][1] == gameMap[2][1]) {
            return gameMap[0][1];
        }

        if (gameMap[0][2] == gameMap[1][2] && gameMap[0][2] == gameMap[2][2]) {
            return gameMap[0][2];
        }

        // check diagonals
        if (gameMap[0][0] == gameMap[1][1] && gameMap[0][0] == gameMap[2][2]) {
            return gameMap[0][0];
        }

        if (gameMap[0][2] == gameMap[1][1] && gameMap[0][2] == gameMap[2][0]) {
            return gameMap[0][2];
        }

        return 2;
    }
}
