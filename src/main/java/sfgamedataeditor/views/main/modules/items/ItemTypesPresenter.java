package sfgamedataeditor.views.main.modules.items;

import sfgamedataeditor.views.common.AbstractModulesPresenter;
import sfgamedataeditor.views.common.ModuleParameter;
import sfgamedataeditor.views.common.ModulesModel;

public class ItemTypesPresenter extends AbstractModulesPresenter<ModuleParameter, ItemTypesView, ModulesModel> {

    public ItemTypesPresenter(ItemTypesView view) {
        super(view);
    }

    @Override
    protected ModulesModel createModel() {
        return null;
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