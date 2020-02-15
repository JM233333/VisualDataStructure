package jm233333;

import java.util.ArrayList;

import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import jm233333.ui.CodeTracker;

/**
 * The {@code Director} class is a singleton class that maintains global data and controls the overall program.
 */
public class Director {

    private static Director instance = new Director();
    private Stage primaryStage;
    private ArrayList<Timeline> animationWaitingList, animationPlayingList;
    private int animationCurrentIndex;
    private BooleanProperty animationPlayingProperty;

    /**
     * Creates the unique instance of {@code Director}.
     */
    private Director() {
        animationWaitingList = new ArrayList<>();
        animationPlayingList = null;
        animationCurrentIndex = -1;
        animationPlayingProperty().setValue(false);
    }

    /**
     * Gets the unique instance of {@code Director}.
     *
     * @return the unique instance of {@code Director}
     */
    public static Director getInstance() {
        return instance;
    }

    /**
     * Initializes the unique instance of {@code Director} with a specified {@code Stage}.
     *
     * @param primaryStage the {@code Stage} that is designated as the primary stage
     */
    public void initialize(Stage primaryStage) {
        setPrimaryStage(primaryStage);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public final Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Adds a {@code Timeline} to the animation waiting list.
     *
     * @param timeline the {@code Timeline} that will be added
     */
    public void addTimeline(Timeline timeline) {
        animationWaitingList.add(timeline);
    }

    /**
     * Gets the last {@code Timeline} in the animation waiting list.
     *
     * @return the last {@code Timeline}
     */
    public final Timeline getLastTimeline() {
        if (animationWaitingList.isEmpty()) {
            return null;
        }
        return animationWaitingList.get(animationWaitingList.size() - 1);
    }

    /**
     * If the animation playing list is empty, swaps it with the animation waiting list;
     * Otherwise, sets the animation status to PLAYING.
     */
    public void playAnimation() {
        // check
        if (isAnimationPlaying()) {
            animationPlayingList.get(animationCurrentIndex).play();
            return;
        }
        // switch buffer
        animationPlayingList = animationWaitingList;
        animationWaitingList = new ArrayList<>();
        // check empty
        if (animationPlayingList.isEmpty()) {
            System.out.println("EMRPT");
            return;
        }
        System.out.println(animationPlayingList.get(0).toString());
        // set event listeners
        for (int i = 0; i < animationPlayingList.size(); i ++) {
            final int index = i;
            Timeline timeline = animationPlayingList.get(index);
            final EventHandler<ActionEvent> oldHandler = timeline.getOnFinished();
            timeline.setOnFinished((event) -> {
                if (oldHandler != null) {
                    oldHandler.handle(event);
                }
                if (index + 1 < animationPlayingList.size()) {
                    animationPlayingList.get(index + 1).play();
                    animationCurrentIndex = index + 1;
                    animationPlayingProperty().setValue(true);
                } else {
                    animationCurrentIndex = -1;
                    animationPlayingProperty().setValue(false);
                }
            });
        }
        // play the first timeline
        animationPlayingList.get(0).play();
        animationCurrentIndex = 0;
        animationPlayingProperty.setValue(true);
    }

    /**
     * If the animation playing list is not empty, sets the animation status to PAUSED.
     */
    public void pauseAnimation() {
        if (isAnimationPlaying() && animationCurrentIndex != -1) {
            animationPlayingList.get(animationCurrentIndex).pause();
        }
    }

    /**
     * a {@code BooleanProperty} that represents the animation status.
     * means PLAYING when the value is {@code true}, or PAUSED while {@code false}.
     */
    public final BooleanProperty animationPlayingProperty() {
        if (animationPlayingProperty == null) {
            animationPlayingProperty = new BooleanPropertyBase(false) {
                @Override
                public Object getBean() {
                    return this;
                }
                @Override
                public String getName() {
                    return "animationPlaying";
                }
            };
        }
        return animationPlayingProperty;
    }

    /**
     * Gets the judgement that if the animation status is PLAYING.
     *
     * @return if the animation status is PLAYING
     */
    public boolean isAnimationPlaying() {
        return animationPlayingProperty().getValue();
    }
}
