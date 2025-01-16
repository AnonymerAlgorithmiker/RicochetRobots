package com.ricochetrobots.main;

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
        DisplayFx displayFx = new DisplayFx();
        displayFx.runGame(startWindow);
    }

}
