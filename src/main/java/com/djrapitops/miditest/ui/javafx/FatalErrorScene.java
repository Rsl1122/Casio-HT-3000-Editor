package com.djrapitops.miditest.ui.javafx;

import com.djrapitops.miditest.State;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;

public class FatalErrorScene extends Scene {

    public FatalErrorScene(State state, Throwable e) {
        super(getText(state, e), 700, 500);
    }

    private static VBox getText(State state, Throwable e) {
        VBox stackTrace = new VBox();
        ObservableList<Node> children = stackTrace.getChildren();

        Text title = new Text("Non-recoverable error occurred and program had to be stopped");
        title.setFont(Constants.FONT_TITLE);
        children.add(title);

        Insets leftPadding = new Insets(0, 0, 0, 8);
        Text info = new Text("\nWhen reporting the issue, please attach the following:\n");
        VBox.setMargin(info, leftPadding);
        children.add(info);

        StringBuilder stackBuilder = new StringBuilder(e.toString());
        addStackTrace(stackBuilder, e.getStackTrace());

        Throwable cause = e.getCause();
        while (cause != null) {
            stackBuilder.append("\ncaused by: ").append(cause.toString());
            addStackTrace(stackBuilder, cause.getStackTrace());
            cause = cause.getCause();
        }

        TextArea stackTraceArea = new TextArea(stackBuilder.toString());
        stackTraceArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        stackTraceArea.prefHeightProperty().bind(stackTrace.heightProperty());
        stackTraceArea.prefWidthProperty().bind(stackTrace.widthProperty());
        children.add(stackTraceArea);

        JFXButton reset = new JFXButton("Restart");
        reset.setStyle(Styles.BG_LIGHT_GREEN);
        reset.setOnAction(event -> state.setView(View.SELECT_MIDI_OUT));
        children.add(reset);

        return stackTrace;
    }

    private static void addStackTrace(StringBuilder stackBuilder, StackTraceElement[] trace) {
        Arrays.stream(trace)
                .map(element -> "\n    " + element.toString())
                .forEach(stackBuilder::append);
    }
}