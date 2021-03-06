package sfgamedataeditor.common.widgets;

import org.apache.log4j.Logger;
import sfgamedataeditor.database.common.CommonTableService;
import sfgamedataeditor.database.common.OffsetableObject;

import java.lang.reflect.Field;

public abstract class AbstractWidgetListener<W extends AbstractWidget, M> {
    private static final Logger LOGGER = Logger.getLogger(AbstractWidgetListener.class);

    private final Field[] mappedFields;
    private final W component;
    private M mappedObject;

    protected AbstractWidgetListener(W component, Field... mappedFields) {
        this.component = component;
        this.mappedFields = mappedFields;
    }

    /**
     * {@inheritDoc}
     */
    public void updateWidgetValue(M mappedObject) {
        this.mappedObject = mappedObject;
        try {
            int[] values = new int[mappedFields.length];
            for (int i = 0; i < mappedFields.length; ++i) {
                mappedFields[i].setAccessible(true);
                Object o = mappedFields[i].get(mappedObject);
                values[i] = (Integer)o;
            }

            setFieldValues(values);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void setWidgetValueToDTOField() {
        int values[] = getFieldValues();
        for (int i = 0; i < mappedFields.length; ++i) {
            mappedFields[i].setAccessible(true);
            try {
                mappedFields[i].set(mappedObject, values[i]);
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        OffsetableObject mappedObject = (OffsetableObject) this.mappedObject;
        CommonTableService.INSTANCE.updateObject(mappedObject, mappedObject.getClass());
    }

    protected Field[] getMappedFields() {
        return mappedFields;
    }

    protected abstract int[] getFieldValues();

    protected abstract void setFieldValues(int[] value);

    protected W getWidget() {
        return component;
    }
}
