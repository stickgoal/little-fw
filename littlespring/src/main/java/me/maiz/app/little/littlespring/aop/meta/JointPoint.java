package me.maiz.app.little.littlespring.aop.meta;

public interface JointPoint {

        String toString();

        String toShortString();

        String toLongString();

        Object getThis();

        Object getTarget();

        Object[] getArgs();

        String getSignature();

        String getKind();


}
