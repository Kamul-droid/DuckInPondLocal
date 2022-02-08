package com.example.duckinpond.model;

public class Rectangle {
    public double x;
    public double y;
    public double width;
    public  double height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(){
        this.x = 0;
        this.y = 0;
        this.width = 1;
        this.height = 1;
    }

    public boolean overlapsSide (Rectangle other){
        boolean noOverlap =
                this.x+this.width < other.x ||
                        other.x + other.width < this.x ||
                        this.y +this.height < other.y ||
                        other.y + other.height < this.y;

        return ! noOverlap;
    }

    public boolean overlaps(Rectangle other){
        /*
        * Four cases where there is no overlap
        * this to the left of other
        * this to the right of other
        * this is above other
        * other is above this
        * */

        boolean noOverlap =
                this.x+this.width < other.x ||
                        other.x + other.width < this.x ||
                        this.y +this.height < other.y ||
                        other.y + other.height < this.y;

        return ! noOverlap;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
