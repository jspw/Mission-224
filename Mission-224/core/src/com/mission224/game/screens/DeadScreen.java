package com.mission224.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mission224.game.Main;
import com.mission224.game.scenes.Hud;

public class DeadScreen implements Screen {

    private Main game;
    private Stage stage;
    private float pauseTime;
    private Label pauseLabel;

    public DeadScreen(Main game) {
        this.game = game;
        pauseTime = 0;

        Viewport viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, Main.batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // FreeType font generator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/ALGER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderWidth = 1;
        parameter.color = Color.RED;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = new Color(0, 0f, 0, 1);
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        pauseLabel = new Label(String.format("            You are DEAD\n    Your Score is:  %05d    ", Hud.score), labelStyle);

        table.add(pauseLabel).center().expand();
        stage.addActor(table);
        Main.manager.get("Audio/SoundEffects/die.wav", Sound.class).play();
    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        if(Hud.objectiveCleared) pauseLabel.setText(String.format("    Objective Cleared!!!\n    Your Score is:  %05d    ", Hud.score));
        if(Hud.timeUp) pauseLabel.setText(String.format("        Oh man Times Up!!!\n    Your Score is:  %05d    ", Hud.score));
        if(pauseTime > 4) {
            game.setScreen(new Menu(game));
            pauseTime = 0;
            dispose();
        }
        pauseTime += dt;
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
