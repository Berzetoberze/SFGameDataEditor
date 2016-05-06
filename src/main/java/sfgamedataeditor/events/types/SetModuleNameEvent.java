package sfgamedataeditor.events.types;

import sfgamedataeditor.events.ClassTuple;
import sfgamedataeditor.events.processing.ViewRegister;
import sfgamedataeditor.views.common.AbstractModulesView;
import sfgamedataeditor.views.common.AbstractView;

public class SetModuleNameEvent extends Event {
    private Class<? extends AbstractView> parentViewClass;
    private Class<? extends AbstractModulesView> childViewClass;
    private Object moduleName;

    public SetModuleNameEvent(Class<? extends AbstractModulesView> childViewClass,
            Class<? extends AbstractView> parentViewClass) {
        this.parentViewClass = parentViewClass;
        this.childViewClass = childViewClass;
    }

    public AbstractModulesView getModulesView() {
        return (AbstractModulesView) ViewRegister.INSTANCE.getView(new ClassTuple<>(childViewClass, parentViewClass));
    }

    public Object getModuleName() {
        return moduleName;
    }

    public void setModuleName(Object moduleName) {
        this.moduleName = moduleName;
    }
}
