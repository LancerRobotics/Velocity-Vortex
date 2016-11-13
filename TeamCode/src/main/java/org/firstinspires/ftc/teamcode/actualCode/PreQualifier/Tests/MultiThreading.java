package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 11/9/2016.
 */

public class MultiThreading extends LancerLinearOpMode {

    @Override
    public void runOpMode() {
        setup();

    }
}
class RunnableDemo implements Runnable{
    private Thread t;
    private String threadName;
    RunnableDemo(String name){
        threadName = name;
        System.out.println("Creating " + threadName);

    }

    @Override
    public void run() {

    }
}