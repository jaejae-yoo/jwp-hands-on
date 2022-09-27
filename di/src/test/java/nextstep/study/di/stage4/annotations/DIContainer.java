package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = initBeans(classes);
        this.beans.forEach(this::initFields);
    }

    private Set<Object> initBeans(final Set<Class<?>> classes) {
        Set<Object> beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            try {
                final Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                beans.add(constructor.newInstance());
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return beans;
    }

    private void initFields(final Object bean) {
        final Field[] fields = bean.getClass()
                .getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            final Class<?> fieldType = field.getType();
            initField(bean, field, fieldType);
        }
    }

    private void initField(final Object targetBean, final Field field, final Class<?> fieldType) {
        for (Object bean : beans) {
            if (fieldType.isAssignableFrom(bean.getClass())) {
                try {
                    field.set(targetBean, bean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName)
                .stream()
                .filter(DIContainer::filterAnnotation)
                .collect(Collectors.toSet());

        return new DIContainer(classes);
    }

    private static boolean filterAnnotation(final Class<?> clazz) {
        return clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(bean -> aClass.isAssignableFrom(bean.getClass()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("빈이 존재하지 않습니다."));
    }
}
