/**
 * @module MainActivity
 * @author 3A04Team9
 * @version 1.0
 */

package fred.plusplus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ApplicationAdapter {

    private activity_main view;
    private RandomEventGenerator eventGen;
    private FredAttributeDataStore fred;
    static boolean titleScreen;
    private FredPoller poller;
    private DeteriorateController deteriorateController;
    private RandomEventRunner run;

    @Override
    //create() gets called once, at the start of the game launch
    public void create() {
        fred = new FredAttributeDataStore();

        view = new activity_main(fred);

        poller = new FredPoller(fred);
        deteriorateController = new DeteriorateController(fred);
        run = new RandomEventRunner(fred);

        titleScreen = true;

        Thread pollerThread = new Thread(poller);
        pollerThread.start();

        Thread deteriorateThread = new Thread(deteriorateController);
        deteriorateThread.start();

        Thread runThread = new Thread(run);
        runThread.start();
    }

    @Override
    //render() gets called every frame
    public void render() {
        //Clear screen
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (fred.dead()) {
            view.drawBG();
            view.drawDeathPage(fred.getBaseTexture());
        } else if (titleScreen) {
            view.drawBG();
            view.drawTitlePage(fred.getBaseTexture(), fred.getExteriorTexture());
        } else {
            view.drawBG();
            view.draw(fred.getBaseTexture(), 0, 0);
            view.drawSubSystems(fred.getVisibleSystems());
            view.drawButtons();

            for (Metric m : fred.getMetrics()) {
                view.drawMetric(m);
            }

            fred.deteriorate();
        }
    }

    @Override
    public void dispose() {
        view.dispose();
        fred.dispose();
    }
}
