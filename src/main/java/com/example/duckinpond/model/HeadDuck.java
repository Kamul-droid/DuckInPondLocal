package com.example.duckinpond.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.example.duckinpond.model.DuckDesign.MAXSIZE;
import static javafx.scene.paint.Color.*;

public class HeadDuck extends Duck {
    private final Image image;
    private int ducksize;
    public boolean head;
    private List<Duck> listDuck = new ArrayList<>();
    private boolean canWhistle =true;
    private String filename;

    public HeadDuck() {
        super();
        this.ducksize = MAXSIZE;
        this.image = new Image(filename);
    }

    public void whistleDuck(){
        System.out.println("i can whistle");
    }


    public List<Duck> getListDuck() {
        return listDuck;
    }

    public void setListDuck(List<Duck> listDuck) {
        this.listDuck = listDuck;
    }
}
