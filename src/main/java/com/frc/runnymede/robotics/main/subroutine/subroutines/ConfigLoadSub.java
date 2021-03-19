package com.frc.runnymede.robotics.main.subroutine.subroutines;

import com.frc.runnymede.robotics.main.Config;
import com.frc.runnymede.robotics.main.Main;
import com.frc.runnymede.robotics.main.subroutine.Subroutine;
import com.frc.runnymede.robotics.main.subroutine.SubroutineState;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine(desc = "config_load",initOnBoot = true, initOrder = 1)
public class ConfigLoadSub extends Subroutine {

    private SubroutineState localState;

    @Override
    public void init() {
        File file = new File(Main.configLoc);
        try {
            Scanner scanner = new Scanner(file);
            StringBuilder configJson = new StringBuilder();
            while (scanner.hasNext()){
                configJson.append(scanner.nextLine());
            }
            Main.config = (new Gson()).fromJson(configJson.toString(), Config.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        localState = SubroutineState.finished;
    }

    @Override
    public SubroutineState getSubroutineState() {
        return localState;
    }

    @Override
    public void stopSubroutine() {

    }



}
