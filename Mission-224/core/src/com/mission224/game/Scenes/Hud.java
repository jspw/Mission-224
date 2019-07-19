package com.mission224.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mission224.game.Main;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;

    Label countdownLabel;
    Label timeLabel;
    Label levelLabel;
    Label gameLabel;
    Label blankLabel;

    public Hud(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;

        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table tabel = new Table();
        tabel.top();
        tabel.setFillParent(true);

        /*// Bitmap Font
        timeLabel = new Label("Time: ", new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        countdownLabel = new Label(String.format("%05d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BROWN));

        gameLabel = new Label("Level: ", new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        levelLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.BROWN));

		tabel.add(gameLabel).expandX().padTop(10);
		tabel.add(levelLabel).expandX();
		tabel.row();
		tabel.add(timeLabel).expandX().padTop(10);
		tabel.add(countdownLabel).expandX();*/

		// FreeType Font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/ALGER.TTF"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 30;
		parameter.borderWidth = 1;
		parameter.color = Color.LIGHT_GRAY;
		parameter.shadowOffsetX = 3;
		parameter.shadowOffsetY = 3;
		parameter.shadowColor = new Color(0, 0f, 0, 1);
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = font;

		gameLabel = new Label("    Level :  ", labelStyle);
		levelLabel = new Label("1", labelStyle);
		timeLabel = new Label("  Time :  ", labelStyle);
		countdownLabel = new Label(String.format("%05d    ", worldTimer), labelStyle);
		blankLabel = new Label(" ", labelStyle);

		tabel.add(gameLabel).padTop(15);
		tabel.add(levelLabel).padTop(15);
		tabel.add(blankLabel).expandX();
		tabel.add(timeLabel).padTop(15);
		tabel.add(countdownLabel).padTop(15);

		// Add the table to stage
        stage.addActor(tabel);
    }

    public void update(float dt) {
		timeCount += dt;
		if(timeCount >= 1) {
			worldTimer--;

			countdownLabel.setText(String.format("%05d    ", worldTimer));
			timeCount = 0;
		}
	}

	@Override
	public void dispose() {
    	stage.dispose();
	}
}
