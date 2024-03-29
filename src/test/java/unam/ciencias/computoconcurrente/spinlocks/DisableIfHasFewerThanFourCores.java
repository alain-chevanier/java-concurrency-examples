package unam.ciencias.computoconcurrente.spinlocks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.condition.DisabledIf;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@DisabledIf("hasFewerThanFourCores")
public @interface DisableIfHasFewerThanFourCores {}
