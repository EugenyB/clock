package controller;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.time.LocalTime;

public class Controller {
    @FXML
    Pane pane;
    @FXML
    Canvas canvas;

    int position = 0;

    public void initialize() {
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        canvas.widthProperty().addListener(e->draw());
        canvas.heightProperty().addListener(e->draw());

//        new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                draw();
//            }
//        }.start();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(1000), e->draw())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        pane.requestFocus();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = pane.getWidth();
        double h = pane.getHeight();
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, w, h);
        double cx = w / 2;
        double cy = h / 2;
        double radius = Math.min(h, w) / 3;
        drawClockFace(gc, cx, cy, radius);
        drawClockHands(gc, cx, cy, radius);
        drawMarker(gc, cx, cy, radius);
    }

    private void drawMarker(GraphicsContext gc, double cx, double cy, double radius) {
        double x = radius * Math.sin(position * Math.PI / 6) + cx;
        double y = -radius * Math.cos(position * Math.PI / 6) + cy;
        gc.setStroke(Color.VIOLET);
        gc.setLineWidth(3);
        gc.strokeRect(x-5, y-5, 10, 10);
        gc.setLineWidth(1);
    }

    private void drawClockHands(GraphicsContext gc, double cx, double cy, double radius) {
        LocalTime time = LocalTime.now();
        double hourAngle = time.getHour() % 12 * Math.PI / 6;
        double minuteAngle = time.getMinute() * Math.PI / 30;
        double secondAngle = time.getSecond() * Math.PI / 30;
        gc.setLineWidth(6);
        gc.setStroke(Color.BLUE);
        double x1 = radius / 3 * Math.sin(hourAngle) + cx;
        double y1 = -radius / 3 * Math.cos(hourAngle) + cy;
        gc.strokeLine(cx, cy, x1, y1);
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        double x2 = 2 * radius / 3 * Math.sin(minuteAngle) + cx;
        double y2 = -2 * radius / 3 * Math.cos(minuteAngle) + cy;
        gc.strokeLine(cx, cy, x2, y2);
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(1);
        double x3 = 3 * radius / 4 * Math.sin(secondAngle) + cx;
        double y3 = -3 * radius / 4 * Math.cos(secondAngle) + cy;
        gc.strokeLine(cx, cy, x3, y3);
    }

    private void drawClockFace(GraphicsContext gc, double cx, double cy, double radius) {
        gc.setStroke(Color.BLACK);
        gc.strokeOval(cx - radius, cy - radius, 2 * radius, 2 * radius);
        for (int i = 0; i < 12; i++) {
            double x1 = radius * Math.sin(i * Math.PI / 6) + cx;
            double y1 = -radius * Math.cos(i * Math.PI / 6) + cy;
            double x2 = 5 * radius / 6 * Math.sin(i * Math.PI / 6) + cx;
            double y2 = -5 * radius / 6 * Math.cos(i * Math.PI / 6) + cy;
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    public void handleKey(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case RIGHT:
                position = (position + 1) % 12;
                break;
            case LEFT:
                position = (position + 11) % 12;
                break;
        }
        draw();
    }
}
