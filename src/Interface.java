import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Interface extends Application {
    @Override
    public void start(Stage stage){
        Log logger = new Log();

        Group group = new Group();
        Group labelGroup = new Group();
        Group textFieldGroup = new Group();
        Group buttonGroup = new Group();

        Label numberLabel = new Label("Number:");
        Label repeatsLabel = new Label("Repeats:");
        TextField numberTF = new TextField();
        TextField repeatsTF = new TextField();
        Button startButton = new Button("Check!");
        TextArea resultTextArea = new TextArea();
        TextArea logTextArea = new TextArea();

        numberLabel.setLayoutX(10);
        numberLabel.setLayoutY(10);
        numberLabel.setFont(Font.font("Consolas",18));
        labelGroup.getChildren().add(numberLabel);

        repeatsLabel.setLayoutX(500);
        repeatsLabel.setLayoutY(10);
        repeatsLabel.setFont(Font.font("Consolas",18));
        labelGroup.getChildren().add(repeatsLabel);

        numberTF.setLayoutX(80);
        numberTF.setLayoutY(10);
        numberTF.setPromptText("Number...");
        numberTF.setPrefSize(400,20);
        textFieldGroup.getChildren().add(numberTF);

        repeatsTF.setLayoutX(580);
        repeatsTF.setLayoutY(10);
        repeatsTF.setPromptText("Repeats...");
        repeatsTF.setPrefSize(100,20);
        textFieldGroup.getChildren().add(repeatsTF);

        startButton.setLayoutX(700);
        startButton.setLayoutY(10);
        startButton.setPrefSize(70,20);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logger.initialEntry();
                try {
                    long inputNumber = Long.parseLong(numberTF.getText());
                    if (inputNumber % 2 == 0) {
                        throw new Exception("Given number is even");
                    }
                    if(inputNumber>Math.pow(2,62)-1){
                        throw new InputMismatchException();
                    }

                    long repeats = Long.parseLong(repeatsTF.getText());

                    logger.writeLog("Input number: "+inputNumber+"\n"+"Repeats: "+repeats,true);

                    Backend b = new Backend(inputNumber,repeats);
                    boolean result = b.run();

                    if(result){
                        resultTextArea.setText(resultTextArea.getText()+"Number "+inputNumber+" is prime."+"\n");
                        resultTextArea.requestFocus();
                        resultTextArea.end();
                    }else{
                        resultTextArea.setText(resultTextArea.getText()+"Number "+inputNumber+" is not prime."+"\n");
                        resultTextArea.requestFocus();
                        resultTextArea.end();
                    }

                    String[] text=logger.test();
                    logTextArea.setText("");
                    if (text!=null){
                        for (int i=0;i<text.length;i++) {
                            logTextArea.appendText((text[i])+"\n");
                        }
                    }
                    logTextArea.setScrollTop(Double.MAX_VALUE);

                }catch (InputMismatchException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("InputMismatchException");
                    alert.setContentText("Given number is greater than 2^62-1 or not a number.");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });

                }catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        buttonGroup.getChildren().add(startButton);

        resultTextArea.setLayoutX(10);
        resultTextArea.setLayoutY(50);
        resultTextArea.setMinHeight(100);
        resultTextArea.setMaxHeight(100);
        resultTextArea.setMinWidth(765);
        resultTextArea.setMaxWidth(765);
        resultTextArea.setWrapText(true);
        resultTextArea.setEditable(false);
        resultTextArea.setFont(Font.font("Consolas", 16));
        resultTextArea.end();
        textFieldGroup.getChildren().add(resultTextArea);

        logTextArea.setLayoutX(10);
        logTextArea.setLayoutY(170);
        logTextArea.setMinHeight(350);
        logTextArea.setMaxHeight(350);
        logTextArea.setMinWidth(765);
        logTextArea.setMaxWidth(765);
        logTextArea.setWrapText(false);
        logTextArea.setEditable(false);
        logTextArea.setFont(Font.font("Consolas", 14));
        logTextArea.end();
        textFieldGroup.getChildren().add(logTextArea);

        //initial parse
        String[] text = null;
        try {
            text = logger.test();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logTextArea.setText("");
        if (text!=null){
            for (int i=0;i<text.length;i++) {
                logTextArea.appendText((text[i])+"\n");
            }
        }
        logTextArea.setScrollTop(Double.MAX_VALUE);
        //

        group.getChildren().addAll(labelGroup,textFieldGroup,buttonGroup);

        Scene scene = new Scene(group, Color.rgb(245,245,245));
        stage.setScene(scene);
        stage.setTitle("Miller-Rabin");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    public static void show(){
        Application.launch();
    }
}
