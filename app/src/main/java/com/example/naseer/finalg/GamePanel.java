package com.example.naseer.finalg;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 512;
    public static  int MOVESPEED = -4;
    private long smokeStartTime;
    private long obsStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private Player player2;
    private Player player3;
    private Player playerman;
    private ArrayList<Obstacles> obs;
    private ArrayList<Consumable> consumables;
    private ArrayList<Helpers> helpers;
    private Random rand = new Random();
    Paint paint = new Paint();
    boolean f1= false;
    private long timer1;
    private  boolean powerOn = false;
    private long timer2;

    private long levelStarttimer;

    private int myspeed;

    private int temp2;

    private int level=0;
    private long obsInterval;

    private  boolean paused = false;
    public int countWaterJugs = 0;

    public String collidedHelperType;
    public MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bgmusic);
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String pname;


    int dodges = 0;
     float scaleFactorX = getWidth()/(WIDTH*1.f);
     float scaleFactorY = getHeight()/(HEIGHT*1.f);
    public GamePanel(Context context)
    {
        super(context);

        pref = getContext().getSharedPreferences("MyPref", 0);

        editor = pref.edit();
        pname = pref.getString("playername", "Player 1");



        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;

            }catch(InterruptedException e){e.printStackTrace();}

        }
        mp.stop();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.gamsample), 35, 54, 4,"mem");

        player2 = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.scooter), 47, 50, 2,"scooty");

        player3 = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.gamsample), 35, 54, 4,"mem");

        playerman = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.biker), 47, 50, 2,"man");

        if(OptionsMenu.soundSetting==true) {
            mp.setLooping(true);
            mp.start();

        }
        else


            mp.stop();

        obs = new ArrayList<Obstacles>();
        consumables = new ArrayList<Consumable>();

        helpers = new ArrayList<Helpers>();
        smokeStartTime=  System.nanoTime();
        obsStartTime = System.nanoTime();



        if (!thread.isAlive()) {

            thread = new MainThread(getHolder(), this);


        }
        thread.setRunning(true);
        thread.start();
        myspeed = 4;
        MOVESPEED = -1 * myspeed;
        levelStarttimer = System.currentTimeMillis();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        scaleFactorX = getWidth()/ (WIDTH*1.f);
        scaleFactorY = getHeight()/ (HEIGHT*1.f);
        if(event.getAction()== MotionEvent.ACTION_DOWN) {
            Log.d("unscaled x",event.getX()+"");
            Log.d("scaled x: ",event.getX()*scaleFactorX+"");
           if(  ( (event.getX() > 800*scaleFactorX)&& (event.getX()<1000*scaleFactorX) ) && ( (event.getY() > 10*scaleFactorY)&& (event.getY()<60*scaleFactorY) ) )
            {

                player.setPlaying(!player.getPlaying());
            }
              player.setUp(true);
            return true;
        }
        if(event.getAction()== MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }



        return super.onTouchEvent(event);
    }

    public void update()

    {
        MOVESPEED = -1 * myspeed;
        if(player.getPlaying()) {

            bg.update();
            player.update();

            long obsElapsed = (System.nanoTime()-obsStartTime)/1000000;
            obsInterval = 120000/(30+level*2);
            if(obsElapsed > obsInterval){
                if((System.currentTimeMillis()-levelStarttimer)>120000) {
                    levelStarttimer = System.currentTimeMillis();
                    myspeed = myspeed + myspeed / 2;

                    countWaterJugs = 0;
                    level++;

                }
                System.out.println("making obs");

                double displayProbablity = java.lang.Math.random();


                if(player.playerType.equalsIgnoreCase("mem")) {
                    System.out.println("displayProbablity--------------------------------------" + displayProbablity);
                    if(displayProbablity< 0.3)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.
                            obstacle4), WIDTH+10, HEIGHT - 160, 50,50, player.getScore(), myspeed+1, 1));

                    if(displayProbablity >= 0.3 && displayProbablity < 0.5)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.obstackle1
                    ), WIDTH, HEIGHT - 160, 50, 50, player.getScore(), myspeed+4, 1));


                    if(displayProbablity>= 0.5 && displayProbablity < 0.7)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.pit
                    ), WIDTH+10, HEIGHT - 160, 50, 50, player.getScore(), myspeed, 1));

                    if(displayProbablity< 0.9 && displayProbablity >= 0.7)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle2
                    ), WIDTH+30, HEIGHT - 160, 50, 50, player.getScore(), myspeed, 1));

                    if(displayProbablity>=0.9)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle3
                    ), WIDTH+80, HEIGHT - 160, 50, 50, player.getScore(), myspeed+4, 1));

                    if(displayProbablity< 0.5)
                    consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.coin
                    ), WIDTH+10, HEIGHT - 220, 50, 50, player.getScore(), 1, myspeed+3,false));

                    if(displayProbablity>= 0.5)
                    consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.settings
                    ), WIDTH+60, HEIGHT - 160, 45, 45, player.getScore(), 1, myspeed,false));

                    if(countWaterJugs < 3 + level/2) {
                        if(dodges>=2) {
                            consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.drinke
                            ), WIDTH + 100, HEIGHT - 220, 50, 50, player.getScore(), 1, myspeed, true));
                            countWaterJugs++;
                        }
                    }

                }
                else if(player.playerType.equalsIgnoreCase("scooty")){

                    if(displayProbablity< 0.9 && displayProbablity >= 0.7)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle2
                        ), WIDTH+20, HEIGHT - 160, 50, 50, player.getScore(), myspeed, 1));

                    if(displayProbablity< 0.3)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(),R.drawable.obstacle3
                        ), WIDTH+60, HEIGHT-160, 50, 50, player.getScore(), myspeed, 1));

                    if(displayProbablity>= 0.5 && displayProbablity < 0.7)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.pit
                        ),WIDTH+10,HEIGHT-160,50,50,player.getScore(),myspeed, 1));


                    if(displayProbablity >= 0.9)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle4
                        ), WIDTH+80, HEIGHT - 160, 50, 50, player.getScore(), 1, myspeed+4, false));

                    if(displayProbablity >= 0.3 && displayProbablity < 0.5)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.obstackle1
                        ), WIDTH, HEIGHT - 160, 50, 50, player.getScore(), 1, myspeed+1, false));

                    if(displayProbablity < 0.5)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.coin
                        ), WIDTH+120, HEIGHT - 230, 50, 50, player.getScore(), 1, myspeed,false));

                    if(displayProbablity>= 0.5)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.settings
                        ), WIDTH+160, HEIGHT - 160, 50, 50, player.getScore(), 1, myspeed,false));



                    if(countWaterJugs < 3 + level/2) {
                        if(dodges>=2) {
                            consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.drinke
                            ), WIDTH + 100, HEIGHT - 220, 50, 50, player.getScore(), 1, myspeed, true));
                            countWaterJugs++;
                        }
                    }
                }
                 else {
                    if(displayProbablity < 0.7 && displayProbablity > 0.6)
                        obs.add(new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.pit
                        ), WIDTH + 100, HEIGHT - 160, 50, 50, player.getScore(), myspeed, 10));

                    if(displayProbablity >= 0 && displayProbablity < 0.1)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.settings
                        ), WIDTH, HEIGHT - 160, 50, 50, player.getScore(), 1, myspeed, false));

                    if(displayProbablity >= 0.3 && displayProbablity < 0.5)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle3
                        ), WIDTH+10, HEIGHT - 160, 50, 50, player.getScore(), 1, myspeed+2, false));

                    if(displayProbablity >= 0.1 && displayProbablity < 0.3)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.obstackle1
                        ), WIDTH+10, HEIGHT - 160, 50, 50, player.getScore(), 1, myspeed+1,false));

                    if(displayProbablity >= 0.7 && displayProbablity < 0.9)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.drinke
                        ), WIDTH+10, HEIGHT - 230, 50, 50, player.getScore(), 1, myspeed+3,false));

                    if(displayProbablity >= 0.9 && displayProbablity < 1)
                        consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.obstacle4
                        ), WIDTH+80, HEIGHT - 160, 50, 50, player.getScore(), 1, myspeed+4, false));

                    if(countWaterJugs < 3 + level/2) {
                        if(dodges>=2) {
                            consumables.add(new Consumable(BitmapFactory.decodeResource(getResources(), R.drawable.coin
                            ), WIDTH + 100, HEIGHT - 220, 50, 50, player.getScore(), 1, myspeed, true));
                            countWaterJugs++;
                        }
                    }
                }
                if(displayProbablity <= 0.1)
                    helpers.add(new Helpers(BitmapFactory.decodeResource(getResources(), R.drawable.
                            fuel1), WIDTH + 80, HEIGHT - 230, 50, 50, player.getScore(), 1,"scooty"));
                else if(displayProbablity >= 0.9)
                    helpers.add(new Helpers(BitmapFactory.decodeResource(getResources(), R.drawable.fuel2
                    ), WIDTH + 150, HEIGHT - 230, 50, 50, player.getScore(), 1,"man"));





                obsStartTime = System.nanoTime();
            }

            for(int i = 0; i<obs.size();i++)
            {
                obs.get(i).update();


                if(collision(obs.get(i),player))
                {
                    obs.remove(i);

                    player.setLives(player.getLives()-1);
                    System.out.println("Collided!!!!!!!!!!!!!");
                    break;
                }
                if(obs.get(i).getX()<-100)
                {
                    obs.remove(i);
                    dodges++;
                    player.setScore(player.getScore()+10);
                    break;
                }
            }

            for(int i = 0; i<helpers.size();i++)
            {
                helpers.get(i).update();


                if(collision(helpers.get(i),player))
                {
                    collidedHelperType = helpers.get(i).helperType;

                    helpers.remove(i);
                    int t = player.getScore();
                    player.setScore((int) (t+50* Math.pow(2, level )));
                    f1 = true;
                    timer1 = System.currentTimeMillis();
                    powerOn = true;
                    break;
                }
                if(helpers.get(i).getX() < -100)
                {
                    helpers.remove(i);
                    break;
                }
            }

            for(int i = 0; i<consumables.size();i++)
            {
                consumables.get(i).update();


                if(collision(consumables.get(i),player))
                {

                    if(consumables.get(i).isBonus==true) {
                        int t = player.getScore();
                        player.setScore((int) (t+50* Math.pow(2, level )));
                        consumables.remove(i);
                        break;
                    }
                    else{
                        player.setScore(player.getScore() + 10);
                        consumables.remove(i);
                        break;
                    }


                }
                if(consumables.get(i).getX()<-100)
                {
                    consumables.remove(i);
                    break;
                }
            }



        }
    }
    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }
    @Override
    public void draw(Canvas canvas)
    {

        super.draw(canvas);

        if(powerOn)
        {
            timer2 = System.currentTimeMillis();
            if((timer2-timer1)>10000)
            {
                powerOn = false;
                int k = player.getScore();
                int temp = player.getLives();

                boolean currPlaying = player.getPlaying();
                int y = player.getY();
                obs.clear();
                helpers.clear();
                consumables.clear();
                player = player3;


                player.setScore(k);
                player.setLives(temp);
                player.setPlaying(currPlaying);


                myspeed = temp2;
            }
        }


        if(canvas!=null) {
            final int savedState = canvas.save();
            float scaleFactorX = getWidth()/(WIDTH*1.f);
            float scaleFactorY = getHeight()/(HEIGHT*1.f);


            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if(f1) {

                System.out.println("Inside IF");
                postInvalidate();
                int k = player.getScore();
                int temp = player.getLives();

                temp2 =  myspeed;
                boolean isPlaying = player.getPlaying();
                int y = player.getY();
               if(collidedHelperType.equals("scooty")) {

                   player = player2;
                   myspeed = myspeed + myspeed/2;

                   obs.clear();
                   helpers.clear();
                   consumables.clear();
                    player.setPlaying(isPlaying);
                }
                if(collidedHelperType.equals("man")) {
                 player = playerman;
                    myspeed = myspeed * 2;

                    obs.clear();
                    helpers.clear();
                    consumables.clear();
                    player.setPlaying(isPlaying);
                }

                player.setScore(k);
                player.setLives(temp);


                //
                 f1=false;
            }

            player.draw(canvas);


            for(Obstacles m: obs)
            {
                m.draw(canvas);
            }

            for(Helpers m: helpers)
            {
                m.draw(canvas);
            }


          for(Consumable c: consumables)
            {
                          c.draw(canvas);
            }

            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            canvas.drawText("SCORE : "+player.getScore(), 500, 25, paint);

            canvas.drawText("LIVES : " + player.getLives(), 300, 25, paint);


            canvas.drawText("LEVEL : " + level, 700, 25, paint);

            if(player.getLives()>0) {

                    canvas.drawText("Player : "+pname, 100, 25, paint);

            }
            else {

                String prevscore = pref.getString("score", "0");
                paint.setTextSize(50);
                canvas.drawText("\t\t\t Game Over! \n\n Press back to go to the Menu!", 16, 180, paint);
                editor.putString("score", prevscore + "," + pname + "," + player.getScore()); // Storing string
                editor.commit();
                thread.setRunning(false);

                mp.stop();
            }

            if(player.getPlaying()) {
                paint.setTextSize(50);
                canvas.drawText("PAUSE", 825, 35, paint);
            }
            else{
                paint.setTextSize(50);
                canvas.drawText("RESUME", 825, 35, paint);
            }

            canvas.restoreToCount(savedState);
        }
    }



}


