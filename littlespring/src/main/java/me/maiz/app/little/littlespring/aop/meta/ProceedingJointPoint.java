package me.maiz.app.little.littlespring.aop.meta;

public interface ProceedingJointPoint  extends JointPoint{

    Object proceed() throws Throwable;

    Object proceed(Object[] args) throws Throwable;

}
