package sfgamedataeditor.views.main.modules.units.races.units;

import sfgamedataeditor.database.creatures.common.CreatureCommonParametersTableService;
import sfgamedataeditor.database.creatures.common.CreaturesCommonParameterObject;
import sfgamedataeditor.database.creatures.equipment.CreatureEquipmentObject;
import sfgamedataeditor.database.creatures.equipment.CreatureEquipmentTableService;
import sfgamedataeditor.database.creatures.production.buildings.CreatureBuildingsObject;
import sfgamedataeditor.database.creatures.production.buildings.CreatureBuildingsTableService;
import sfgamedataeditor.database.creatures.production.resources.CreatureResourcesObject;
import sfgamedataeditor.database.creatures.production.resources.CreatureResourcesTableService;
import sfgamedataeditor.database.creatures.spells.CreatureSpellObject;
import sfgamedataeditor.database.creatures.spells.CreatureSpellTableService;
import sfgamedataeditor.views.common.AbstractModulesPresenter;
import sfgamedataeditor.views.main.modules.units.races.units.parameters.UnitsParametersModel;
import sfgamedataeditor.views.main.modules.units.races.units.parameters.UnitsParametersModelParameter;

import javax.swing.*;
import java.util.List;

public class UnitListPresenter extends AbstractModulesPresenter<UnitListModelParameter, UnitListView, UnitsParametersModel> {

    public UnitListPresenter(UnitListView view) {
        super(view);
    }

    @Override
    protected UnitsParametersModel createModel() {
        String selectedCreatureName = getView().getSelectedModuleValue();
        Integer creatureId = CreatureCommonParametersTableService.INSTANCE.getCreatureIdByName(selectedCreatureName);
        CreaturesCommonParameterObject commonParameterObject = CreatureCommonParametersTableService.INSTANCE.getCreatureParametersByCreatureId(creatureId);
        List<CreatureEquipmentObject> creatureEquipment = CreatureEquipmentTableService.INSTANCE.getCreatureEquipmentByCreatureId(creatureId);
        List<CreatureSpellObject> creatureSpells = CreatureSpellTableService.INSTANCE.getCreatureSpellsByCreatureId(creatureId);
        List<CreatureResourcesObject> creatureResources = CreatureResourcesTableService.INSTANCE.getCreatureResourcesByCreatureId(creatureId);
        List<CreatureBuildingsObject> creatureBuildings = CreatureBuildingsTableService.INSTANCE.getCreatureBuildingsByCreatureId(creatureId);
        Icon icon = getView().getSelectedModuleIcon();
        UnitsParametersModelParameter parameter = new UnitsParametersModelParameter(commonParameterObject,
                creatureEquipment, creatureSpells, creatureResources, creatureBuildings, icon);
        return new UnitsParametersModel(parameter);
    }
}
