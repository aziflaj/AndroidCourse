package com.aziflaj.flippybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlippyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture[] flippy;
    Texture background;
    Texture topTube;
    Texture bottomTube;

    int birdState = 0;

    float birdY = 0f; // bird's position
    float velocity = 0;
    float gravity = 1.5f;

    int gameState = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        flippy = new Texture[]{new Texture("bird.png"), new Texture("bird2.png")};
        background = new Texture("bg.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        birdY = Gdx.graphics.getHeight() / 2 - flippy[0].getHeight() / 2;
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
                birdY = Gdx.graphics.getHeight() - flippy[0].getHeight();
            }

            if (birdY > 0 || velocity < 0) {
                velocity += gravity;
                birdY -= velocity;
            }
            birdState = birdState == 1 ? 0 : 1;
        } else {
            if (Gdx.input.justTouched()) {
                gameState = 1;
                velocity = -30;
            }
        }

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(flippy[birdState],
                (Gdx.graphics.getWidth() - flippy[birdState].getWidth()) / 2,
                birdY);
        batch.draw(topTube,
                (Gdx.graphics.getWidth() - topTube.getWidth()) / 2,
                Gdx.graphics.getHeight() - topTube.getHeight() / 2);
        batch.draw(bottomTube,
                (Gdx.graphics.getWidth() - bottomTube.getWidth()) / 2,
                -bottomTube.getHeight() / 2);
        batch.end();
    }
}
