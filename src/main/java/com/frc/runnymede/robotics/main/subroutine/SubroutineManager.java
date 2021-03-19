package com.frc.runnymede.robotics.main.subroutine;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Class manages subroutines and the registrations and invocation of subroutines.
 */
public class SubroutineManager {

    //Subroutine master list contains all the registered subroutines
    private static ArrayList<Subroutine> subroutines = new ArrayList<>();

    /**
     * Register provided subroutine to master list with specific provided index
     * @param index
     * @param subroutine
     */
    private static void registerSubroutine(int index, Subroutine subroutine){
        subroutines.add(index,subroutine);
    }

    /**
     * Register provided subroutine to end of master list
     * @param subroutine
     */
    private static void registerSubroutine(Subroutine subroutine){
        subroutines.add(subroutine);
    }

    /**
     * Finds and registers all available subroutine classes
     */
    public static void registerAllSubroutines()  {
        try {
            Reflections reflections = new Reflections("com.frc.runnymede.robotics.main"); //Instantiates reflection object on given path
            Set<Class<? extends Subroutine>> subTypes = reflections.getSubTypesOf(Subroutine.class);//Finds all classes that extend subroutine class
            Iterator<Class<? extends Subroutine>> iterator = subTypes.iterator();//Creates iterator from the set of classes that use subroutine type
            while (iterator.hasNext()) { //Iterates through set
                Class<?> clazz = Class.forName(iterator.next().getName());//get the class name from the current iterator object and creates a Object of type Class
                Object object = clazz.getDeclaredConstructor().newInstance();//Creates a new instance of generic class object
                System.out.println("Registering class " + object.getClass().getName());
                if (checkAnnotations(object)) {//Casts Subroutine type to object and adds subroutine to master subroutine list if contains proper annotations
                    registerSubroutine((Subroutine) object);
                } else {
                    System.out.println("Does not contain proper annotations, registration of subroutine canceled!");
                }
            }

            for (Subroutine subroutine : subroutines){
                Annotation[] annotations = subroutine.getClass().getAnnotations();
                for (Annotation anno : annotations){
                    if (anno instanceof com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine){
                        com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine annotation = (com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine) anno;
                        subroutine.setDesc(annotation.desc());
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Checks whether or not the provided object meets the criteria to be added to the subroutine lists
     * @param obj
     * @return
     */
    private static boolean checkAnnotations(Object obj){
        for (Annotation annotation : obj.getClass().getAnnotations()){ //For each statement that iterates through all annotations within class
            if (annotation instanceof com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine){ // checks if subroutine annotation is present
                return true; // returns true if presence is noted.
            }
        }
        return false;
    }


    /**
     * Checks the boot sequence subroutines for proper usage and inits them in the given order
     */
    private static void initBootSubroutines() throws SubroutineNotFoundException {

        HashMap<Integer,Subroutine> bootSequenceSubroutine = new HashMap<>();//Sequenced boot order subroutine desc
        ArrayList<Subroutine> bootUnsequencedSubroutine = new ArrayList<>();//Unsequenced boot order subroutine desc
        List<Integer> initSeq = new ArrayList<>();

        //Code block below adds all boot subroutines into the master list
        for (Subroutine subroutine : subroutines){
            Annotation[] annotations = subroutine.getClass().getAnnotations();
            for (Annotation anno : annotations){
                if (anno instanceof com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine){
                    com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine annotation = (com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine) anno;
                    if (annotation.initOnBoot()){
                        if (annotation.initOrder() == -1){//If default init order integer is detected subroutine is added to the unsequenced list
                            bootUnsequencedSubroutine.add(annotation.initOrder(),subroutine);
                        }
                        else {
                            initSeq.add(annotation.initOrder());//Otherwise subroutine is added to the sequenced list and will be called when sequenced
                            bootSequenceSubroutine.put(annotation.initOrder(), subroutine);
                        }
                    }
                }
            }
        }


        //Checks if the sequence annotations are valid
        for (int i : initSeq){
            int count = 0;
            for (Integer i2 : initSeq){
                if (count > 1){
                    throw new RuntimeException("One or more subroutine classes contain the same initialization sequence number");
                }
                if (i2 == i){
                    count ++;
                }
            }
        }

        //Sorts the sequence
        Collections.sort(initSeq);

        //Checks to make sure that the sequence is incremental and does not contain any gaps
        int temp = 0;
        for (int i = 0 ; i < initSeq.size(); i++){
            if (i > 0){
                if (!(temp == initSeq.get(i) - 1)){
                    throw new RuntimeException("Please review your init order within the subroutines package as the sort order does not seem to be incremental");
                }
            }
            temp = initSeq.get(i);
        }

        //Initializes sequence in order
        for (int initOrder: initSeq){
            bootSequenceSubroutine.get(initOrder).init();
        }

        //Initializes unsequenced subroutines
        for (Subroutine subroutine : bootUnsequencedSubroutine){
            subroutine.init();
        }

    }

    /**
     * Finds the subroutine object and returns it
     * @param desc
     * @return
     * @throws SubroutineNotFoundException
     */
    public static Subroutine getSubroutine(String desc) throws SubroutineNotFoundException {

        for (Subroutine subroutine : subroutines){
            if (subroutine.getDesc().equals(desc)){
                return subroutine;
            }
        }

        throw new SubroutineNotFoundException("Subroutine with desc " + desc + " not found within list");

    }





}
