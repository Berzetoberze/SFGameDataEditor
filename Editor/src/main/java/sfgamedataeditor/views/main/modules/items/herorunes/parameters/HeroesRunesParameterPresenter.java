package sfgamedataeditor.views.main.modules.items.herorunes.parameters;

import org.apache.log4j.Logger;
import sfgamedataeditor.common.GUIElement;
import sfgamedataeditor.common.IconElement;
import sfgamedataeditor.common.viewconfigurations.item.heroesrunes.GUIElements;
import sfgamedataeditor.common.widgets.AbstractWidget;
import sfgamedataeditor.database.creatures.herospells.HeroSpellObject;
import sfgamedataeditor.database.creatures.parameters.CreatureParameterObject;
import sfgamedataeditor.database.creatures.skills.CreatureSkillObject;
import sfgamedataeditor.database.items.price.parameters.ItemPriceParametersObject;
import sfgamedataeditor.events.processing.ViewRegister;
import sfgamedataeditor.mvc.objects.AbstractPresenter;
import sfgamedataeditor.views.main.MainView;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeroesRunesParameterPresenter extends AbstractPresenter<HeroesRunesParametersModelParameter, HeroesRunesParametersView> {

    private static final Logger LOGGER = Logger.getLogger(HeroesRunesParameterPresenter.class);

    private static final Map<Integer, Integer> SPELL_NUMBER_MAPPING = new HashMap<>();
    private static final Map<Integer, Integer> SKILL_NUMBER_MAPPING = new HashMap<>();

    public HeroesRunesParameterPresenter(HeroesRunesParametersView view) {
        super(view);
        initializeSpellNumberMapping();
        initializeSkillNumberMapping();
    }

    private void initializeSpellNumberMapping() {
        SPELL_NUMBER_MAPPING.put(GUIElements.SPELL_PANEL_1, 0);
        SPELL_NUMBER_MAPPING.put(GUIElements.SPELL_PANEL_2, 1);
        SPELL_NUMBER_MAPPING.put(GUIElements.SPELL_PANEL_3, 2);
        SPELL_NUMBER_MAPPING.put(GUIElements.SPELL_PANEL_4, 3);
    }

    private void initializeSkillNumberMapping() {
        SKILL_NUMBER_MAPPING.put(GUIElements.REQUIREMENT_CLASS_SUBCLASS_1, 0);
        SKILL_NUMBER_MAPPING.put(GUIElements.REQUIREMENT_CLASS_SUBCLASS_2, 1);
        SKILL_NUMBER_MAPPING.put(GUIElements.REQUIREMENT_CLASS_SUBCLASS_3, 2);
        SKILL_NUMBER_MAPPING.put(GUIElements.REQUIREMENT_CLASS_SUBCLASS_4, 3);
        SKILL_NUMBER_MAPPING.put(GUIElements.REQUIREMENT_CLASS_SUBCLASS_5, 4);
        SKILL_NUMBER_MAPPING.put(GUIElements.REQUIREMENT_CLASS_SUBCLASS_6, 5);
        SKILL_NUMBER_MAPPING.put(GUIElements.REQUIREMENT_CLASS_SUBCLASS_7, 6);
        SKILL_NUMBER_MAPPING.put(GUIElements.SKILL_LEVEL_1, 0);
        SKILL_NUMBER_MAPPING.put(GUIElements.SKILL_LEVEL_2, 1);
        SKILL_NUMBER_MAPPING.put(GUIElements.SKILL_LEVEL_3, 2);
        SKILL_NUMBER_MAPPING.put(GUIElements.SKILL_LEVEL_4, 3);
        SKILL_NUMBER_MAPPING.put(GUIElements.SKILL_LEVEL_5, 4);
        SKILL_NUMBER_MAPPING.put(GUIElements.SKILL_LEVEL_6, 5);
        SKILL_NUMBER_MAPPING.put(GUIElements.SKILL_LEVEL_7, 6);
    }

    @Override
    public void updateView() {
        HeroesRunesParametersModelParameter parameter = getModel().getParameter();
        ItemPriceParametersObject priceParametersObject = parameter.getPriceParametersObject();
        CreatureParameterObject creatureParameterObject = parameter.getCreatureParameterObject();
        List<CreatureSkillObject> creatureSkills = parameter.getCreatureSkills();
        List<HeroSpellObject> heroSpellObjects = parameter.getHeroSpellObjects();
        Icon icon = parameter.getIcon();

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

                Class<?> dtoClass = annotation.DTOClass();
                if (dtoClass.equals(ItemPriceParametersObject.class)) {
                    widget.getListener().updateWidgetValue(priceParametersObject);
                } else if (dtoClass.equals(CreatureParameterObject.class)) {
                    widget.getListener().updateWidgetValue(creatureParameterObject);
                } else if (dtoClass.equals(HeroSpellObject.class)) {
                    if (heroSpellObjects == null || heroSpellObjects.isEmpty()) {
                        getView().getTabPane().setEnabledAt(HeroesRunesParametersView.SPELL_PARAMETERS_TAB_INDEX, false);
                    } else {
                        getView().getTabPane().setEnabledAt(HeroesRunesParametersView.SPELL_PARAMETERS_TAB_INDEX, true);
                        Integer spellIndex = SPELL_NUMBER_MAPPING.get(annotation.GUIElementId());
                        if (spellIndex >= heroSpellObjects.size()) {
                            widget.setVisible(false);
                        } else {
                            widget.setVisible(true);
                            widget.getListener().updateWidgetValue(heroSpellObjects.get(spellIndex));
                        }
                    }
                } else if (dtoClass.equals(CreatureSkillObject.class)) {
                    if (creatureSkills == null || creatureSkills.isEmpty()) {
                        getView().getTabPane().setEnabledAt(HeroesRunesParametersView.SKILL_PARAMETERS_TAB_INDEX, false);
                    } else {
                        getView().getTabPane().setEnabledAt(HeroesRunesParametersView.SKILL_PARAMETERS_TAB_INDEX, true);
                        Integer skillIndex = SKILL_NUMBER_MAPPING.get(annotation.GUIElementId());
                        if (skillIndex >= creatureSkills.size()) {
                            widget.setVisible(false);
                        } else {
                            widget.setVisible(true);
                            widget.getListener().updateWidgetValue(creatureSkills.get(skillIndex));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(), e);
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
}