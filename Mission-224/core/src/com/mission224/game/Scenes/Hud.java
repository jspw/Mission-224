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

    public static Integer worldTimer = 300;
    private float timeCount;
    private Label countdownLabel;
    private Label levelLabel;

    public Hud(SpriteBatch sb) {

        worldTimer = 300;
        timeCount = 0;

        Viewport viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        /*// Bitmap Font
        timeLabel = new Label("Time: ", new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        countdownLabel = new Label(String.format("%05d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BROWN));

        gameLabel = new Label("Level: ", new Label.LabelStyle(new BitmapFont(), Color.BROWN));
        levelLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.BROWN));

		table.add(gameLabel).expandX().padTop(10);
		table.add(levelLabel).expandX();
		table.row();
		table.add(timeLabel).expandX().padTop(10);
		table.add(countdownLabel).expandX();*/

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

		Label gameLabel = new Label("    Life :  ", labelStyle);
		levelLabel = new Label("15", labelStyle);
		Label timeLabel = new Label("  Time :  ", labelStyle);
		countdownLabel = new Label(String.format("%05d    ", worldTimer), labelStyle);
		Label blankLabel = new Label(" ", labelStyle);

		table.add(gameLabel).padTop(15);
		table.add(levelLabel).padTop(15);
		table.add(blankLabel).expandX();
		table.add(timeLabel).padTop(15);
		table.add(countdownLabel).padTop(15);

		// Add the table to stage
        stage.addActor(table);
    }

    public void update(float dt, int life) {
		timeCount += dt;
		if(timeCount >= 1) {
			worldTimer--;
			countdownLabel.setText(String.format("%05d    ", worldTimer));
			timeCount = 0;
		}

		levelLabel.setText(life);
	}

	@Override
	public void dispose() {
    	stage.dispose();
	}
}
