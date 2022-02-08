package com.example.duckinpond.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Rock {
    protected Rectangle boundary ;
    protected Vector position  ;



    protected Image image = new Image("r3.png");

    public Rock() {
        this.position = new Vector(350,250);

        this.boundary = new Rectangle(0,0,0, 0);
    }

    public void setPosition(double x, double y ) {
        this.position.set(x,y);

    }


    public Rectangle getBoundary(){
        this.boundary.x = this.position.x;
        this.boundary.y = this.position.y;
        this.boundary.width = this.image.getWidth();
        this.boundary.height = this.image.getHeight();
        return this.boundary;
    }

    public boolean overlaps(Lily other){
        return this.getBoundary().overlaps(other.getBoundary());
    }

    public boolean overlaps(Duck other){
        return this.getBoundary().overlaps(other.getBoundary());
    }

    public void render (GraphicsContext context){
        context.drawImage(image, position.x, position.y);
    }

    public Vector getPosition() {
        return this.position;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
