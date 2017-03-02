package sfgamedataeditor.views.main.modules.spellbook.parameters;

import org.apache.log4j.Logger;
import sfgamedataeditor.common.GUIElement;
import sfgamedataeditor.common.IconElement;
import sfgamedataeditor.common.viewconfigurations.item.scrolls.GUIElements;
import sfgamedataeditor.common.widgets.AbstractWidget;
import sfgamedataeditor.common.widgets.common.combobox.level.LevelComboBoxParameter;
import sfgamedataeditor.database.items.price.parameters.ItemPriceParametersObject;
import sfgamedataeditor.database.items.price.parameters.ItemPriceParametersTableService;
import sfgamedataeditor.database.items.spelleffect.ItemSpellEffectsObject;
import sfgamedataeditor.database.items.spelleffect.ItemSpellEffectsTableService;
import sfgamedataeditor.events.processing.ViewRegister;
import sfgamedataeditor.mvc.objects.AbstractPresenter;
import sfgamedataeditor.views.main.MainView;
import sfgamedataeditor.views.utility.i18n.I18NService;
import sfgamedataeditor.views.utility.i18n.I18NTypes;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class SpellBookParametersPresenter extends AbstractPresenter<SpellBookParametersModelParameter, SpellBookParametersView> {

    private static final Integer SPELL_OBJECT_TYPE = Integer.valueOf(I18NService.INSTANCE.getMessage(I18NTypes.ITEM_TYPES_NAME_MAPPING, "items.spells"));
    private static final Logger LOGGER = Logger.getLogger(SpellBookParametersPresenter.class);

    public SpellBookParametersPresenter(SpellBookParametersView view) {
        super(view);
    }

    @Override
    public void updateView() {
        SpellBookParametersModelParameter parameter = getModel().getParameter();

        int selectedLevel = parameter.getScrollLevel();
        String scrollBaseName = parameter.getScrollBaseName();
        Set<Integer> scrollLevels = getScrollLevels(scrollBaseName);
        String scrollName = scrollBaseName + " - " + I18NService.INSTANCE.getMessage(I18NTypes.WEAPON_GUI, "level") + " " + selectedLevel;
        int itemId = ItemPriceParametersTableService.INSTANCE.getItemIdByItemNameAndType(scrollName, SPELL_OBJECT_TYPE);
        ItemPriceParametersObject priceParametersObject = ItemPriceParametersTableService.INSTANCE.getObjectByItemId(itemId);
        List<ItemSpellEffectsObject> itemSpellEffectsObjects = ItemSpellEffectsTableService.INSTANCE.getObjectsByItemId(itemId);
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

                int guiElementId = annotation.GUIElementId();
                if (guiElementId == GUIElements.LEVEL) {
                    LevelComboBoxParameter levelComboBoxParameter = new LevelComboBoxParameter(selectedLevel, scrollLevels);
                    widget.getListener().updateWidgetValue(levelComboBoxParameter);
                } else {
                    Class<?> dtoClass = annotation.DTOClass();
                    if (dtoClass.equals(ItemPriceParametersObject.class)) {
                        widget.getListener().updateWidgetValue(priceParametersObject);
                    } else if (dtoClass.equals(ItemSpellEffectsObject.class)) {
                        if (itemSpellEffectsObjects == null || itemSpellEffectsObjects.isEmpty()) {
                            panel.setVisible(false);
                        } else {
                            panel.setVisible(true);
                            // it's guaranteed that scroll has only one spell effect on it
                            widget.getListener().updateWidgetValue(itemSpellEffectsObjects.get(0));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private Set<Integer> getScrollLevels(String scrollBaseName) {
        Set<Integer> scrollLevels = new TreeSet<>();
        ResourceBundle bundle = I18NService.INSTANCE.getBundle(I18NTypes.ITEMS);
        String prefix = scrollBaseName + " - ";
        for (String key : bundle.keySet()) {
            String value = bundle.getString(key);
            if (value.startsWith(prefix)) {
                scrollLevels.add(Integer.valueOf(value.split("\\s")[3]));
            }
        }

        return scrollLevels;
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
