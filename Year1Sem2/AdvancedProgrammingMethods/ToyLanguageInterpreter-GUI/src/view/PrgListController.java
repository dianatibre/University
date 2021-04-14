package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import repository.Repository;

public class PrgListController implements Initializable {

    static Repository myFirstRepository, mySecondRepository, myThirdRepository, myFourthRepository, myLastRepository;
    static Controller myFirstController, mySecondController, myThirdController, myFourthController, myLastController;
    //static IStmt firstProgram, secondProgram, thirdProgram, fourthProgram, lastProgram;
    @FXML
    ListView<Controller> myPrgList;
    @FXML
    Button runButton;

    public void setUp() {
        myFirstRepository = new Repository("firstProgramLog.txt");
        myFirstController = new Controller(myFirstRepository);
        mySecondRepository = new Repository("secondProgramLog.txt");
        mySecondController = new Controller(mySecondRepository);
        myThirdRepository = new Repository("thirdProgramLog.txt");
        myThirdController = new Controller(myThirdRepository);
        myFourthRepository = new Repository("fourthProgramLog.txt");
        myFourthController = new Controller(myFourthRepository);
        myLastRepository = new Repository("lastProgramLog.txt");
        myLastController = new Controller(myLastRepository);
        firstProgram = new IfStmt(new ConstExp(10), new CompStmt(new CompStmt(new AssignStmt("v", new ConstExp(5)), new AssignStmt("m", new ConstExp(6))), new PrintStmt(new ArithExp('/', new VarExp("v"), new ConstExp(3)))), new PrintStmt(new ConstExp(100)));
        secondProgram = new CompStmt(new OpenRFile("f","test.in"), new CompStmt(new ReadFile(new VarExp("f"), "c"), new CompStmt(new PrintStmt(new VarExp("c")), new CompStmt(new IfStmt(new VarExp("c"), new CompStmt(new ReadFile(new VarExp("f"), "c"), new PrintStmt(new VarExp("c"))), new PrintStmt(new ConstExp(0))), new CloseRFile(new VarExp("f"))))));
        thirdProgram = new CompStmt(new AssignStmt("v", new ConstExp(10)), new CompStmt(new HeapAllocation("v", new ConstExp(20)), new CompStmt(new HeapAllocation("a", new ConstExp(22)), new CompStmt(new HeapWriting("a", new ConstExp(30)), new CompStmt(new PrintStmt(new VarExp("a")), new CompStmt(new PrintStmt(new HeapRead("a")), new AssignStmt("a", new ConstExp(0))))))));
        fourthProgram = new CompStmt(new AssignStmt("v", new ConstExp(6)), new WhileStmt(new ArithExp('-', new VarExp("v"), new ConstExp(4)), new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ConstExp(1))))));
        lastProgram = new CompStmt(new AssignStmt("v", new ConstExp(10)), new CompStmt(new HeapAllocation("a", new ConstExp(22)), new CompStmt(new ForkStmt(new CompStmt(new HeapWriting("a", new ConstExp(30)), new CompStmt(new AssignStmt("v", new ConstExp(32)), new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new HeapRead("a")))))), new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new HeapRead("a"))))));
        MyIStack<IStmt> exeStack1 = new MyStack<IStmt>();
        MyIDictionary<String, Integer> symTable1 = new MyDictionary<String, Integer>();
        MyIList<Integer> out1 = new MyList<Integer>();
        MyIDictionary<Integer, MyTuple<String, BufferedReader>> fileTable1 = new MyDictionary<Integer, MyTuple<String, BufferedReader>>();
        MyIDictionary<Integer, Integer> heap1 = new MyDictionary<Integer, Integer>();
        PrgState myPrgState1 = new PrgState(exeStack1, symTable1, out1, fileTable1, heap1, firstProgram);
        myFirstController.addProgram(myPrgState1);
        MyIStack<IStmt> exeStack2 = new MyStack<IStmt>();
        MyIDictionary<String, Integer> symTable2 = new MyDictionary<String, Integer>();
        MyIList<Integer> out2 = new MyList<Integer>();
        MyIDictionary<Integer, MyTuple<String, BufferedReader>> fileTable2 = new MyDictionary<Integer, MyTuple<String, BufferedReader>>();
        MyIDictionary<Integer, Integer> heap2 = new MyDictionary<Integer, Integer>();
        PrgState myPrgState2 = new PrgState(exeStack2, symTable2, out2, fileTable2, heap2, secondProgram);
        mySecondController.addProgram(myPrgState2);
        MyIStack<IStmt> exeStack3 = new MyStack<IStmt>();
        MyIDictionary<String, Integer> symTable3 = new MyDictionary<String, Integer>();
        MyIList<Integer> out3 = new MyList<Integer>();
        MyIDictionary<Integer, MyTuple<String, BufferedReader>> fileTable3 = new MyDictionary<Integer, MyTuple<String, BufferedReader>>();
        MyIDictionary<Integer, Integer> heap3 = new MyDictionary<Integer, Integer>();
        PrgState myPrgState3 = new PrgState(exeStack3, symTable3, out3, fileTable3, heap3, thirdProgram);
        myThirdController.addProgram(myPrgState3);
        MyIStack<IStmt> exeStack4 = new MyStack<IStmt>();
        MyIDictionary<String, Integer> symTable4 = new MyDictionary<String, Integer>();
        MyIList<Integer> out4 = new MyList<Integer>();
        MyIDictionary<Integer, MyTuple<String, BufferedReader>> fileTable4 = new MyDictionary<Integer, MyTuple<String, BufferedReader>>();
        MyIDictionary<Integer, Integer> heap4 = new MyDictionary<Integer, Integer>();
        PrgState myPrgState4 = new PrgState(exeStack4, symTable4, out4, fileTable4, heap4, fourthProgram);
        myFourthController.addProgram(myPrgState4);
        MyIStack<IStmt> exeStack5 = new MyStack<IStmt>();
        MyIDictionary<String, Integer> symTable5= new MyDictionary<String, Integer>();
        MyIList<Integer> out5 = new MyList<Integer>();
        MyIDictionary<Integer, MyTuple<String, BufferedReader>> fileTable5 = new MyDictionary<Integer, MyTuple<String, BufferedReader>>();
        MyIDictionary<Integer, Integer> heap5 = new MyDictionary<Integer, Integer>();
        PrgState myLastPrgState = new PrgState(exeStack5, symTable5, out5, fileTable5, heap5, lastProgram);
        myLastController.addProgram(myLastPrgState);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUp();
        ObservableList<Controller> myData = FXCollections.observableArrayList();
        myData.add(myFirstController);
        myData.add(mySecondController);
        myData.add(myThirdController);
        myData.add(myFourthController);
        myData.add(myLastController);
        myPrgList.setItems(myData);
        myPrgList.getSelectionModel().selectFirst();
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent e) {
                Stage programStage = new Stage();
                Parent programRoot;
                Callback<Class<?>, Object> controllerFactory = type -> {
                    if (type == PrgRunController.class) {
                        return new PrgRunController(myPrgList.getSelectionModel().getSelectedItem());
                    } else {
                        try {
                            return type.newInstance() ;
                        } catch (Exception exc) {
                            System.err.println("Could not create controller for "+type.getName());
                            throw new RuntimeException(exc);
                        }
                    }
                };
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProgramLayout.fxml"));
                    fxmlLoader.setControllerFactory(controllerFactory);
                    programRoot = fxmlLoader.load();
                    Scene programScene = new Scene(programRoot);
                    programStage.setTitle("Toy Language - Program running");
                    programStage.setScene(programScene);
                    programStage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
