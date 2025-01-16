package View;

import Data.*;
import Data.Enums.*;
import Data.Testing.SavedConfigs;
import Logic.AI;
import Logic.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DisplayFx {

    //Associated Classes
    private Game game;
    private Logic.AI ai;
    private DisplayUtility utility = new DisplayUtility();
    private AIWindow aiWindow;
    private Stage window;

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


    //Data

    //Current state variables
    private Colors selectedColor;
    private Direction selectedDirection;
    private boolean crashWall;
    private int selectedVicAlgo;
    private int setupVicAlgo;
    private ArrayList<MoveCommand> visSeq=new ArrayList<MoveCommand>();


    private ArrayList<String> moveListlist = new ArrayList<>();

    public DisplayFx (AI ai, Game game, Stage window){
        this.ai=ai;
        this.game=game;
        this.window=window;
        this.aiWindow=new AIWindow(game,ai,this);
        playGame();
    }

    //Main Game
    public void playGame() {

        //Basic UI Setup
        window.setTitle("Ricochet Robots");
        window.setResizable(true);

        drawEmptyBoard();
        drawObstacles();
        drawVP();
        drawRobots();

        Scene scene = createLayout();
        generateKeyhandlers(scene);

        window.setScene(scene);
        window.show();

    }

    //@todo: DarkMode
    //@todo: Refactor
    public Scene createLayout(){
        movelistScrollPane = drawListOfMoves();
        HBox gridAndMoveList = new HBox(boardGrid , movelistScrollPane);
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
        Scene scene = new Scene(vBoxAll, 950, 920);
        return scene;
    }

    public void drawButtons(){
        revertLastMoveButton = drawRevertButton();
        aiSetupButton = drawAISetupButton();
        moveAIButton = drawMoveAIButton();
        solveAIButton = drawSolveWithAiButton();
        drawColorButtons();
        drawDirectionButtons();
    }

    public void generateKeyhandlers (Scene scene){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
                    case N: executeNextMove(); break;
                    case ENTER: executeNextSequence(); break;
                }
            }
        });

    }

    public void drawEmptyBoard(){

        boardGrid = new GridPane();
        boardGrid.setHgap(0);
        boardGrid.setVgap(0);
        boardGrid.setPadding(new Insets(10, 10, 0, 10));

        for (int i = 0; i <= game.getConfig().getLength(); i++) {
            for (int j = 0; j <= game.getConfig().getHeight(); j++) {
                Random random = new Random();
                int randomNumber =random.nextInt(3);
                if (randomNumber == 0){
                    boardGrid.add(new ImageView(new Image(View.DisplayFx.class.getResource("SpriteFloor.jpg").toExternalForm())), i, j);
                }if (randomNumber == 1){
                    boardGrid.add(new ImageView(new Image(View.DisplayFx.class.getResource("SpriteFloor2.jpg" ).toExternalForm())), i, j);
                }if (randomNumber == 2){
                    boardGrid.add(new ImageView(new Image(View.DisplayFx.class.getResource("SpriteFloor3.jpg" ).toExternalForm())), i, j);
                }

            }
        }
    }

    public void drawVP(){
        ArrayList<VictorySpawn> victorySpawns = game.getState().getBoard().getVictorySpawns();
        for (VictorySpawn victorySpawn : victorySpawns) {
            Colors colors = victorySpawn.getColor();
            Image newVPSpawn = null;
            switch (colors) {
                case RED:
                    newVPSpawn = new Image(View.DisplayFx.class.getResource("VRedSpawn.png").toExternalForm());
                    break;
                case GREEN:
                    newVPSpawn = new Image(View.DisplayFx.class.getResource("VGreenSpawn.png").toExternalForm());
                    break;
                case BLUE:
                    newVPSpawn = new Image(View.DisplayFx.class.getResource("VBlueSpawn.png").toExternalForm());
                    break;
                case YELLOW:
                    newVPSpawn = new Image(View.DisplayFx.class.getResource("VYellowSpawn.png").toExternalForm());
                    break;
            }
            boardGrid.add(new ImageView(newVPSpawn), victorySpawn.getCoord().getX(), victorySpawn.getCoord().getY());
        }

        Colors colors = game.getState().getBoard().getVictoryPoint().getColor();
        Image newVP = null;
        switch (colors) {
            case RED:
                newVP = new Image(View.DisplayFx.class.getResource("VRed.png").toExternalForm());
                break;
            case GREEN:
                newVP = new Image(View.DisplayFx.class.getResource("VGreen.png").toExternalForm());
                break;
            case BLUE:
                newVP = new Image(View.DisplayFx.class.getResource("VBlue.png").toExternalForm());
                break;
            case YELLOW:
                newVP = new Image(View.DisplayFx.class.getResource("VYellow.png").toExternalForm());
                break;
        }
        boardGrid.add(new ImageView(newVP), game.getState().getBoard().getVictoryPoint().getCoord().getX(), game.getState().getBoard().getVictoryPoint().getCoord().getY());
    }

    public void drawObstacles(){
        ArrayList<Obstacle> obstacles = game.getState().getBoard().getObstacles();
        for (Obstacle obstacle : obstacles) {
            ObsType type = obstacle.getType();
            // if E is printed an Obs is missing its  type
            Image newObs = null;
            switch (type) {
                case VERTICAL:
                    newObs = new Image(View.DisplayFx.class.getResource("SpriteWallVertical.png").toExternalForm());
                    break;
                case HORIZONTAL:
                    newObs = new Image(View.DisplayFx.class.getResource("SpriteWallHorizontal.png").toExternalForm());
                    break;
            }

            boardGrid.add(new ImageView(newObs), obstacle.getCoord1().getX(), obstacle.getCoord1().getY());
        }
    }

    public void drawRobots(){
        ArrayList<Robot> robots = game.getState().getBoard().getRobots();
        for (Robot robot : robots) {
            Colors colors = robot.getColor();
            Image newRobot = null;
            switch (colors) {
                case RED:
                    newRobot = new Image(View.DisplayFx.class.getResource("RobotRed.png").toExternalForm());
                    break;
                case GREEN:
                    newRobot = new Image(View.DisplayFx.class.getResource("RobotGreen.png").toExternalForm());
                    break;
                case BLUE:
                    newRobot = new Image(View.DisplayFx.class.getResource("RobotBlue.png").toExternalForm());
                    break;
                case YELLOW:
                    newRobot = new Image(View.DisplayFx.class.getResource("RobotYellow.png").toExternalForm());
                    break;
            }
            boardGrid.add(new ImageView(newRobot), robot.getCoord().getX(), robot.getCoord().getY());
        }
    }

    public ScrollPane drawListOfMoves (){
        ScrollPane movelistScrollPane = new ScrollPane();
        movelist.setPadding(new Insets(5, 0, 0,0 ));
        movelistScrollPane.setContent(movelist);
        movelistScrollPane.setPrefViewportHeight(game.getConfig().getHeight()*50);
        movelistScrollPane.setPrefViewportWidth(120);
        //so we stay at the bottom
        movelistScrollPane.setVvalue(movelistScrollPane.getVmax());

        return movelistScrollPane;
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
                executeNextMove();
            }
        });

        return moveAIButton;
    }

    public Button drawSolveWithAiButton(){
        solveAIButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                executeNextSequence();
            }
        });

        return solveAIButton;
    }

    //Searches for TreeSearch solution if successful visSeq will be refilled
    //otherwise visSeq is emptyList
    public void executeNextSequence(){
        int movesUsed = -1;
        long timerBegin = System.currentTimeMillis();

        while (!this.visSeq.isEmpty()){
            executeNextMove();
            redrawMovelist();
            redrawRobots();
        }
        this.visSeq=ai.createSeq().getMoveCommands();
        if (this.visSeq.isEmpty()){
            this.visSeq=new ArrayList<MoveCommand>();
            System.out.println("no result");
            if(ai.isInterrupted()){
                aiWindow.updateAnalysisLabel(10,0,0,1);
            }else{
                aiWindow.updateAnalysisLabel(10,0,1,0);
            }
            game.forceNewVictoryPoint();
            this.moveListlist = new ArrayList<>();
            redrawMovelist();
            redrawRobots();
            return;
        }
        else{
            redrawMovelist();
            redrawRobots();
            System.out.println("Solution found");
            movesUsed = this.visSeq.size();
            System.out.println("Moves: "+ movesUsed);

        }
        long timerEnd = System.currentTimeMillis();
        long timeDiff = timerEnd-timerBegin;
        float timeUsed = (float) timeDiff* (float) 0.001;
        aiWindow.updateAnalysisLabel(timeUsed, movesUsed,0,0);
    }

    public void executeNextMove(){
        if(!visSeq.isEmpty()){
            MoveCommand curr = visSeq.get(0);
            this.setSelectedColor(curr.getColor());
            this.setSelectedDirection(curr.getDir());
            this.moveRobot(curr.getDir(), curr.getColor());
            visSeq.remove(curr);
        }

    }


    //Visually Moves the Robot, Calls certain redraw Methods
    public boolean moveRobot(Direction selectedDirection, Colors selectedColor){
        MoveCommand mCmd = new MoveCommand(selectedColor, selectedDirection);
        // used to determine if the robot hit a wall  (then returns -1)
        //clears movelist if victorypoint is reached
        switch(game.moveRobot(mCmd)){
            case -1:
                crashWall =true;
                break;
            case 0:
                crashWall = false;
                addMovelistEntry(selectedColor,selectedDirection);
                break;
            case 1:
                crashWall=false;
                this.moveListlist = new ArrayList<>();
                redrawMovelist();
                break;
        }
        redrawRobots();
        score.setText("Score: " + game.getState().getScore());
        return crashWall;
    }

    public void redrawRobots(){
        int distanceToVps = (game.getConfig().getHeight()+1) * (game.getConfig().getLength()+1) //all fields
                + game.getConfig().getObstacleList().size() ;                                      //all obs

        //we delete all robots and VPs redraw them new (less hazzle than to identify which Robot moved this turn)
        for (int i = 0 ; i <= (game.getState().getBoard().getRobots().size() + game.getState().getBoard().getVictorySpawns().size()  ); i++){
            boardGrid.getChildren().remove(distanceToVps);
        }
        drawRobots();
        drawVP();
        moveScore.setText("Moves: "+game.getState().getMoveList().size() );

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
                redrawRobots();
                if (moveListlist.size() > 0){
                    moveListlist.remove(moveListlist.size()-1);
                }
                redrawMovelist();
                redrawRobots();
            }
        });

        return revertLastMoveButton;
    }


    public void drawDirectionButtons(){
        left.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedDirection = Direction.LEFT;
                moveRobot(selectedDirection , selectedColor );
            }
        });

        up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedDirection = Direction.UP;
                moveRobot(selectedDirection , selectedColor );

            }
        });

        down.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedDirection = Direction.DOWN;
                moveRobot(selectedDirection , selectedColor );

            }
        });

        right.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectedDirection = Direction.RIGHT;
                moveRobot(selectedDirection , selectedColor );

            }
        });

        VBox upAndDown = new VBox(up,down);
        this.directionButtons = new HBox(left, upAndDown , right );
        this.directionButtons.setAlignment(Pos.BOTTOM_CENTER);
        this.directionButtons.setPadding(new Insets(20, 0, 0, 0));

    }

    //Getter and Setter madness
    public Colors getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Colors selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Direction getSelectedDirection() {
        return selectedDirection;
    }

    public void setSelectedDirection(Direction selectedDirection) {
        this.selectedDirection = selectedDirection;
    }

    public RadioButton getRed() {
        return red;
    }

    public void setRed(RadioButton red) {
        this.red = red;
    }

    public RadioButton getGreen() {
        return green;
    }

    public void setGreen(RadioButton green) {
        this.green = green;
    }

    public RadioButton getBlue() {
        return blue;
    }

    public void setBlue(RadioButton blue) {
        this.blue = blue;
    }

    public RadioButton getYellow() {
        return yellow;
    }

    public void setYellow(RadioButton yellow) {
        this.yellow = yellow;
    }

    public Button getLeft() {
        return left;
    }

    public void setLeft(Button left) {
        this.left = left;
    }

    public Button getUp() {
        return up;
    }

    public void setUp(Button up) {
        this.up = up;
    }

    public Button getDown() {
        return down;
    }

    public void setDown(Button down) {
        this.down = down;
    }

    public Button getRight() {
        return right;
    }

    public void setRight(Button right) {
        this.right = right;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isCrashWall() {
        return crashWall;
    }

    public void setCrashWall(boolean crashWall) {
        this.crashWall = crashWall;
    }

    public AI getAi() {
        return ai;
    }

    public void setAi(AI ai) {
        this.ai = ai;
    }
}