package sfgamedataeditor.views.main.modules.spells.schools.spells.parameters;

import sfgamedataeditor.common.GUIElement;
import sfgamedataeditor.common.IconElement;
import sfgamedataeditor.common.viewconfigurations.spell.parameters.GUIElements;
import sfgamedataeditor.common.widgets.Disabled;
import sfgamedataeditor.database.spells.parameters.SpellParametersObject;
import sfgamedataeditor.mvc.objects.AbstractPresenter;
import sfgamedataeditor.mvc.objects.PresentableView;
import sfgamedataeditor.views.utility.i18n.I18NService;
import sfgamedataeditor.views.utility.i18n.I18NTypes;

import javax.swing.*;

@SuppressWarnings("unused")
public class SpellParameterView implements PresentableView {

    private static final int SPELL_COMMON_PARAMETERS_TAB_INDEX = 0;
    private static final int SPELL_PARAMETERS_TAB_INDEX = 1;

    // inside spell offsets are taken from http://spellforcefanforum.hostoi.com/viewtopic.php?f=14&t=242
    private JPanel mainPanel;

    @Disabled
    @GUIElement(GUIElementId = GUIElements.NUMBER, DTOColumnNames = "spellNumber", DTOClass = SpellParametersObject.class)
    private JPanel numberPanel;

    @Disabled
    @GUIElement(GUIElementId = GUIElements.NAME_ID, DTOColumnNames = "spellNameId", DTOClass = SpellParametersObject.class)
    private JPanel nameIdPanel;

    @GUIElement(GUIElementId = GUIElements.SPELL_LEVEL)
    private JPanel levelPanel;

    @GUIElement(GUIElementId = GUIElements.REQUIREMENT_CLASS_SUBCLASS_1, DTOColumnNames = {"requirementClass1", "requirementSubClass1"}, DTOClass = SpellParametersObject.class)
    private JPanel requirementClassSubClassPanel1;

    @GUIElement(GUIElementId = GUIElements.REQUIREMENT_LEVEL_1, DTOColumnNames = "requirementLevel1", DTOClass = SpellParametersObject.class)
    private JPanel requirementLevelPanel1;

    @GUIElement(GUIElementId = GUIElements.REQUIREMENT_CLASS_SUBCLASS_2, DTOColumnNames = {"requirementClass2", "requirementSubClass2"}, DTOClass = SpellParametersObject.class)
    private JPanel requirementClassSubClassPanel2;

    @GUIElement(GUIElementId = GUIElements.REQUIREMENT_LEVEL_2, DTOColumnNames = "requirementLevel2", DTOClass = SpellParametersObject.class)
    private JPanel requirementLevelPanel2;

    @GUIElement(GUIElementId = GUIElements.REQUIREMENT_CLASS_SUBCLASS_3, DTOColumnNames = {"requirementClass3", "requirementSubClass3"}, DTOClass = SpellParametersObject.class)
    private JPanel requirementClassSubClassPanel3;

    @GUIElement(GUIElementId = GUIElements.REQUIREMENT_LEVEL_3, DTOColumnNames = "requirementLevel3", DTOClass = SpellParametersObject.class)
    private JPanel requirementLevelPanel3;

    @GUIElement(GUIElementId = GUIElements.MANA_USAGE, DTOColumnNames = "manaUsage", DTOClass = SpellParametersObject.class)
    private JPanel manaActivationPanel;

    @GUIElement(GUIElementId = GUIElements.CAST_TYPE, DTOColumnNames = "castType", DTOClass = SpellParametersObject.class)
    private JPanel castTypePanel;

    @GUIElement(GUIElementId = GUIElements.CAST_TIME, DTOColumnNames = "castTime", DTOClass = SpellParametersObject.class)
    private JPanel castTimePanel;

    @GUIElement(GUIElementId = GUIElements.COOLDOWN, DTOColumnNames = "cooldown", DTOClass = SpellParametersObject.class)
    private JPanel cooldownPanel;

    @GUIElement(GUIElementId = GUIElements.MIN_RANGE, DTOColumnNames = "minRange", DTOClass = SpellParametersObject.class)
    private JPanel minRangePanel;

    @GUIElement(GUIElementId = GUIElements.MAX_RANGE, DTOColumnNames = "maxRange", DTOClass = SpellParametersObject.class)
    private JPanel maxRangePanel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_1, DTOColumnNames = "parameter1", DTOClass = SpellParametersObject.class)
    private JPanel parameter1Panel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_2, DTOColumnNames = "parameter2", DTOClass = SpellParametersObject.class)
    private JPanel parameter2Panel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_3, DTOColumnNames = "parameter3", DTOClass = SpellParametersObject.class)
    private JPanel parameter3Panel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_4, DTOColumnNames = "parameter4", DTOClass = SpellParametersObject.class)
    private JPanel parameter4Panel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_5, DTOColumnNames = "parameter5", DTOClass = SpellParametersObject.class)
    private JPanel parameter5Panel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_6, DTOColumnNames = "parameter6", DTOClass = SpellParametersObject.class)
    private JPanel parameter6Panel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_7, DTOColumnNames = "parameter7", DTOClass = SpellParametersObject.class)
    private JPanel parameter7Panel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_8, DTOColumnNames = "parameter8", DTOClass = SpellParametersObject.class)
    private JPanel parameter8Panel;

    @GUIElement(GUIElementId = GUIElements.PARAMETER_9, DTOColumnNames = "parameter9", DTOClass = SpellParametersObject.class)
    private JPanel parameter9Panel;
    private JPanel parametersPanel;
    private JTabbedPane tabPane;
    private JPanel commonParameters;

    @IconElement
    private JLabel iconLabel;

    public SpellParameterView() {
        tabPane.setTitleAt(SPELL_COMMON_PARAMETERS_TAB_INDEX, I18NService.INSTANCE.getMessage(I18NTypes.SPELLS_GUI, "tab.spell.common.parameters"));
        tabPane.setTitleAt(SPELL_PARAMETERS_TAB_INDEX, I18NService.INSTANCE.getMessage(I18NTypes.SPELLS_GUI, "tab.spell.parameters"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public Class<? extends AbstractPresenter> getPresenterClass() {
        return SpellParameterPresenter.class;
    }
}
