package com.frc.runnymede.robotics.main.subroutine.subroutines;

import com.frc.runnymede.robotics.main.subroutine.Subroutine;
import com.frc.runnymede.robotics.main.subroutine.SubroutineState;

@com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine(desc = "Startup_subroutine_test",initOnBoot = true, initOrder = 1)
public class StartupSub extends Subroutine {

    private SubroutineState localState;

    @Override
    public void init() {

    }

    @Override
    public SubroutineState getSubroutineState() {
        return localState;
    }

    @Override
    public void stopSubroutine() {

    }



}
