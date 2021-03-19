package com.frc.runnymede.robotics.main.subroutine.annotations;

import com.frc.runnymede.robotics.main.subroutine.SubroutineState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Subroutine {
    public boolean initOnBoot() default false;
    public String desc();
    public int initOrder() default -1;
    public SubroutineState moveOnState() default SubroutineState.finished;
}
