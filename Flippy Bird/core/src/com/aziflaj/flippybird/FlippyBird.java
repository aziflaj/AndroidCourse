package com.aziflaj.flippybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlippyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture flippy;
    Texture background;

    float birdY = 0f; // bird's position
    float velocity = 0;
    float gravity = 1.5f;

    int gameState = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        flippy = new Texture("flippy.png");
        background = new Texture("bg.png");

        birdY = Gdx.graphics.getHeight() / 2 - flippy.getHeight() / 2;
    }

    @Override
    public void render() {
        if (gameState != 0) {
            if (Gdx.input.justTouched()) {
                velocity = -30;
            }

            if (birdY - velocity < 0) {
                birdY = 0;
            }

            if (birdY - velocity > Gdx.graphics.getHeight()) {
                birdY = Gdx.graphics.getHeight() + flippy.getHeight();
            }

            if (birdY > 0 || velocity < 0) {
                velocity += gravity;
                birdY -= velocity;
            }
        } else {

            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(flippy, Gdx.graphics.getWidth() / 2 - flippy.getWidth() / 2, birdY);
        batch.end();
    }
}
