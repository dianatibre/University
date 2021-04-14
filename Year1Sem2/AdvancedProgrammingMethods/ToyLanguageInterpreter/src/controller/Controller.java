/*
   public void addProgram(PrgState newPrg) {
        this.repository.addPrg(newPrg);
    }

    public void callGarbageCollector(List<PrgState>prgStates){
        if(prgStates.isEmpty())
            throw new MyException("Trying to do garbage collection on an empty repository.");

        var originalHeap = prgStates.get(0).getHeap().getContent();

        List<Integer> systemAddresses = new ArrayList<>();

        systemAddresses.addAll(getAddressesFromSymTable(Collections.singletonList(originalHeap.values())));
        for(PrgState prgState : prgStates)
            systemAddresses.addAll(getAddressesFromSymTable(Collections.singletonList(prgState.getSymTable().getDictionary().values())));

        var newHeap = conservativeGarbageCollector(systemAddresses, originalHeap);

        for(PrgState prgState : prgStates)
            prgState.getHeap().setContent(newHeap);
        /*
        PrgState progState = prgStates.get(0);
        var dummy = prgStates.stream()
                .map(program -> program.getSymTable().getDictionary().values())
                .collect(Collectors.toList());
        progState.getHeap().setContent(safeGarbageCollector(
                getAddressesFromSymTable(prgStates.stream().map(program -> program.getSymTable().getDictionary().values()).collect(Collectors.toList())),
                getAddressesFromHeap(progState.getHeap().getContent().values()),
                progState.getHeap().getContent()
        ));



        //prgStates.forEach(prgState -> {
        //    prgState.getHeap().setContent(safeGarbageCollector(getAddressesFromSymTable(prgState.getSymTable().getDictionary().values(),prgState.getHeap().getDictionary().values()),prgState.getHeap().getContent()));
        //});
    }

    Map<Integer, IValue> conservativeGarbageCollector(List<Integer> systemAddresses, Map<Integer,IValue> heap){
        return heap.entrySet().stream()
                .filter(e->systemAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Integer> getAddressesFromHeap(Collection<IValue> values) {
        return values.stream()
                .filter(val -> val instanceof  RefValue)
                .map(value -> {RefValue v1 = (RefValue) value; return v1.getAddr();})
                .collect(Collectors.toList());

    }

    public void allStep() throws InterruptedException {
        this.executor = Executors.newFixedThreadPool(2);
        //remove the completed programs
        List<PrgState> prgList=removeCompletedPrg(repository.getPrgList());
        while(prgList.size() > 0){
            //prgList.get(0).getHeap().setContent(safeGarbageCollector(getAddressesFromSymTable(prgList.get(0).getSymTable().lookup(prgList.get(0).getHeap().getContent()))));
            callGarbageCollector(prgList);
            oneStepForAllPrg(prgList);
            //remove the completed programs
            prgList=removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdownNow();
        //HERE the repository still contains at least one Completed Prg
        // and its List<PrgState> is not empty. Note that oneStepForAllPrg calls the method //setPrgList of repository in order to change the repository
        // update the repository state
        //prgList=removeCompletedPrg(repository.getPrgList());
        repository.setPrgList(prgList);
    }

    void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        //before the execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // RUN concurrently one step for each of the existing PrgStates
        // -----------------------------------------------------------------------
        // prepare the list of callables
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                .collect(Collectors.toList());
        //start the execution of the callables
        // it returns the list of new created PrgStates (namely threads)
        List<PrgState> newPrgList = this.executor.invokeAll(callList).stream()
                .map(future -> { try { return future.get();}
                         catch (Exception e) {
                            //throw new MyException(e.getMessage());
                             System.out.println(e.getMessage());
                             return null; //change here pls
                        }
                        }).filter(p-> p != null )
                .collect(Collectors.toList());
        //add the new created threads to the list of existing threads
        prgList.addAll(newPrgList);
        //after the execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Save the current programs in the repository
        repository.setPrgList(prgList);

    }

    //GARBAGE COLLECTOR
    public Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddresses, HashMap<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Integer, IValue> safeGarbageCollector(List<Integer> addresses, List<Integer> addressesFromHeap, HashMap<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> addresses.contains(e.getKey()) ||
                        heap.values().stream()
                                .filter(v -> v instanceof RefValue)
                                .anyMatch(v -> ((RefValue) v).getAddr() == e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Integer> getAddressesFromSymTable(List<Collection<IValue>> symTablesValues) {
        return symTablesValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }
}
*/

package controller;

import exception.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.statement.IStmt;
import model.value.IValue;
import model.value.RefValue;
import repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private IRepository repository;
    private ExecutorService executor;

    public Controller(IRepository repo) {
        this.repository = repo;
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    //GARBAGE COLLECTOR
    public List<Integer> getAddressesFromSymTable(Collection<IValue> symTablesValues) {
        return symTablesValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

    public List<Integer> getAddressFromHeap(Collection<IValue> heapVal) {
        return heapVal.stream()
                .filter(val -> val instanceof RefValue)
                .map(value -> {
                    RefValue v1 = (RefValue) value;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

    public Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddresses, HashMap<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Integer, IValue> safeGarbageCollector(List<Integer> addresses, List<Integer> heapAddress, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> addresses.contains(e.getKey()) || heapAddress.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void collectGarbage(List<PrgState> prgList) {
        PrgState prg = prgList.get(0);
        var dummy = prgList.stream()
                .map(programState -> programState.getSymTable().getDictionary().values())
                .collect(Collectors.toList());
        prg.getHeap().setContent(safeGarbageCollector(
                getAddressesFromSymTable(prg.getSymTable().getDictionary().values()),
                getAddressFromHeap(prg.getHeap().getContent().values()),
                prg.getHeap().getContent()));
    }

    public void addService(PrgState prg) {
        repository.addPrg(prg);
    }

    //one step execution of a program
    public PrgState oneStep(PrgState state) throws MyException, IOException {
        MyIStack<IStmt> stk = state.getExeStack();
        if (stk.isEmpty())
            throw new MyException("Program stack is empty!");
        IStmt crtStatement = stk.pop();
        return crtStatement.execute(state);
    }

    @SuppressWarnings("unChecked")
    public void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        prgList.forEach(prg -> {
            repository.logPrgStateExec(prg);
        });
        List<Callable<PrgState>> callableList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) p::oneStep)
                .collect(Collectors.toList());

        List<PrgState> newPrgList = executor.invokeAll(callableList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        prgList.addAll(newPrgList);

        prgList.forEach(prg -> {

            repository.logPrgStateExec(prg);
        });
        repository.setPrgList(prgList);


    }

    //unchecked
    // complete execution of a program
    public void allStep() throws IOException, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        //remove the completed programs
        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());
        while (prgList.size() > 0) {
            collectGarbage(prgList);
            oneStepForAllPrg(prgList);
            //remove the completed programs
            prgList = removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdown();
        //HERE the repository still contains at least one Completed Prg
        // and its List<PrgState> is not empty. Note that oneStepForAllPrg calls the method
        //setPrgList of repository in order to change the repository

        // update the repository state
        repository.setPrgList(prgList);
    }

    /*

    public Controller(IRepository repo) {
        this.repository = repo;
    }

    public IRepository getRepo(){
        return repository;
    }


    List<PrgState> removeCompletedPrg(List<PrgState> inPrgList){
        return inPrgList.stream()
                //.filter(p -> p.isNotCompleted())
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public void addProgram(PrgState newPrg) {
        this.repository.addPrg(newPrg);
    }

    /*
    public void allStep(boolean displayFlag) throws RuntimeException {

        PrgState prg = repo.getCrtPrg();
        if(prg.getExecutionStack().isEmpty()){
            System.out.println("EXECUTION STACK IS EMPTY. YOU MAY BE TRYING TO RERUN THE SAME EXAMPLE AGAIN. PLEASE RESTART THE INTERPRETOR AND TRY AGAIN.");
            if(!prg.getOutput().isEmpty())
                System.out.println("We have found some output. We're printing it here. (for debug purposes only)");
            return;
        }
        if(displayFlag) System.out.println(prg);

        while (!prg.getExecutionStack().isEmpty()){
            oneStep(prg);
            //repo.logPrgStateExec();
            if(displayFlag) System.out.println(prg);

            List<Integer> symTableAddr = getAddr(prg.getSymTable().getContent().values());
            List<Integer> heapTableAddr = getAddr(prg.getHeap().getContent().values());

            List<Integer> allAddr = Stream.concat(symTableAddr.stream(), heapTableAddr.stream())
                    .collect(Collectors.toList());

           prg.getHeap().setContent(saferGarbageCollector(allAddr, prg.getHeap().getContent()));
        }
    }
     */

    /*

    void oneStepForAllPrg (List<PrgState> prgList) throws InterruptedException {
        prgList.forEach(prg ->
                repository.logPrgStateExec(prg));

        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                .collect(Collectors.toList());

        //list of new threads returned by our program
        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> { try { return future.get();}
                catch(Exception e) {
                    System.out.println(e.getMessage());
                    return null; //change here pls
                }}).filter(p -> p!=null)
                .collect(Collectors.toList());

        //we add all new threads to the prgList
        prgList.addAll(newPrgList);

        prgList.forEach(prg->
                repository.logPrgStateExec(prg));
        repository.setPrgList(prgList);
    }


    public void allStep() throws RuntimeException {
        executor = Executors.newFixedThreadPool(2);

        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());

        while(prgList.size() > 0) {

            try {
                callGarbageCollector(prgList);
                oneStepForAllPrg(prgList);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            prgList = removeCompletedPrg(repository.getPrgList());
        }

        executor.shutdownNow();

        repository.setPrgList(prgList);

    }


    List<Integer> getAddr(Collection<IValue> values){
        return values.stream()
                .filter(v-> v instanceof RefValue)
                .map(v-> {RefValue v1 = (RefValue)v; return v1.getAddr();})
                .collect(Collectors.toList());
    }

    List<Integer> getAddrSymTables(List<PrgState> prgStateList){
        List<Integer> ans = new ArrayList<>();
        for(PrgState prgState : prgStateList){
            ans.addAll(getAddr(prgState.getSymTable().getDictionary().values()));
        }
        return ans;
    }


    //GARBAGE COLLECTOR
    public Map<Integer, IValue> safeGarbageCollector(List<Integer> addresses, List<Integer> addressesFromHeap, HashMap<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> addresses.contains(e.getKey()) || addressesFromHeap.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> systemAddresses, Map<Integer,IValue> heap){
        return heap.entrySet().stream()
                .filter(e->systemAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void callGarbageCollector(List<PrgState> prgStateList){
        if(prgStateList.isEmpty())
            throw new RuntimeException("Trying to do garbage collection on an empty repository.");

        var originalHeap = prgStateList.get(0).getHeap().getContent();

        List<Integer> systemAddresses = new ArrayList<>();

        systemAddresses.addAll(getAddr(originalHeap.values()));
        for(PrgState prgState : prgStateList)
            systemAddresses.addAll(getAddr(prgState.getSymTable().getDictionary().values()));

        var newHeap = unsafeGarbageCollector(systemAddresses, originalHeap);

        for(PrgState prgState : prgStateList)
            prgState.getHeap().setContent(newHeap);
    }

     */

}

