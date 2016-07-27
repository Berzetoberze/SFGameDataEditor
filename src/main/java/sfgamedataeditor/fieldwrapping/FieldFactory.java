package sfgamedataeditor.fieldwrapping;

import org.apache.log4j.Logger;
import sfgamedataeditor.fieldwrapping.fields.AbstractDataField;
import sfgamedataeditor.fieldwrapping.fields.ComboBox;
import sfgamedataeditor.fieldwrapping.fields.TextField;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum  FieldFactory {
    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(FieldFactory.class);

    private Map<Class<? extends JComponent>, Class<? extends AbstractDataField>> fieldTypes =
            new HashMap<Class<? extends JComponent>, Class<? extends AbstractDataField>>() {{
                put(JComboBox.class, ComboBox.class);
                put(JTextField.class, TextField.class);
            }};

    public AbstractDataField createField(Class<? extends JComponent> componentClass, Object... parameters) {
        if (!fieldTypes.containsKey(componentClass)) {
            throw new RuntimeException("No class with name: " + componentClass.getName() + " exists in field factory");
        }

        Class<? extends AbstractDataField> dataFieldClass = fieldTypes.get(componentClass);
        AbstractDataField abstractDataField = null;
        try {
            // TODO make more strict constructor search
            abstractDataField = (AbstractDataField) dataFieldClass.getDeclaredConstructors()[0].newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return abstractDataField;
    }
}