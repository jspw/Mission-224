package com.mission224.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mission224.game.Main;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;

    Label countdownLabel;
    Label timeLabel;
    Label levelLabel;
    Label gameLabel;

    public Hud(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;

        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table tabel = new Table();
        tabel.top();
        tabel.setFillParent(true);

        timeLabel = new Label("Time: ", new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        timeLabel.setScale(10f);
        timeLabel.setPosition(200, 200);
        countdownLabel = new Label(String.format("%05d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BROWN));

        gameLabel = new Label("Level: ", new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        levelLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.BROWN));

        /*tabel.add(gameLabel).expandX().padTop(10);
        tabel.add(levelLabel).expandX();
        //tabel.row();
        tabel.add(timeLabel).expandX().padTop(10);
        tabel.add(countdownLabel).expandX();

        stage.addActor(tabel);*/
        stage.addActor(timeLabel);
    }
}
