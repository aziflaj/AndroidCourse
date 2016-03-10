package com.aziflaj.flippybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

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

    float gap = 400f;
    float maxTubeOffset;
    Random rnd;
    float[] tubeOffset;
    float tubeVelovity = 4f;
    float[] tubeX;
    int numberOfTubes = 4;
    float distanceBetweenTubes;

    @Override
    public void create() {
        batch = new SpriteBatch();
        flippy = new Texture[]{new Texture("bird.png"), new Texture("bird2.png")};
        background = new Texture("bg.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        birdY = Gdx.graphics.getHeight() / 2 - flippy[0].getHeight() / 2;
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - 100 - gap / 2;
        rnd = new Random();
        tubeX = new float[numberOfTubes]; //= (Gdx.graphics.getWidth() - topTube.getWidth()) / 2;
        tubeOffset = new float[numberOfTubes];
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (rnd.nextFloat() - 0.5f) * 2 * maxTubeOffset;
            tubeX[i] = (Gdx.graphics.getWidth() - topTube.getWidth()) / 2 + distanceBetweenTubes * i;
        }
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState != 0) {
            if (Gdx.input.justTouched()) {
                velocity = -30;
            }

            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                }

                tubeX[i] -= tubeVelovity;
                batch.draw(topTube,
                        tubeX[i],
                        Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube,
                        tubeX[i],
                        Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
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

        batch.draw(flippy[birdState],
                (Gdx.graphics.getWidth() - flippy[birdState].getWidth()) / 2,
                birdY);
        batch.end();
    }
}
