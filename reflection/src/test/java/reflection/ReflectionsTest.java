package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.lang.reflect.Constructor;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        for (Class<?> controller : controllers) {
            log.info(controller.toString());
        }
        for (Class<?> service : services) {
            log.info(service.toString());

        }
        for (Class<?> repository : repositories) {
            log.info(repository.toString());
        }
    }
}
