package sfgamedataeditor.views.main.modules.units.races;

import sfgamedataeditor.views.common.AbstractModulesController;
import sfgamedataeditor.views.common.ModuleParameter;
import sfgamedataeditor.views.main.modules.units.races.units.UnitListModel;
import sfgamedataeditor.views.main.modules.units.races.units.UnitListModelParameter;

import java.util.List;

public class UnitRacesController extends AbstractModulesController<ModuleParameter, UnitRacesView, UnitListModel> {

    public UnitRacesController(UnitRacesView view) {
        super(view);
    }

    @Override
    protected UnitListModel createModel() {
        String selectedRaceName = getView().getSelectedModuleValue();
        List<String> unitNames = UnitMapping.INSTANCE.getUnitNames(selectedRaceName);
        UnitListModelParameter parameter = new UnitListModelParameter(unitNames, null);
        return new UnitListModel(parameter);
    }

    @Override
    public void updateView() {
        if (getModel() == null) {
            setModulesComboBoxValue(null);
            return;
        }

        String moduleName = getModel().getParameter().getModuleName();
        if (isElementExistsInComboBox(moduleName)) {
            setModulesComboBoxValue(moduleName);
        } else {
            setModulesComboBoxValue(null);
        }
    }
}
