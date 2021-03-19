package com.frc.runnymede.robotics.main.subroutine.subroutines;

import com.frc.runnymede.robotics.main.db.HibernateThread;
import com.frc.runnymede.robotics.main.subroutine.Subroutine;
import com.frc.runnymede.robotics.main.subroutine.SubroutineState;

@com.frc.runnymede.robotics.main.subroutine.annotations.Subroutine(desc = "database_subroutine",initOnBoot = true, initOrder = 2,moveOnState = SubroutineState.active)
public class DatabaseSub extends Subroutine {

    private SubroutineState localState;

    @Override
    public void init() {
        HibernateThread.initHibernateThread();
        localState = SubroutineState.active;
    }

    @Override
    public SubroutineState getSubroutineState() {
        return localState;
    }

    @Override
    public void stopSubroutine() {
        HibernateThread.shutDown();
    }
}
