package fred.plusplus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;

/**
 * Created by Josh on 31/03/2017.
 */

public class activity_main {

    private ShowSubsystemListener showListener;
    private ToggleSubsystemListener toggleListener;
    private StimuliListener stimuliListener;
    private InputMultiplexer multiplexer;
    private int space = 175;
    private int metricPos;

    private SpriteBatch batch;
    private BitmapFont font;


    private Texture red;
    private Texture yellow;
    private Texture green;

    private Texture bg;

    public activity_main(FredAttributeDataStore fred) {
        batch = new SpriteBatch();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 160;
        font = new FreeTypeFontGenerator(Gdx.files.internal("Fonts//Bubblegum.ttf")).generateFont(parameter);

        red = new Texture("Red.png");
        yellow = new Texture("Yellow.png");
        green = new Texture("Green.png");
        bg = new Texture("BG.png");

        resetMetricPos();

        showListener = new ShowSubsystemListener(fred);
        stimuliListener = new StimuliListener(fred);
        toggleListener = new ToggleSubsystemListener(fred, showListener, stimuliListener);

        showListener.addButton("nervous", "shownNervous.png", "hiddenNervous.png");
        showListener.addButton("cardiovascular", "shownCardiovascular.png", "hiddenCardiovascular.png");
        showListener.addButton("digestive", "shownDigestive.png", "hiddenDigestive.png");
        showListener.addButton("locomotor", "shownLocomotor.png", "hiddenLocomotor.png");

        toggleListener.addButton("nervous", "activeNervous.png", "inactiveNervous.png");
        toggleListener.addButton("cardiovascular", "activeCardiovascular.png", "inactiveCardiovascular.png");
        toggleListener.addButton("digestive", "activeDigestive.png", "inactiveDigestive.png");
        toggleListener.addButton("locomotor", "activeLocomotor.png", "inactiveLocomotor.png");

        stimuliListener.addButton(0, "nervous", "StimuliButton_Homework.png", "inactiveNervous.png", new Stimulus("homework", "Pencil.mp3"));
        stimuliListener.addButton(space, "nervous", "StimuliButton_Party.png", "inactiveNervous.png", new Stimulus("partying", "Party.mp3"));

        stimuliListener.addButton(space * 2, "cardiovascular", "StimuliButton_Treadmill.png", "inactiveCardiovascular.png", new Stimulus("running", "Treadmill.mp3"));
        stimuliListener.addButton(space * 3, "cardiovascular", "StimuliButton_Medicine.png", "inactiveCardiovascular.png", new Stimulus("medicine", "Medicine.mp3"));

        stimuliListener.addButton(space * 4, "digestive", "StimuliButton_Burger.png", "inactiveDigestive.png", new Stimulus("food", "Burger.mp3"));
        stimuliListener.addButton(space * 5, "digestive", "StimuliButton_Beer.png", "inactiveDigestive.png", new Stimulus("drink", "Beer.mp3"));

        stimuliListener.addButton(space * 6, "locomotor", "StimuliButton_Exercise.png", "inactiveLocomotor.png", new Stimulus("lift_weights", "Weights.mp3"));
        stimuliListener.addButton(space * 7, "locomotor", "StimuliButton_Bandaid.png", "inactiveLocomotor.png", new Stimulus("bandage", "Bandaid.mp3"));

        //This is how libGDX handles multiple different input listeners
        //Whenever a touch event happens, it will pass it to the first listener
        //If a listener doesn't handle it, it returns false and the event passes to the next listener
        //If a listener does handle the input, it returns true and the multiplexer stops
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(showListener);
        multiplexer.addProcessor(toggleListener);
        multiplexer.addProcessor(stimuliListener);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void drawBG() {
        batch.begin();
        batch.draw(bg, 0, 0);
        batch.end();
        resetMetricPos();
    }

    public void draw(Texture texture, int x, int y) {
        batch.begin();
        batch.draw(texture, x, y);
        batch.end();
    }

    public void drawSubSystems(ArrayList<System> systems) {
        batch.begin();
        for (System sys : systems) {
            batch.draw(sys.getTexture(), 0, 0);
        }
        batch.end();
    }

    public void drawButtons() {
        batch.begin();
        for (Button b : showListener.getButtons()) {
            if (b.isActive()) {
                batch.draw(b.getTexture(), b.getX(), b.getY());
            }
        }
        for (Button b : toggleListener.getButtons()) {
            batch.draw(b.getTexture(), b.getX(), b.getY());
        }
        for (Button b : stimuliListener.getButtons()) {
            if (b.isActive()) {
                batch.draw(b.getTexture(), b.getX(), b.getY());
            }
        }
        batch.end();
    }

    private void resetMetricPos() {
        metricPos = 2300;
    }

    public void drawMetric(Metric m) {
        int posX = 1210;
        int width = (int) 2.5 * m.getValue() + 10;
        int height = 20;

        batch.begin();

        batch.draw(m.getImage(), posX - 100, metricPos - 30, 100, 100);

        if (m.isGood()) {
            if (m.getValue() > 70) {
                batch.draw(green, posX, metricPos, width, height);
            } else if (m.getValue() > 30) {
                batch.draw(yellow, posX, metricPos, width, height);
            } else {
                batch.draw(red, posX, metricPos, width, height);
            }
        } else {
            if (m.getValue() < 30) {
                batch.draw(green, posX, metricPos, width, height);
            } else if (m.getValue() < 70) {
                batch.draw(yellow, posX, metricPos, width, height);
            } else {
                batch.draw(red, posX, metricPos, width, height);
            }
        }
        batch.end();
        metricPos -= 95;
    }

    public void drawTitlePage(Texture base, Texture clothes) {
        batch.begin();
        batch.draw(base, 0, 0);
        batch.draw(clothes, 0, 0);
        font.draw(batch, "Fred++", 500, 180);
        batch.end();
    }

    public void drawDeathPage(Texture base) {
        batch.begin();
        batch.draw(base, 0, 0);
        font.draw(batch, "You died!", 450, 180);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }

}
