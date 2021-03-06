package sfgamedataeditor.common.widgets.spells.casttype;

import sfgamedataeditor.common.widgets.AbstractWidget;

import javax.swing.*;
import java.util.List;

@SuppressWarnings("unused")
public class CastTypeWidget extends AbstractWidget<CastTypeWidgetListener> {
    private JPanel mainPanel;
    private JRadioButton isProjectileToEnemyRadioButton;
    private JRadioButton isProjectileToAlliesRadioButton;
    private JRadioButton isTargetAreaRadioButton;
    private JRadioButton isWorldInstantAreaRadioButton;
    private JRadioButton isInstantAreaRadioButton;
    private JRadioButton isWorldTargetAreaRadioButton;
    private JRadioButton isAlliesAreaRadioButton;
    private JLabel label;
    private JSeparator parametersSeparator;
    private JPanel parametersPanel;

    public CastTypeWidget() {
        add(getMainPanel());
    }

    JRadioButton getIsProjectileToEnemyRadioButton() {
        return isProjectileToEnemyRadioButton;
    }

    JRadioButton getIsProjectileToAlliesRadioButton() {
        return isProjectileToAlliesRadioButton;
    }

    JRadioButton getIsTargetAreaRadioButton() {
        return isTargetAreaRadioButton;
    }

    JRadioButton getIsWorldInstantAreaRadioButton() {
        return isWorldInstantAreaRadioButton;
    }

    JRadioButton getIsInstantAreaRadioButton() {
        return isInstantAreaRadioButton;
    }

    JRadioButton getIsWorldTargetAreaRadioButton() {
        return isWorldTargetAreaRadioButton;
    }

    JRadioButton getIsAlliesAreaRadioButton() {
        return isAlliesAreaRadioButton;
    }

    @Override
    public void insertListener(CastTypeWidgetListener listener) {
        isProjectileToEnemyRadioButton.addItemListener(listener);
        isProjectileToAlliesRadioButton.addItemListener(listener);
        isTargetAreaRadioButton.addItemListener(listener);
        isWorldInstantAreaRadioButton.addItemListener(listener);
        isInstantAreaRadioButton.addItemListener(listener);
        isWorldTargetAreaRadioButton.addItemListener(listener);
        isAlliesAreaRadioButton.addItemListener(listener);
    }

    @Override
    public void updateI18N(List<String> i18nStrings) {
        label.setText(i18nStrings.get(0));
        isProjectileToEnemyRadioButton.setText(i18nStrings.get(1));
        isProjectileToAlliesRadioButton.setText(i18nStrings.get(2));
        isTargetAreaRadioButton.setText(i18nStrings.get(3));
        isWorldInstantAreaRadioButton.setText(i18nStrings.get(4));
        isInstantAreaRadioButton.setText(i18nStrings.get(5));
        isWorldTargetAreaRadioButton.setText(i18nStrings.get(6));
        isAlliesAreaRadioButton.setText(i18nStrings.get(7));
    }

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
