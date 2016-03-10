package com.aziflaj.flippybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlippyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture flippy;

    @Override
    public void create() {
        batch = new SpriteBatch();
        flippy = new Texture("flippy.png");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(flippy, 0, 0);
        batch.end();
    }
}
