package com.frc.runnymede.robotics.main.subroutine.subroutines;

import com.frc.runnymede.robotics.main.subroutine.Subroutine;
import com.frc.runnymede.robotics.main.subroutine.SubroutineState;

@com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine(desc = "database_subroutine",initOnBoot = true, initOrder = 2,moveOnState = SubroutineState.active)
public class DatabaseSub extends Subroutine {
    @Override
    public void init() {

    }

    @Override
    public SubroutineState getSubroutineState() {
        return null;
    }

    @Override
    public void stopSubroutine() {

    }
}
