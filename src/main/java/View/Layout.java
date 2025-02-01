package View;

import Data.Enums.Colors;
import Data.Enums.Direction;
import Logic.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Layout {
    //Ui Declaration
    private ScrollPane movelistScrollPane;
    private GridPane boardGrid;

    //Buttondeclaration
    private RadioButton red = new RadioButton("Red ");
    private RadioButton green = new RadioButton("Green ");
    private RadioButton blue = new RadioButton("Blue ");
    private RadioButton yellow = new RadioButton("Yellow ");
    private HBox colorButtons;
    private Button left = new Button("←");
    private Button up = new Button("↑");
    private Button down = new Button("↓");
    private Button right = new Button("→");
    private HBox directionButtons;
    private Button aiSetupButton = new Button("AI Setup");
    private Button moveAIButton = new Button("Make AI Move");
    private Button solveAIButton = new Button("Solve with AI");
    private Button showKeyBindings = new Button("Show Keybindings");
    private Button revertLastMoveButton = new Button("Revert Last Move");

    //Labeldeclaraiton
    private Label score = new Label("Score: 0");
    private Label moveScore = new Label ("Moves: ");
    private Label movelist = new Label("Made moves: ");

    private Colors selectedColor;
    private Direction selectedDirection;
    private ArrayList<String> moveListlist = new ArrayList<>();

    private Scene LayoutScene;
    private Game game;
    private AIWindow aiWindow;
    private DisplayFx dfx;

    //@todo: Dark Mode
    public Layout(Game game,DisplayFx dfx, AIWindow aiWindow){
        this.game = game;
        this.dfx=dfx;
        this.aiWindow=aiWindow;
        movelistScrollPane = drawListOfMoves();
        HBox gridAndMoveList = new HBox(dfx.getBoardGrid(), movelistScrollPane);
        //vBoxAll.setStyle("-fx-background-color: #1d1c21;");
        drawButtons();

        VBox aiButtons = new VBox(aiSetupButton,moveAIButton,solveAIButton);
        aiButtons.setAlignment(Pos.BOTTOM_LEFT);
        HBox aiHButtons = new HBox(aiButtons);

        moveScore.setText("Moves: "+game.getState().getMoveList().size() );
        VBox revertAndKey = new VBox(revertLastMoveButton,showKeyBindings);
        HBox revertButtonH = new HBox(revertAndKey);

        HBox scoreH = new HBox(score);


        HBox hBoxAllButtons = new HBox(aiHButtons, revertButtonH ,scoreH, colorButtons, directionButtons , moveScore);
        hBoxAllButtons.setSpacing(50);
        hBoxAllButtons.setAlignment(Pos.BASELINE_CENTER);

        hBoxAllButtons.setPadding(new Insets(10,0,0,0));
        VBox vBoxAll = new VBox(gridAndMoveList, hBoxAllButtons);
        LayoutScene = new Scene(vBoxAll, 950, 920);
        generateKeyhandlers();
    }


    public void drawButtons(){
        revertLastMoveButton = drawRevertButton();
        aiSetupButton = drawAISetupButton();
        moveAIButton = drawMoveAIButton();
        solveAIButton = drawSolveWithAiButton();
        drawColorButtons();
        drawDirectionButtons();
    }

    public void drawColorButtons(){
        red.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedColor = Colors.RED;
            }
        });


        green.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedColor = Colors.GREEN;
            }
        });

        blue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedColor = Colors.BLUE;
            }
        });

        yellow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedColor = Colors.YELLOW;
            }
        });


        selectedColor = Colors.RED;
        red.setSelected(true);

        ToggleGroup radioGroupColor = new ToggleGroup();
        red.setToggleGroup(radioGroupColor);
        green.setToggleGroup(radioGroupColor);
        blue.setToggleGroup(radioGroupColor);
        yellow.setToggleGroup(radioGroupColor);

        this.colorButtons = new HBox(red, green, blue, yellow);
    }

    public Button drawRevertButton (){
        revertLastMoveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                game.revertMove();
                dfx.redrawRobots();
                if (moveListlist.size() > 0){
                    moveListlist.remove(moveListlist.size()-1);
                }
                redrawMovelist();
                dfx.redrawRobots();
            }
        });

        return revertLastMoveButton;
    }


    public void drawDirectionButtons(){
        left.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedDirection = Direction.LEFT;
                dfx.moveRobot(selectedDirection , selectedColor );
            }
        });

        up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedDirection = Direction.UP;
                dfx.moveRobot(selectedDirection , selectedColor );

            }
        });

        down.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedDirection = Direction.DOWN;
                dfx.moveRobot(selectedDirection , selectedColor );

            }
        });

        right.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedDirection = Direction.RIGHT;
                dfx.moveRobot(selectedDirection , selectedColor );

            }
        });

        VBox upAndDown = new VBox(up,down);
        this.directionButtons = new HBox(left, upAndDown , right );
        this.directionButtons.setAlignment(Pos.BOTTOM_CENTER);
        this.directionButtons.setPadding(new Insets(20, 0, 0, 0));

    }
    public Button drawAISetupButton(){
        aiSetupButton.setOnAction(new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent event) {
                aiWindow.createWindow();
            }
        });

        return aiSetupButton;
    }

    public Button drawMoveAIButton(){
        moveAIButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dfx.executeNextMove();
            }
        });

        return moveAIButton;
    }

    public Button drawSolveWithAiButton(){
        solveAIButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dfx.executeNextSequence();
            }
        });

        return solveAIButton;
    }

    public void generateKeyhandlers (){
        LayoutScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W: up.fire(); break;
                    case A: left.fire(); break;
                    case S: down.fire(); break;
                    case D: right.fire(); break;
                    case DIGIT1: red.fire(); break;
                    case DIGIT2: green.fire(); break;
                    case DIGIT3: blue.fire(); break;
                    case DIGIT4: yellow.fire(); break;
                    case BACK_SPACE: revertLastMoveButton.fire(); break;
                    case N: dfx.executeNextMove(); break;
                    case ENTER: dfx.executeNextSequence(); break;
                }
            }
        });

    }

    public ScrollPane drawListOfMoves (){
        ScrollPane movelistScrollPane = new ScrollPane();
        movelist.setPadding(new Insets(5, 0, 0,0 ));
        movelistScrollPane.setContent(movelist);
        //height of Scoreboard times 50
        movelistScrollPane.setPrefViewportHeight(15*50);
        movelistScrollPane.setPrefViewportWidth(120);
        //so we stay at the bottom
        movelistScrollPane.setVvalue(movelistScrollPane.getVmax());

        return movelistScrollPane;
    }

    public void addMovelistEntry(Colors color, Direction dir){
        moveListlist.add('\n' + color.toString() + "  "+ "\t" + dir.toString() );
        redrawMovelist();
    }

    public void redrawMovelist (){
        String movelistString = "Made moves: ";
        for (String move : moveListlist){
            movelistString = movelistString + move;
        }
        movelist.setText(movelistString);
        //scrolls to the bottom
        movelistScrollPane.setVvalue(movelistScrollPane.getVmax());
    }

    public Scene getLayoutScene() {
        return LayoutScene;
    }

    public void setLayoutScene(Scene layoutScene) {
        LayoutScene = layoutScene;
    }

    public ArrayList<String> getMoveListlist() {
        return moveListlist;
    }

    public void setMoveListlist(ArrayList<String> moveListlist) {
        this.moveListlist = moveListlist;
    }

    public Label getScore() {
        return score;
    }

    public void setScore(Label score) {
        this.score = score;
    }

    public Label getMoveScore() {
        return moveScore;
    }

    public void setMoveScore(Label moveScore) {
        this.moveScore = moveScore;
    }
}
