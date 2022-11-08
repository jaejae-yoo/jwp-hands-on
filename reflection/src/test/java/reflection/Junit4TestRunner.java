package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Class<?> aClass = Class.forName(clazz.getName());

        for (Method method : aClass.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof MyTest) {
                    method.invoke(aClass.getDeclaredConstructor().newInstance());
                }
            }
        }
    }
}
