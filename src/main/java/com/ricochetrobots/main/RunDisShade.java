package com.ricochetrobots.main;
//To compile a javafx project into a shaded jar a Main that does not extend Application is needed.
//So this class functions as a workaround
public class RunDisShade {
    public static void main(String[] args){
        RunDis.main(args);
    }
}
