package com.fx.pomodoro.controllers;

import com.fx.pomodoro.model.Attempt;
import com.fx.pomodoro.model.AttemptKind;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Home {

    private final AudioClip applause;
    @FXML
    private VBox container;

    @FXML
    private Label title;

    @FXML
    private TextArea message;


    private Attempt currentAttempt;

    private StringProperty timerText;

    private Timeline timeline;

    public Home() {
        timerText = new SimpleStringProperty();
        setTimerText(0);
        applause = new AudioClip(getClass().getResource("/sounds/applause.mp3").toExternalForm());
    }

    public String getTimerText() {
        return timerText.get();
    }

    public StringProperty timerTextProperty() {
        return timerText;
    }

    public void setTimerText(String text) {
        timerText.set(text);
    }

    public void setTimerText(int remainingSeconds) {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        setTimerText(String.format("%02d:%02d",minutes,seconds));
    }

    private void prepareAttempt(AttemptKind kind){
        reset();
        currentAttempt = new Attempt(kind,"");
        addAttemptStyle(kind);
        title.setText(kind.getDisplayName());
        setTimerText(currentAttempt.getRemainingSeconds());
        timeline = new Timeline();
        timeline.setCycleCount(kind.getTotalSeconds());
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),e -> {
            currentAttempt.tick();
            setTimerText(currentAttempt.getRemainingSeconds());
        }));
        timeline.setOnFinished( e -> {
            saveCurrentAttempt();
            applause.play();
            prepareAttempt(currentAttempt.getAttemptKind() == AttemptKind.FOCUS ?
                    AttemptKind.BREAK : AttemptKind.FOCUS);
        });
    }

    private void saveCurrentAttempt() {
        currentAttempt.setMessage(message.getText());
        currentAttempt.save();
    }

    private void reset() {
        clearAttemptStyle();
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING){
            timeline.stop();
        }
    }

    public void playTimer(){
        container.getStyleClass().add("playing");
        timeline.play();
    }

    public void pauseTimer(){
        container.getStyleClass().remove("playing");
        timeline.pause();
    }

    private void addAttemptStyle(AttemptKind kind) {
        container.getStyleClass().add(kind.toString().toLowerCase());
    }

    private void clearAttemptStyle(){
        container.getStyleClass().remove("playing");
        for (AttemptKind kind : AttemptKind.values()){
            container.getStyleClass().remove(kind.toString().toLowerCase());
        }
    }

    public void handleRestart(ActionEvent actionEvent) {
        prepareAttempt(AttemptKind.FOCUS);
    }

    public void handlePlay(ActionEvent actionEvent) {
        if (currentAttempt == null){
            handleRestart(actionEvent);
        }
        else {
            playTimer();
        }
    }

    public void handlePause(ActionEvent actionEvent) {
        pauseTimer();
    }
}
