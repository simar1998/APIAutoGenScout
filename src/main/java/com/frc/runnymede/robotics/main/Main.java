package com.frc.runnymede.robotics.main;

import com.frc.runnymede.robotics.main.subroutine.Subroutine;
import com.frc.runnymede.robotics.main.subroutine.SubroutineManager;
import com.frc.runnymede.robotics.main.subroutine.SubroutineState;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;

public class Main {
    public static void main(String... args){
        try {
            SubroutineManager.registerAllSubroutines();
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
