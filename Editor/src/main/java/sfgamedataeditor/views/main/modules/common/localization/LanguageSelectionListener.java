package sfgamedataeditor.views.main.modules.common.localization;

import sfgamedataeditor.views.common.ObjectTuple;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LanguageSelectionListener implements ItemListener {

    private final JComboBox<ObjectTuple> comboBox;

    public LanguageSelectionListener(JComboBox<ObjectTuple> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }

        ObjectTuple selectedItem = (ObjectTuple) comboBox.getSelectedItem();
        LocalizationService.INSTANCE.setLanguageId(selectedItem.getObjectId());
    }
}