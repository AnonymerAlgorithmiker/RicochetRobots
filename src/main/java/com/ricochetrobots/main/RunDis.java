package com.ricochetrobots.main;

import Data.Testing.SavedConfigs;
import Logic.AI;
import Logic.Game;
import View.AIWindow;
import View.DisplayFx;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Created by Martin Eberle aka WillShakesBeer on 23.11.2021.
 */

//test
public class RunDis extends Application {

    public static void main (String[] args){
        launch(args);
    }


    public void start(final Stage startWindow){
        SavedConfigs testConfig= new SavedConfigs();
        Game game = new Game(testConfig.loadDefaultGameConfig());
        AI ai = new AI(game);
        ai.setAiDefaults();
        DisplayFx displayFx = new DisplayFx(ai,game,startWindow);
        displayFx.playGame();
    }

}
