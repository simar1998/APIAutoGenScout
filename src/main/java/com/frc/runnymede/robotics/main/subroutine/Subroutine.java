package com.frc.runnymede.robotics.main.subroutine;

public abstract class Subroutine {

    String desc = "";

    public abstract void init();
    public abstract SubroutineState getSubroutineState();
    public abstract void stopSubroutine();

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
