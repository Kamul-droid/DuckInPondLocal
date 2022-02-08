package com.example.duckinpond.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

public interface DuckDesign {
    int MINSIZE = 5;
    int MAXSIZE = 100;

    //void eat(Map<Vector, Lily> lilyVectorMap, Lily lily);
   // boolean move (Vector pos) throws InterruptedException;

    void eat(Map<Vector, Lily> lilyVectorMap, Vector destination);

    boolean move(Map<Vector, Lily> boundary,  List<Rock> rockList) ;

    boolean isHeadDuck();
    //void changeColor(Color color);

    //void changeColor(Image image);

    void changeColor(String image);

    boolean isAlive();
    void hungryDuck();
    void queueDuck();
    void lineUpDuck(Duck duck, HeadDuck headDuck);


}
