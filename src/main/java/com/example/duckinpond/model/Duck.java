package com.example.duckinpond.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Duck  implements DuckDesign {


    /*
     * we have private attribute as ducksize to check his growth
     * the boolean alive to check if the duck can continue to eat,
     * the boolean inline to check if the duck is following a head duck
     * and the boolean lastInQueue to check if the duck is the last one inline
     * */

    protected int ducksize;
    protected int timeToEat;
    protected boolean alive = true;
    protected boolean inline = false;
    protected boolean lastInQueue = false;
    protected boolean head = false;
    protected Vector position;
    protected Vector velocity;
    protected Rectangle boundary;
    public double rotation; //degrees
    //protected List<Duck> listDuck= new ArrayList<>();
    private String imageFileName ;
    private boolean checkpoint = true;
    private double newx = 0;
    private double newy =0 ;
    private double vx =0;
    private double vy =0;
    boolean rockCollision = false;
    protected Image image = new Image("c1.png") ;
    Vector ajustPosition = new Vector();



    //protected Image image = new Image(filename) ;

    public Duck() {

        this.ducksize = MINSIZE;
        this.position = new Vector();
        this.velocity = new Vector(5,5);
        this.rotation =0;
        this.boundary = new Rectangle();


    }

    /*
     * We set the picture of the duck and get his boundary
     * */
    public void setImage(String image) {
        this.image = new Image(image);
        this.boundary.width = this.image.getWidth();
        this.boundary.height = this.image.getHeight();
    }

    public Image getImage(){
        return this.image;
    }

    /*
     * To get the new position of the duck on the canvas
     * */
    public Vector getPosition() {
        return this.position;
    }

    public void setPosition(double posX, double posY) {
        this.position.set(posX,posY);
    }

    public Rectangle getBoundary() {
        this.boundary.width = getImage().getWidth();
        this.boundary.height = getImage().getHeight();
        this.boundary.setPosition(this.position.x, this.position.y);
        return this.boundary;
    }

    public boolean overlaps (Lily lily){
        return this.getBoundary().overlaps(lily.getBoundary());
    }

    public boolean overlaps (Duck other){
        return this.getBoundary().overlaps(other.getBoundary());
    }

    public boolean overlaps (Rock other){
        return this.getBoundary().overlaps(other.getBoundary());
    }

    public  void update (double deltaTime){
        /*
        * update according to velocity
        * */
        this.position.add(this.velocity.x * deltaTime, this.velocity.y * deltaTime);
      // wrap(900,700);

    }

    public void wrap(double screenWidth, double screenHeight){
        if (this.position.getX()+this.getBoundary().width/2<0)
            this.position.x= screenWidth + this.getBoundary().width/2;
        if (this.position.getX() > screenWidth + this.getBoundary().width/2)
            this.position.x= - this.getBoundary().width/2;
        if (this.position.getY() + this.getBoundary().height/2 <0)
            this.position.y= screenHeight;
        if (this.position.getY() > screenHeight)
            this.position.y = -this.getBoundary().height/2;

    }


    @Override
    public void  eat(Map<Vector, Lily> lilyVectorMap, Vector destination) {
        Lily lily = lilyVectorMap.get(destination);
        //System.out.println("ducksize"+ducksize);
        if (this.ducksize < MAXSIZE){

            if (lily != null) {
                if (overlaps(lily)){
                    this.ducksize+=5;

                    grow(this.ducksize);
                    lilyVectorMap.remove(destination);


                }
            }
        }

    }

    protected void grow( int ducksize){
        if (ducksize==MINSIZE){
            this.setImage("c1.png");
        }
        if (ducksize==10){
            this.setImage("c2.png");

        }
        if (ducksize==15){

            this.setImage("c3.png");


        }
        if (ducksize==25){
            this.head = true;
            this.setImage("c4.png");

        }
        if (ducksize==MAXSIZE){
            this.setImage("c5.png");
            this.head = true;
        }

    }

    @Override
    public boolean move(Map<Vector, Lily> lilyMap, List<Rock> rockList)  {

        double vx=0;
        double vy=0 ;
        this.timeToEat++;
        //this.velocity.setLength(0);
        Vector lilyPos = searchNextLily(lilyMap);
        Lily temp = lilyMap.get(lilyPos);
        double angle = 0;
        if (!(temp == null)) {

             angle = Math.toDegrees(Math.PI - Math.abs(Math.atan(((lilyPos.x+temp.getBoundary().width/2)-(this.position.x+this.boundary.width/2))/ ((this.position.y +this.boundary.height/2)-(lilyPos.y+temp.getBoundary().height/2)))));
        }
        //System.out.println("degre"+angle);
        //position.setAngle(angle);
        this.rotation = angle;//position.getAngle();

        //update(1/60);

        if (!lilyMap.isEmpty()) {

            if (overlaps(temp)){
                eat(lilyMap,lilyPos);
                return true;
            }
            for (Rock rock:rockList) {
                rockCollision =  overlaps(rock);
                //System.out.println("rock dim"+rock.getBoundary().x +rock.getBoundary().width + rock.getBoundary().y+rock.getBoundary().height);
               // System.out.println("rock collision " +rockCollision);
                if (rockCollision)
                {
                    double currentSideX = this.position.x- lilyPos.x;
                    double rockCenter = rock.getBoundary().x + rock.getBoundary().width/2;
                    double radius =  Math.sqrt(Math.pow(((rockCenter)- this.position.x),2)+Math.pow(((rock.getBoundary().y + rock.getBoundary().height/2)-this.position.y),2));
                    radius+=10;

                   Vector origin = new Vector(this.position.x,this.position.y);
                    double increment = 0.009;
                    if (currentSideX < 0){


                        for (double i = 180; i>= 90; i-=increment)
                        {

                            this.position.x = (origin.x - radius*Math.cos(Math.toRadians(i)));

                            this.position.y = origin.y - radius* Math.sin(Math.toRadians(i));


                        }
                        for (double i = 90; i>= 0; i-=increment)
                        {

                            this.position.x = (origin.x + radius*Math.cos(Math.toRadians(i)));

                            this.position.y = origin.y + radius* Math.sin(Math.toRadians(i));


                        }


                    } else
                    {

                        for (double i =  90; i >=0; i-=increment)
                        {

                            this.position.x = origin.x - radius*Math.cos(Math.toRadians(i));

                            this.position.y = origin.y - radius* Math.sin(Math.toRadians(i));




                        }
                        for (double i =  90; i >=180; i-=increment)
                        {

                            this.position.x = origin.x + radius*Math.cos(Math.toRadians(i));

                            this.position.y = origin.y + radius* Math.sin(Math.toRadians(i));




                        }


                    }
                }
            }
                vx = this.position.x - lilyPos.x;
                vy = this.position.y - lilyPos.y;
            if (!rockCollision) {
            }
            if (vx < 0)
            {
                   this.position.x += ((-vx / 60) + 0.001);
            }
            if (vy < 0)
            {
                this.position.y += ((-vy / 60) + 0.001);
            }
            if (vx > 0)
            {
                this.position.x -= ((vx / 60) - 0.001);
            }
            if (vy > 0)
            {
                this.position.y -= ((vy / 60 )- 0.001);
            }

            wrap(900,600);
        }
        else
        {
           this.randomMove(rockList);
        }

        if (this.timeToEat == 5000){
            System.out.println("humgry et size "+this.ducksize);
            this.hungryDuck();
            this.timeToEat = 0;
        }
        return false;

    }

    private Vector NextLilyAfterThis(Map<Vector, Lily> lilyMap, Vector lilyPos) {

        double d = 1500 ;
        double dist = 0;
        Vector nextPos = new Vector(-1,-1);
        if (!lilyMap.keySet().isEmpty()) {

            for (Vector pos:lilyMap.keySet()) {

                if (pos != lilyPos){

                  dist = Math.sqrt(Math.pow(((pos.x)- this.position.x),2)+Math.pow((pos.y-this.position.y),2));
                }


                if (dist < d){
                    //System.out.println("dist enter " +dist);

                    d=dist;
                    //System.out.println("dist " +d);
                    //System.out.println("pos act "+pos.x +pos.y);
                    nextPos=pos;
                }
            }
        }
        return  nextPos;
    }

    protected  void randomMove(List<Rock> rockList){
        /*
         * Array for random move
         * */

        List<Integer> choice = new ArrayList<>();
        choice.add(-1);
        choice.add(1);
        /*List<Integer> screenPosition = new ArrayList<>();
        screenPosition.add(0);
        screenPosition.add(20);
        screenPosition.add(40);
        screenPosition.add(80);
        screenPosition.add(100);
        screenPosition.add(200);
        screenPosition.add(500);
        screenPosition.add(600);
        screenPosition.add(700);*/
        double angle = 0;
        if (!(ajustPosition == null)) {

            angle = Math.toDegrees(Math.PI/2 - Math.abs(Math.atan(((ajustPosition.x)-(this.position.x+this.boundary.width/2))/ ((this.position.y +this.boundary.height/2)-(ajustPosition.y)))));
        }
        //System.out.println("degre"+angle);
        //position.setAngle(angle);
        this.rotation = angle;//position.getAngle();



        int dir;
        if (checkpoint ) {
            Random random = new Random();
            int val = random.nextInt(choice.size());
            int valy = random.nextInt(choice.size());
            dir = choice.get(val);
            dir = choice.get(valy);
            //newx = dir * screenPosition.get(random.nextInt(screenPosition.size()));
            //newy = dir * screenPosition.get(random.nextInt(screenPosition.size()));

            newx = dir * Math.random()*820+50;
            newy = dir * Math.random()*650+50;
            ajustPosition = wrapRandom(900,600,newx,newy);
            boolean hasCollide =true;
            while (hasCollide) {

                for (Rock rock : rockList) {
                    if (!((ajustPosition.x >= rock.getPosition().x && ajustPosition.x<= (rock.getPosition().x+rock.getBoundary().width)) && ajustPosition.y >= rock.position.y && ajustPosition.y <= (rock.getPosition().y+rock.getBoundary().height))) {
                        hasCollide = false;
                    }
                }

                newx = dir * Math.random()*820+50;
                newy = dir * Math.random()*650+50;
                ajustPosition = wrapRandom(900,600,newx,newy);
            }

//
            vx = this.position.x - ajustPosition.getX();
            vy = this.position.y - ajustPosition.getY();
            System.out.println("x et y random"+ajustPosition.x+" "+ajustPosition.y);
            checkpoint = false;

        }

        if (Math.abs(this.position.x - ajustPosition.x) < 5 || Math.abs(this.position.y-ajustPosition.y) < 5 ){
            checkpoint = true;
            System.out.println("checkpoint generate pos "+checkpoint);
        }
        for (Rock rock:rockList) {
            rockCollision =  overlaps(rock);
            if (rockCollision)
            {
                double currentSideX = this.position.x- newx;
                double rockCenterx = rock.getBoundary().x + rock.getBoundary().width/2;
                double rockCentery = rock.getBoundary().y + rock.getBoundary().height/2;
                double radius =  Math.sqrt(Math.pow((rockCenterx- this.position.x),2)+Math.pow((rockCentery-this.position.y),2));
                radius+=10;

                Vector origin = new Vector(this.position.x,this.position.y);
                double increment = 0.005;
                if (currentSideX < 0){


                    for (double i = 180; i>= 90; i-=increment)
                    {

                        this.position.x = (origin.x - radius*Math.cos(Math.toRadians(i)));

                        this.position.y = origin.y - radius* Math.sin(Math.toRadians(i));

                    }
                    for (double i = 90; i>= 0; i-=increment)
                    {

                        this.position.x = (origin.x + radius*Math.cos(Math.toRadians(i)));

                        this.position.y = origin.y + radius* Math.sin(Math.toRadians(i));


                    }


                } else
                {

                    for (double i =  90; i >=0; i-=increment)
                    {

                        this.position.x = origin.x - radius*Math.cos(Math.toRadians(i));

                        this.position.y = origin.y - radius* Math.sin(Math.toRadians(i));




                    }
                    for (double i =  90; i >=180; i-=increment)
                    {

                        this.position.x = origin.x + radius*Math.cos(Math.toRadians(i));

                        this.position.y = origin.y + radius* Math.sin(Math.toRadians(i));




                    }


                }
            }

        }


            if (vx < 0) {

                this.position.x += -vx / 250 + 0.001;

            }
            if (vy < 0) {
                this.position.y += -vy / 250 + 0.001;

            }
            if (vx > 0) {
                this.position.x -= vx / 250 - 0.001;

            }
            if (vy > 0) {
                this.position.y -= vy / 250 - 0.001;
            }

        System.out.println(" after move"+this.position.x+" "+this.position.y);
        System.out.println(" dest move"+ajustPosition.x+" "+ajustPosition.y);


    }

    private Vector wrapRandom(double screenWidth, double screenHeight, double newx, double newy) {

        if (newx < 0)
            newx = Math.abs(newx);
        if (newx > screenWidth )
            newx = screenWidth;
        if (newy  < 0)
            newy= Math.abs(newy);
        if (newy > screenHeight)
            newy = screenHeight;

        return new Vector(newx,newy);

    }

    @Override
    public boolean isHeadDuck() {
        return this.head;
    }

    @Override
    public void changeColor(String image) {
        this.image =new Image(image);
    }

    @Override
    public boolean isAlive() {
        boolean alive = this.alive;
        return alive;
    }

    @Override
    public void hungryDuck() {
        if (this.ducksize > 0) {

            this.ducksize-=2;
            this.grow(this.ducksize);
            if (this.ducksize==0){
                this.alive=false;

            }
        }
    }

    @Override
    public void queueDuck() {

    }

    @Override
    public void lineUpDuck(Duck duck, HeadDuck hduck) {
        if (duck != null) {
            if (hduck.head){
                //hduck.getListDuck() ;//.add(duck);
                this.inline = true;
            }

        }

    }

    public static void spawn(List<Duck> duckList,List<Lily> lilyList,List<Rock> rocks){

    }

    public Vector searchNextLily(Map<Vector,Lily> allpos){
        double d = 1500 ;
        List<Integer> rockX = new ArrayList<>();
        rockX.add(350);
        rockX.add(700);
        rockX.add(750);

        Vector nextPos = new Vector(-1,-1);
        if (!allpos.isEmpty()) {

            for (Vector pos:allpos.keySet()) {

                double dist = Math.sqrt(Math.pow(((pos.x)- this.position.x),2)+Math.pow((pos.y-this.position.y),2));

                if (dist < d){
                    //System.out.println("dist enter " +dist);

                    d=dist;
                    //System.out.println("dist " +d);
                    //System.out.println("pos act "+pos.x +pos.y);
                    nextPos=pos;
                }
            }
        }
        /*this.position.x = nextPos.x;
        this.position.y = nextPos.y;*/
        return nextPos;

    }

    public Lily getLilyAtPos( Vector position, Lily lili){
        if (lili.position.x == position.x && lili.position.y == position.y){
            return lili;
        }
        return lili;
    }


    public void render (GraphicsContext context){
        context.save();
        context.translate(this.position.getX(),this.position.getY());
        context.rotate(this.rotation);
        context.translate(-this.image.getWidth()/2,-this.image.getHeight()/2);
        context.drawImage(this.getImage(), 0, 0);
        context.restore();

    }

    public boolean follow( Map<Vector,Duck> duckList, Map<Vector,Lily> lilyList, List<Rock> rocks) {

        double vx=0;
        double vy=0 ;
        this.timeToEat++;
        Vector headDestination = searchHead(duckList);
        Vector destination = searchNextDuck(duckList,headDestination);
        Duck temp = duckList.get(destination);
        double angle = 0;
        if (!(temp == null)) {

            angle = Math.toDegrees(Math.PI - Math.abs(Math.atan(((destination.x+temp.getBoundary().width/2)-(this.position.x+this.boundary.width/2))/ ((this.position.y +this.boundary.height/2)-(destination.y+temp.getBoundary().height/2)))));
        }
        this.rotation = angle;
        if (!lilyList.isEmpty())
        {

            for (Lily lili:lilyList.values() )
            {
                if (overlaps(lili))
                {
                    eat(lilyList,destination);
                    return true;
                }

            }

            for (Rock rock:rocks)
            {
                rockCollision =  overlaps(rock);
                //System.out.println("rock dim"+rock.getBoundary().x +rock.getBoundary().width + rock.getBoundary().y+rock.getBoundary().height);
                // System.out.println("rock collision " +rockCollision);
                if (rockCollision)
                {
                    double currentSideX = this.position.x- destination.x;
                    double rockCenter = rock.getBoundary().x + rock.getBoundary().width/2;
                    double radius =  Math.sqrt(Math.pow(((rockCenter)- this.position.x),2)+Math.pow(((rock.getBoundary().y + rock.getBoundary().height/2)-this.position.y),2));
                    radius+=10;

                    Vector origin = new Vector(this.position.x,this.position.y);
                    double increment = 0.009;
                    if (currentSideX < 0){


                        for (double i = 180; i>= 90; i-=increment)
                        {

                            this.position.x = (origin.x - radius*Math.cos(Math.toRadians(i)));

                            this.position.y = origin.y - radius* Math.sin(Math.toRadians(i));


                        }
                        for (double i = 90; i>= 0; i-=increment)
                        {

                            this.position.x = (origin.x + radius*Math.cos(Math.toRadians(i)));

                            this.position.y = origin.y + radius* Math.sin(Math.toRadians(i));


                        }


                    } else
                    {

                        for (double i =  90; i >=0; i-=increment)
                        {

                            this.position.x = origin.x - radius*Math.cos(Math.toRadians(i));

                            this.position.y = origin.y - radius* Math.sin(Math.toRadians(i));




                        }
                        for (double i =  90; i >=180; i-=increment)
                        {

                            this.position.x = origin.x + radius*Math.cos(Math.toRadians(i));

                            this.position.y = origin.y + radius* Math.sin(Math.toRadians(i));




                        }


                    }
                }
            }

            vx = this.position.x - destination.x;
            vy = this.position.y - destination.y;
            if (!rockCollision) {
            }
            if (vx < 0)
            {
                this.position.x += ((-vx / 60) + 0.001);
            }
            if (vy < 0)
            {
                this.position.y += ((-vy / 60) + 0.001);
            }
            if (vx > 0)
            {
                this.position.x -= ((vx / 60) - 0.001);
            }
            if (vy > 0)
            {
                this.position.y -= ((vy / 60 )- 0.001);
            }

            wrap(900,600);
        }
        else
        {
            this.randomMove(rocks);
        }

        if (this.timeToEat == 5000){
            System.out.println("humgry et size "+this.ducksize);
            this.hungryDuck();
            this.timeToEat = 0;
        }
        return false;

    }

    public Vector searchHead(Map<Vector,Duck> duckVectorMap) {
        Vector newPos = new Vector();
        if (!duckVectorMap.isEmpty()) {
            for (Duck head:duckVectorMap.values())
            {
                if (head.isHeadDuck()){
                   newPos = head.position;
                   head.inline = true;
                   break;
                }
            }

        }

        return newPos;

    }

    public Vector searchNextDuck(Map<Vector,Duck> vectorDuckMap, Vector position){

        double d = 1500 ;
        Vector nextPos = new Vector();
        if (!vectorDuckMap.isEmpty()) {

            for (Vector pos:vectorDuckMap.keySet()) {

                double dist = Math.sqrt(Math.pow(((pos.x)- position.x),2)+Math.pow((pos.y-position.y),2));

                if (dist < d && !vectorDuckMap.get(pos).inline){

                    d=dist;
                    nextPos=pos;
                }
            }
            Duck nextDuck =  vectorDuckMap.get(nextPos);
            if ( !(nextDuck == null)) {

                nextDuck.inline = true;
            }
        }
        return nextPos;
    }
}

