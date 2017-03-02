package sfgamedataeditor.common.widgets.creatures.equipment;

import sfgamedataeditor.common.widgets.AbstractWidgetListener;
import sfgamedataeditor.database.common.OffsetableObject;
import sfgamedataeditor.database.items.price.parameters.ItemPriceParametersTableService;
import sfgamedataeditor.events.processing.EventProcessor;
import sfgamedataeditor.events.types.ShowContentViewEvent;
import sfgamedataeditor.mvc.objects.Model;
import sfgamedataeditor.mvc.objects.PresentableView;
import sfgamedataeditor.views.common.notimplemented.NotImplementedView;
import sfgamedataeditor.views.main.modules.items.armor.pieces.list.parameters.ArmorParametersView;
import sfgamedataeditor.views.main.modules.items.miscellaneous.parameters.MiscellaneousParametersView;
import sfgamedataeditor.views.main.modules.items.spellscrolls.schools.parameters.SpellScrollsParametersView;
import sfgamedataeditor.views.main.modules.items.weapons.pieces.list.parameters.WeaponParametersView;
import sfgamedataeditor.views.main.modules.merchants.inventory.items.models.*;
import sfgamedataeditor.views.utility.Pair;
import sfgamedataeditor.views.utility.ViewTools;
import sfgamedataeditor.views.utility.i18n.I18NService;
import sfgamedataeditor.views.utility.i18n.I18NTypes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EquipmentWidgetListener extends AbstractWidgetListener<EquipmentWidget, OffsetableObject> implements ItemListener, ActionListener {

    private final Map<Integer, Pair<Class<? extends PresentableView>, ModelCreator>> itemTypesClassViews = new HashMap<>();

    public EquipmentWidgetListener(EquipmentWidget component, Field... mappedFields) {
        super(component, mappedFields);
        initializeItemTypesClassViewMap();
    }

    private void initializeItemTypesClassViewMap() {
        addArmorViewsMapping();
        addWeaponsViewsMapping();
        addSpellScrollsViewsMapping();
        addMiscellaneousViewsMapping();
    }

    private void addSpellScrollsViewsMapping() {
        SpellScrollsModelCreator creator = new SpellScrollsModelCreator();
        Pair<Class<? extends PresentableView>, ModelCreator> pair = new Pair<Class<? extends PresentableView>, ModelCreator>(SpellScrollsParametersView.class, creator);
        int scrollsTypeId = getItemTypeByNameMapping("items.scrolls");
        itemTypesClassViews.put(scrollsTypeId, pair);
    }

    private void addMiscellaneousViewsMapping() {
        MiscellaneousModelCreator creator = new MiscellaneousModelCreator();
        Pair<Class<? extends PresentableView>, ModelCreator> pair = new Pair<Class<? extends PresentableView>, ModelCreator>(MiscellaneousParametersView.class, creator);
        int miscellaneousTypeId = getItemTypeByNameMapping("items.miscellaneous");
        itemTypesClassViews.put(miscellaneousTypeId, pair);
    }

    private void addWeaponsViewsMapping() {
        WeaponModelCreator creator = new WeaponModelCreator();
        Pair<Class<? extends PresentableView>, ModelCreator> pair = new Pair<Class<? extends PresentableView>, ModelCreator>(WeaponParametersView.class, creator);
        int oneHandWeaponTypeId = getItemTypeByNameMapping("items.1h.weapon");
        itemTypesClassViews.put(oneHandWeaponTypeId, pair);

        int twoHandWeaponTypeId = getItemTypeByNameMapping("items.2h.weapon");
        itemTypesClassViews.put(twoHandWeaponTypeId, pair);

        int bowsTypeId = getItemTypeByNameMapping("items.bow");
        itemTypesClassViews.put(bowsTypeId, pair);
    }

    private void addArmorViewsMapping() {
        ArmorModelCreator creator = new ArmorModelCreator();
        Pair<Class<? extends PresentableView>, ModelCreator> pair = new Pair<Class<? extends PresentableView>, ModelCreator>(ArmorParametersView.class, creator);
        int helmetsId = getItemTypeByNameMapping("items.armor.helmets");
        itemTypesClassViews.put(helmetsId, pair);

        int chestArmorTypeId = getItemTypeByNameMapping("items.armor.chest.armor");
        itemTypesClassViews.put(chestArmorTypeId, pair);

        int robesTypeId = getItemTypeByNameMapping("items.armor.robes");
        itemTypesClassViews.put(robesTypeId, pair);

        int legsArmorTypeId = getItemTypeByNameMapping("items.armor.legs.armor");
        itemTypesClassViews.put(legsArmorTypeId, pair);

        int shieldsTypeId = getItemTypeByNameMapping("items.armor.shield");
        itemTypesClassViews.put(shieldsTypeId, pair);

        int ringsTypeId = getItemTypeByNameMapping("items.armor.rings");
        itemTypesClassViews.put(ringsTypeId, pair);
    }

    private Integer getItemTypeByNameMapping(String nameMapping) {
        return Integer.parseInt(I18NService.INSTANCE.getMessage(I18NTypes.ITEM_TYPES_NAME_MAPPING, nameMapping));
    }

    @Override
    protected int[] getFieldValues() {
        String selectedItemName = (String) getWidget().getItemPieceComboBox().getSelectedItem();
        Integer selectedItemId = ItemPriceParametersTableService.INSTANCE.getItemIdByItemName(selectedItemName);
        return new int[]{selectedItemId};
    }

    @Override
    protected void setFieldValues(int[] value) {
        int itemId = value[0];
        if (itemId == 0) {
            return;
        }

        String itemName = I18NService.INSTANCE.getMessage(I18NTypes.ITEMS, String.valueOf(itemId));
        int itemPieceId = ItemPriceParametersTableService.INSTANCE.getItemTypeIdByItemId(itemId);
        String itemPieceNameKey = ViewTools.getKeyStringByPropertyValue(String.valueOf(itemPieceId), I18NTypes.ITEM_TYPES_NAME_MAPPING);
        String itemTypeName = I18NService.INSTANCE.getMessage(I18NTypes.COMMON, itemPieceNameKey);
        getWidget().getItemTypeComboBox().setSelectedItem(itemTypeName);
        updateItemNames();
        getWidget().getItemPieceComboBox().setSelectedItem(itemName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getWidget().getItemPieceComboBox().getSelectedItem() == null) {
            return;
        }

        String itemTypeName = (String) getWidget().getItemTypeComboBox().getSelectedItem();
        int itemTypeId = getItemTypeByNameMapping(itemTypeName);
        String selectedItemName = (String) getWidget().getItemPieceComboBox().getSelectedItem();
        Integer itemId = ItemPriceParametersTableService.INSTANCE.getItemIdByItemNameAndType(selectedItemName, itemTypeId);
        Class<? extends PresentableView> classViewToShow = getItemParametersViewClassByItemId(itemId);
        Model model = createModel(itemId);
        EventProcessor.INSTANCE.process(new ShowContentViewEvent(classViewToShow, model));
    }

    private Class<? extends PresentableView> getItemParametersViewClassByItemId(int itemId) {
        int itemTypeId = ItemPriceParametersTableService.INSTANCE.getItemTypeIdByItemId(itemId);
        Pair<Class<? extends PresentableView>, ModelCreator> pair = itemTypesClassViews.get(itemTypeId);
        // TODO remove in future, stub for not implemented items sold by merchants
        if (pair == null) {
            return NotImplementedView.class;
        } else {
            return pair.getKey();
        }
    }

    private Model createModel(Integer itemId) {
        int itemTypeId = ItemPriceParametersTableService.INSTANCE.getItemTypeIdByItemId(itemId);
        Pair<Class<? extends PresentableView>, ModelCreator> pair = itemTypesClassViews.get(itemTypeId);
        // TODO remove in future, stub for not implemented items sold by merchants
        if (pair == null) {
            return null;
        } else {
            return pair.getValue().createModel(itemId, null);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }

        if (e.getSource().equals(getWidget().getItemTypeComboBox())) {
            updateItemNames();
            return;
        }

        if (e.getSource().equals(getWidget().getItemPieceComboBox())) {
            setWidgetValueToDTOField();
        }
    }

    private void updateItemNames() {
        String itemTypeName = (String) getWidget().getItemTypeComboBox().getSelectedItem();
        String itemTypeI18NKey = ViewTools.getKeyStringByPropertyValue(itemTypeName, I18NTypes.COMMON);
        String itemPieceType = I18NService.INSTANCE.getMessage(I18NTypes.ITEM_TYPES_NAME_MAPPING, itemTypeI18NKey);
        final Set<String> itemNames = ItemPriceParametersTableService.INSTANCE.getItemsByItemType(Integer.parseInt(itemPieceType));

        final JComboBox<String> itemPieceComboBox = getWidget().getItemPieceComboBox();
        itemPieceComboBox.removeAllItems();
        for (String itemName : itemNames) {
            itemPieceComboBox.addItem(itemName);
        }
    }
}
