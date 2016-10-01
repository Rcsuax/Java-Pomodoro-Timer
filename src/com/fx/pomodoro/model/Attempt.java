package com.fx.pomodoro.model;

public class Attempt {

    private String message;

    private int remainingSeconds;

    private AttemptKind attemptKind;

    public Attempt(AttemptKind kind, String message){
        attemptKind = kind;
        this.message = message;
        remainingSeconds = kind.getTotalSeconds();
    }

    public String getMessage() {
        return message;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public AttemptKind getAttemptKind() {
        return attemptKind;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void tick() {
        remainingSeconds--;
    }

    @Override
    public String toString() {
        return "Attempt{" +
                "message='" + message + '\'' +
                ", remainingSeconds=" + remainingSeconds +
                ", attemptKind=" + attemptKind +
                '}';
    }

    public void save() {
        System.out.printf("Saving: %s %n",this.toString());
    }
}
