package com.example.dim_apl;

import android.graphics.Point;
import android.view.View;

import java.util.List;
import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

public class MyView extends View {

    public class Pointer {
        int id;
        int contaLineas;
        float prevX, prevY;
        int color = Color.BLACK;

        public ArrayList<Line> lineas = new ArrayList<Line>();

        public Pointer (){}

        public Pointer(int id){
            this.id = id;
            this.contaLineas = 0;
        }
    }

    public class Line {
        Paint paint = new Paint();
        float prevX, prevY, nextX, nextY;
        int color = Color.BLACK;
        public Line(float prevX, float prevY, float nextX, float nextY, Paint paint, int color){
            this.prevX = prevX;
            this.prevY = prevY;
            this.nextX = nextX;
            this.nextY = nextY;
            this.paint = paint;
            this.color = color;
        }

        public Line(){}

    }
    Random random = new Random();

    Paint paint = new Paint();

    float prevX, prevY, nextX, nextY;
    ArrayList<Pointer> contactos = new ArrayList<Pointer>();

    int color = Color.BLACK;

    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int j = 0; j<contactos.size(); j++){
            Pointer aDibujar = contactos.get(j);
            for (int i = 0; i<aDibujar.contaLineas;i++){
                Line linea = aDibujar.lineas.get(i);
                canvas.drawLine(linea.prevX, linea.prevY, linea.nextX, linea.nextY,linea.paint);
            }
        }
        paint.setColor(this.color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int mActiveID = event.getPointerId(event.getActionIndex());
        int mActiveIndex = event.getActionIndex();

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                System.out.println("SE HA PULSADO:: "+ mActiveID);
                Pointer puntero = new Pointer(mActiveID);
                puntero.prevX = event.getX(mActiveIndex);
                puntero.prevY = event.getY(mActiveIndex);
                puntero.color = Color.rgb(random.nextInt(255), random.nextInt( 255), random.nextInt(255));
                contactos.add(puntero);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                System.out.println("SE HA LEVANTADO UN POINTER");
                Pointer a_remover = null;
                for (int i = 0; i<event.getPointerCount(); i++){
                    if (contactos.get(i).id == mActiveID){
                        a_remover = contactos.get(i);
                    }
                }
                if (a_remover != null){
                    System.out.println("Se ha removido el ID: " + a_remover.id);
                    contactos.remove(a_remover);
                }
                this.invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                Pointer pointer_add_line = null;
                for (int i = 0; i < event.getPointerCount(); i++){
                    if (contactos.get(i).id == event.getPointerId(i)){
                        pointer_add_line = contactos.get(i);
                        pointer_add_line.lineas.add(new Line(pointer_add_line.prevX, pointer_add_line.prevY, event.getX(i), event.getY(i), this.paint, pointer_add_line.color));
                        pointer_add_line.contaLineas++;
                        pointer_add_line.prevX= event.getX(i);
                        pointer_add_line.prevY = event.getY(i);
                    }
                }
                this.invalidate();
                break;
        }
        return true;
    }


}