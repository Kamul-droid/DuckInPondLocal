package com.example.duckinpond;

import com.example.duckinpond.model.Duck;
import com.example.duckinpond.model.Lily;
import com.example.duckinpond.model.Rock;
import com.example.duckinpond.model.Vector;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        stage.setTitle("Hello DUCKPOND!");

        stage.setScene(scene);
        double  SCREEN_WIDHT = 900;
        double  SCREEN_HEIGHT = 700;
        Canvas canvas = new Canvas(SCREEN_WIDHT,SCREEN_HEIGHT);
        GraphicsContext context = canvas.getGraphicsContext2D();
        root.setCenter(canvas);
        context.setFill(Color.DARKOLIVEGREEN);
        context.fillRect(0,0,900,700);

        stage.show();

        // create the rocks
        List<Rock> rocks = new ArrayList<>();
        spawnRocks( rocks);


        // creer un nenuphar et le mangerj"
        List<Lily> lilyList = new ArrayList<>();
        Lily firstlili = new Lily();
        lilyList.add(firstlili);
        //Lily first = new Lily();
       // lilyList.add(first);
        //first.setPosition(500,250);

        List<Vector> vectorList = new ArrayList<>();
        vectorList.add(firstlili.getPosition());
       //vectorList.add(first.getPosition());


        Map<Vector, Lily> lilyVectorMap = new HashMap<>();
        lilyVectorMap.put(firstlili.getPosition(),firstlili);
       // lilyVectorMap.put(first.getPosition(),first);


        // ArrayList de canard present sur le plateau
        Map<Duck,Vector> duckVectorMap = new HashMap<>();
        List<Duck> duckList = new ArrayList<>();
        List<Duck> headDuck = new ArrayList<>();
        // Another array de duck
        Map<Vector,Duck> vectorDuckMap = new HashMap<>();



        spawnDuck(duckVectorMap, vectorDuckMap, duckList,  rocks);

        spawnLily( rocks, lilyList,  duckList,  lilyVectorMap,  vectorList);






        AnimationTimer gameloop = new AnimationTimer() {
            boolean hasMoveTo = false;
            @Override
            public void handle(long l) {



                if (!duckVectorMap.isEmpty()) {

                    for (Duck litleduck : duckVectorMap.keySet())
                    {

                        if (litleduck != null)
                        {
                            // find next destination
                           // Vector destination = litle.searchNextLily(lilyVectorMap);

                            if (litleduck.getPosition().x > 0 && litleduck.getPosition().x < 900 && litleduck.getPosition().y > 0 && litleduck.getPosition().y < 700)
                            {
                                //Vector destinationHead = new Vector(0,0);

                                if (litleduck.isHeadDuck()){

                                        hasMoveTo = litleduck.move(lilyVectorMap,rocks);

                                }
                                else{
                                    // check if there is a head duck
                                    boolean isThereAheadDuck = false;
                                    for (Duck head:duckList)
                                    {
                                        if (head.isHeadDuck()){
                                            isThereAheadDuck = true;
                                           // destinationHead = new Vector( head.getPosition().x-40,head.getPosition().y-40);//litle.searchNextHead(duckVectorMap);
                                            break;
                                        }
                                    }

                                    if (isThereAheadDuck){

                                            //Vector newDestination = new Vector(litle.getPosition().x-litle.getBoundary().width,litle.getPosition().y);
                                            //destinationHead.x+=60;
                                            //destinationHead.y+=60;
                                            hasMoveTo = litleduck.follow(vectorDuckMap,lilyVectorMap,rocks);


                                    } else
                                    {

                                            hasMoveTo = litleduck.move(lilyVectorMap,rocks);

                                    }
                                }



                            }
                            context.setFill(Color.DARKGREEN);
                            context.fillRect(0, 0, SCREEN_WIDHT,SCREEN_HEIGHT);
                            drawRock(rocks, context);
                            drawLily(lilyVectorMap,context);
                            drawDuck(duckVectorMap,context);
                            //litleduck.render(context);
//

                        }
                        if (!litleduck.isAlive()) {
                            System.out.println("not alive " + litleduck.isAlive());
                            duckVectorMap.remove(litleduck);
                            context.setFill(Color.DARKGREEN);
                            context.fillRect(0, 0, 900, 700);
                            // draw lily
                           drawLily(lilyVectorMap,context);
                            //litle.render(context);
                        }


                    }



                }
            }
        };
        gameloop.start();
    }


    public static void main(String[] args) {
        launch();
    }

    private void drawDuck(Map<Duck,Vector> duckList, GraphicsContext context){
        for (Duck duck:duckList.keySet()) {
            duck.render(context);

        }
    }
    private void drawRock(List<Rock> rocks, GraphicsContext context) {
        for (Rock rock : rocks) {
            if (rock != null) {
                rock.render(context);
            }
        }
    }

    private void drawLily(Map<Vector,Lily> lilyVectorMap, GraphicsContext context){
        for (Lily lily : lilyVectorMap.values()) {

            if (lily != null) {

                lily.render(context);
            }
        }
    }

    // create duck

    private void spawnDuck(Map<Duck,Vector> duckVectorMap, Map<Vector,Duck> vectorDuckMap, List<Duck> duckList, List<Rock> rocks){
        for (int i=0; i<3; i++){
            Duck litle = new Duck();
            double x = Math.random()*700 + 100;
            double y = Math.random()*500 +100;
            litle.setPosition(x,y);
            boolean collision = false;
            for (Rock littleRock : rocks){
                if (litle.overlaps(littleRock)){
                    collision = true;
                }

            }
            if (!collision){

                if (duckVectorMap.isEmpty() && duckList.isEmpty() ) {

                    duckList.add(litle);
                    duckVectorMap.put(litle,litle.getPosition());
                    vectorDuckMap.put(litle.getPosition(),litle);
                }else {
                    for (int j = 0; j < duckList.size(); j++){
                        if (!duckList.get(j).overlaps(litle)){
                            duckList.add(litle);
                            duckVectorMap.put(litle,litle.getPosition());
                            vectorDuckMap.put(litle.getPosition(),litle);

                        }
                    }
                }
            }
        }


    }

    private void spawnLily(List<Rock> rocks, List<Lily> lilyList, List<Duck> duckList, Map<Vector,Lily> lilyVectorMap, List<Vector> vectorList){
        int lilycount = 15;
        for (int n = 0; n < lilycount; n++){
            Lily lili = new Lily();
            double x = Math.random()*700 + 100;
            double y = Math.random()*500 +100;
            lili.setPosition(x,y);

            boolean collision = false;
            for (Rock littleRock : rocks){
                if (lili.overlaps(littleRock)){
                    collision = true;
                }

            }
            if (!collision) {

                // position collision check
                for (int j = 0; j < lilyList.size(); j++) {
                    boolean collisionLili = lilyList.get(j).overlaps(lili);
                    boolean collisionDuck = false;
                    for (Duck littleDuck:duckList)
                    {
                        if (littleDuck.overlaps(lili))
                        {
                            collisionDuck = true;

                        }
                    }
                    //boolean collisionDuck = duckList.get(j).overlaps(lili);
                    if (!collisionLili && !lilyList.contains(lili) && !collisionDuck) {
                        lilyList.add(lili);
                        vectorList.add(lili.getPosition());
                        lilyVectorMap.put(lili.getPosition(), lili);

                    }

                }
            }
        }



    }

    private void spawnRocks(List<Rock> rocks){
        Rock firstRocks = new Rock();
        Rock secondRocks = new Rock();
        Rock thirdRocks = new Rock();

        secondRocks.setPosition(750,555);
        secondRocks.setImage(new Image("r1.png"));
        thirdRocks.setPosition(600,150);
        thirdRocks.setImage(new Image("r2.png"));

        rocks.add(firstRocks);
        rocks.add(secondRocks);
        rocks.add(thirdRocks);
    }

}