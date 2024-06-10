package com.example.snakegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private List<Rect> snake;
    private Rect food;
    private Random random;
    private int score;
    private int directionX;
    private int directionY;
    private TextView scoreTextView;
    private boolean isGameRunning = false;
    public SnakeGame(SurfaceView surfaceView, TextView scoreTextView) {
        this.surfaceView = surfaceView; //przypisanie powierzchni do rysowania
        this.surfaceHolder = surfaceView.getHolder(); //pobranie powierzchni do rysowania
        this.paint = new Paint(); //utworzenie obiektu klasy Paint
        this.snake = new ArrayList<>(); //utworzenie listy przechowującej segmenty węża
        this.food = new Rect(); //utworzenie obiektu klasy Rect przechowującego jedzenie
        this.random = new Random();
        this.score = 0; //ustawienie początkowego wyniku
        this.directionX = 0; //ustawienie początkowego kierunku ruchu węża
        this.directionY = 1; //ustawienie początkowego kierunku ruchu węża
        this.scoreTextView = scoreTextView; //przypisanie pola tekstowego do wyświetlania wyniku

        resetGame();
    }

    public void startGame() { //metoda rozpoczynająca grę
        if (!isGameRunning) {
            resetGame();
            isGameRunning = true;
        }
    }

    public void resetGame() { //metoda resetująca grę
        snake.clear();
        snake.add(new Rect(50, 50, 100, 100));
        spawnFood();
        score = 0;
        updateScore();
        drawGame();
    }

    private void spawnFood() { //metoda generująca jedzenie
        int maxWidth = surfaceView.getWidth() - 50;
        int maxHeight = surfaceView.getHeight() - 50;

        if (maxWidth <= 0 || maxHeight <= 0) {
            return;
        }

        int left = random.nextInt(maxWidth);
        int top = random.nextInt(maxHeight);
        food.set(left, top, left + 50, top + 50);
    }

    public void updateDirection(float x, float y) { //metoda aktualizująca kierunek ruchu węża
        if (isGameRunning) {
            if (Math.abs(x) > Math.abs(y)) {
                directionX = x > 0 ? -1 : 1;
                directionY = 0;
            } else {
                directionX = 0;
                directionY = y > 0 ? -1 : 1;
            }

            updateGame();
        }
    }

    private void updateGame() { //metoda aktualizująca stan gry
        if (isGameRunning) {
            Rect head = new Rect(snake.get(0));
            int newX = head.left + directionX * 35;
            int newY = head.top + directionY * 35;

            for (int i = 1; i < snake.size(); i++) {
                Rect segment = snake.get(i);
                if (head.contains(segment.centerX(), segment.centerY())) {
                    endGame();
                    return;
                }
            }

            if (newX < 0) {
                newX = surfaceView.getWidth() - 50;
            } else if (newX >= surfaceView.getWidth()) {
                newX = 0;
            }

            if (newY < 0) {
                newY = surfaceView.getHeight() - 50;
            } else if (newY >= surfaceView.getHeight()) {
                newY = 0;
            }

            head.offsetTo(newX, newY);
            snake.add(0, head);

            if (Rect.intersects(head, food)) {
                score++;
                spawnFood();
            } else {
                snake.remove(snake.size() - 1);
            }

            updateScore();
            drawGame();
        }
    }


    private void endGame() { //metoda kończąca grę
        isGameRunning = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(surfaceView.getContext());
        builder.setTitle("Przegrałeś")
                .setMessage("Twój wynik: " + score)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //przycisk OK zamykający okno dialogowe
                        resetGame(); //resetowanie gry
                    }
                })
                .setCancelable(false)
                .show();
    }



    private void updateScore() { //metoda aktualizująca wynik
        scoreTextView.post(() -> scoreTextView.setText("Score: " + score));
    }

    private void drawGame() { //metoda rysująca grę
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.GREEN);

            for (Rect part : snake) {
                canvas.drawRect(part, paint);
            }

            paint.setColor(Color.RED);
            canvas.drawRect(food, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
