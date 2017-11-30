package com.example.naseer.finalg;


import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Player extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private int lives;
    private double dya;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    public String playerType;
    public Player(Bitmap res, int w, int h, int numFrames, String pt) {

        x = 100;
        y = GamePanel.HEIGHT - 160;
        dy = 0;
        score = 0;
        height = h;
        width = w;
        lives = 3;
        playerType = pt;

        playing = true;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

    }


    public void setUp(boolean b){up = b;}

    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100)
        {
            startTime = System.nanoTime();
        }
        animation.update();

        if(up){
            if(y>300)
            {
                y = y - 30;
            }



        }
        else{

            if(y<(GamePanel.HEIGHT-160))
            {
                y = y + 30;
            }

        }

    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore(){return score;}
    public void setScore(int score2){this.score = score2;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDYA(){dya = 0;}
    public void resetScore(){score = 0;}

    public void setLives(int l) {this.lives = l;}
    public int getLives () {return lives;}


    public void setH(int h) {this.y=h;}
    public int getH() {return  this.y;}

}

