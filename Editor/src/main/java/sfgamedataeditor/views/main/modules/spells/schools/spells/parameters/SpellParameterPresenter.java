package sfgamedataeditor.views.main.modules.spells.schools.spells.parameters;

import org.apache.log4j.Logger;
import sfgamedataeditor.common.GUIElement;
import sfgamedataeditor.common.IconElement;
import sfgamedataeditor.common.viewconfigurations.spell.parameters.GUIElements;
import sfgamedataeditor.common.widgets.AbstractWidget;
import sfgamedataeditor.common.widgets.common.combobox.level.LevelComboBoxParameter;
import sfgamedataeditor.database.spells.names.SpellNameObject;
import sfgamedataeditor.database.spells.names.SpellNameTableService;
import sfgamedataeditor.database.spells.parameters.SpellParametersObject;
import sfgamedataeditor.database.spells.parameters.SpellParametersTableService;
import sfgamedataeditor.events.processing.ViewRegister;
import sfgamedataeditor.mvc.objects.AbstractPresenter;
import sfgamedataeditor.views.main.MainView;
import sfgamedataeditor.views.utility.i18n.I18NService;
import sfgamedataeditor.views.utility.i18n.I18NTypes;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.*;

public class SpellParameterPresenter extends AbstractPresenter<SpellParameterModelParameter, SpellParameterView> {

    private static final Logger LOGGER = Logger.getLogger(SpellParameterPresenter.class);
    private static final String SPELL_I18N_NAME_POSTFIX = ".name";
    private final Map<Integer, List<String>> i18nStrings = new HashMap<>();
    private final Map<Integer, String> i18nDTOFieldsToGUIElementsIdsMap = new HashMap<Integer, String>() {{
        put(GUIElements.PARAMETER_1, "field1");
        put(GUIElements.PARAMETER_2, "field2");
        put(GUIElements.PARAMETER_3, "field3");
        put(GUIElements.PARAMETER_4, "field4");
        put(GUIElements.PARAMETER_5, "field5");
        put(GUIElements.PARAMETER_6, "field6");
        put(GUIElements.PARAMETER_7, "field7");
        put(GUIElements.PARAMETER_8, "field8");
        put(GUIElements.PARAMETER_9, "field9");
    }};

    public SpellParameterPresenter(SpellParameterView view) {
        super(view);
        createI18NMap();
    }

    private void createI18NMap() {
        i18nStrings.put(GUIElements.PARAMETER_1, new ArrayList<String>());
        i18nStrings.put(GUIElements.PARAMETER_2, new ArrayList<String>());
        i18nStrings.put(GUIElements.PARAMETER_3, new ArrayList<String>());
        i18nStrings.put(GUIElements.PARAMETER_4, new ArrayList<String>());
        i18nStrings.put(GUIElements.PARAMETER_5, new ArrayList<String>());
        i18nStrings.put(GUIElements.PARAMETER_6, new ArrayList<String>());
        i18nStrings.put(GUIElements.PARAMETER_7, new ArrayList<String>());
        i18nStrings.put(GUIElements.PARAMETER_8, new ArrayList<String>());
        i18nStrings.put(GUIElements.PARAMETER_9, new ArrayList<String>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView() {
        SpellParameterModelParameter parameter = getModel().getParameter();
        int selectedSpellId = parameter.getSpellId();
        int selectedLevel = parameter.getLevel();
        Icon icon = parameter.getIcon();
        Set<Integer> spellLevels = SpellParametersTableService.INSTANCE.getSpellLevels(selectedSpellId);

        int spellMinLevel = (int) ((TreeSet) spellLevels).first();
        int spellMaxLevel = (int) ((TreeSet) spellLevels).last();
        selectedLevel = adjustSelectedLevel(selectedLevel, spellMinLevel, spellMaxLevel);
        SpellParametersObject spellParameter = SpellParametersTableService.INSTANCE.getSpellParameterBySpellIdAndLevel(selectedSpellId, selectedLevel);
        updateI18NWidgetsData(spellParameter);

        Field[] declaredFields = getView().getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            try {
                IconElement iconElement = declaredField.getAnnotation(IconElement.class);
                if (iconElement != null) {
                    declaredField.setAccessible(true);
                    JLabel panel = (JLabel) declaredField.get(getView());
                    panel.setIcon(icon);
                    continue;
                }

                GUIElement annotation = declaredField.getAnnotation(GUIElement.class);
                if (annotation == null) {
                    continue;
                }

                declaredField.setAccessible(true);
                JPanel panel = (JPanel) declaredField.get(getView());
                AbstractWidget widget = (AbstractWidget) panel.getComponent(0);

                int guiElementId = annotation.GUIElementId();
                if (guiElementId == GUIElements.SPELL_LEVEL) {
                    LevelComboBoxParameter levelComboBoxParameter = new LevelComboBoxParameter(selectedLevel, spellLevels);
                    widget.getListener().updateWidgetValue(levelComboBoxParameter);
                } else {
                    if (i18nDTOFieldsToGUIElementsIdsMap.containsKey(guiElementId)) {
                        List<String> strings = i18nStrings.get(guiElementId);
                        if (!strings.isEmpty()) {
                            widget.setVisible(true);
                            widget.updateI18N(strings);
                            widget.getListener().updateWidgetValue(spellParameter);
                        } else {
                            widget.setVisible(false);
                        }
                    } else {
                        widget.getListener().updateWidgetValue(spellParameter);
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private void updateI18NWidgetsData(SpellParametersObject spellParametersObject) {
        Integer spellNameId = spellParametersObject.spellNameId;
        String spellName = I18NService.INSTANCE.getMessage(I18NTypes.SPELLS_NAME_MAPPING, String.valueOf(spellNameId));
        SpellNameObject spellNameObject = SpellNameTableService.INSTANCE.getSpellName(I18NService.INSTANCE.getMessage(I18NTypes.SPELLS_GUI, spellName + SPELL_I18N_NAME_POSTFIX));
        for (Map.Entry<Integer, String> stringIntegerEntry : i18nDTOFieldsToGUIElementsIdsMap.entrySet()) {
            i18nStrings.get(stringIntegerEntry.getKey()).clear();
            try {
                Field field = spellNameObject.getClass().getDeclaredField(stringIntegerEntry.getValue());
                if (field == null) {
                    continue;
                }

                field.setAccessible(true);
                String parameterI18nValue = (String) field.get(spellNameObject);
                if (parameterI18nValue != null) {
                    i18nStrings.get(stringIntegerEntry.getKey()).add(parameterI18nValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.info(e.getMessage(), e);
            }
        }
    }

    @Override
    public void renderView() {
        MainView mainView = ViewRegister.INSTANCE.getView(MainView.class);
        mainView.renderViewInsideContentPanel(getView().getMainPanel());
    }

    @Override
    public void unRenderView() {
        MainView mainView = ViewRegister.INSTANCE.getView(MainView.class);
        mainView.unRenderViewInsideContentPanel(getView().getMainPanel());
    }

    // in case user selected spell with level-range [1; 12] with level 5
    // then selected spell with level range [13; 20]
    // we have to select level 13 to appropriately load correct data
    // and vice versa, if user selected spell with level range [13; 20]
    // and then selected spell with level range [1; 12]
    private int adjustSelectedLevel(int selectedLevel, int spellMinLevel, int spellMaxLevel) {
        if (selectedLevel <= spellMaxLevel && selectedLevel >= spellMinLevel) {
            return selectedLevel;
        } else {
            return spellMinLevel;
        }
    }
}
