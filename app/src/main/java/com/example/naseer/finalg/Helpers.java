package com.example.naseer.finalg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;


public class Helpers extends GameObject{
     private int score;
     private int speed;
     private Random rand = new Random();
     private Animation animation = new Animation();
     private Bitmap spritesheet;

     public String helperType;

     public Helpers(Bitmap res, int x, int y, int w, int h, int s, int numFrames, String hType)
     {
         super.x = x;
         super.y = y;
         width = w;
         height = h;
         score = s;

         helperType = hType;

         speed = 7 + (int) (rand.nextDouble()*score/30);
         if(speed>40)speed = 40;

         Bitmap[] image = new Bitmap[numFrames];

         spritesheet = res;

         for(int i = 0; i<image.length;i++)
         {
             image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
         }

         animation.setFrames(image);
         animation.setDelay(100-speed);

     }
     public void update()
     {
         x-=speed;
         animation.update();
     }
     public void draw(Canvas canvas)
     {
         try{
             canvas.drawBitmap(animation.getImage(),x,y,null);
         }catch(Exception e){}
     }

     @Override
     public int getWidth()
     {
         return width-10;
     }

 }



