package info.getbus.mybatis.reflection.factory;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import java.util.*;

public class MapBasedObjectFactory extends DefaultObjectFactory {
    private boolean fullOverride;

    private Map<Class<?>, Class<?>> classByInterface;

    public MapBasedObjectFactory() {
        classByInterface = new HashMap<>();
        classByInterface.put(Queue.class, LinkedList.class);
        classByInterface.put(Deque.class, LinkedList.class);
    }

    public MapBasedObjectFactory(Map<Class<?>, Class<?>> classByInterface) {
        this.classByInterface = classByInterface;
    }

    @Override
    protected Class<?> resolveInterface(Class<?> type) {
        if (fullOverride) {
            return resolveInterfaceFromMap(type);
        } else {
            Class<?> classToCreate = super.resolveInterface(type);
            return type == classToCreate ? resolveInterfaceFromMap(type) : classToCreate;
        }
    }

    private Class<?> resolveInterfaceFromMap(Class<?> type) {
        Class<?> classToCreate = classByInterface.get(type);
        return null == classToCreate ? type : classToCreate;
    }


    public boolean isFullOverride() {
        return fullOverride;
    }

    public void setFullOverride(boolean fullOverride) {
        this.fullOverride = fullOverride;
    }
}
