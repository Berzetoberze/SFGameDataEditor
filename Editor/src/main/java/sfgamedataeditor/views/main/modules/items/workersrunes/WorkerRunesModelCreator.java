package sfgamedataeditor.views.main.modules.items.workersrunes;

import sfgamedataeditor.database.items.price.parameters.ItemPriceParametersObject;
import sfgamedataeditor.database.items.price.parameters.ItemPriceParametersTableService;
import sfgamedataeditor.views.common.model.creators.ModelCreator;
import sfgamedataeditor.views.main.modules.items.workersrunes.parameters.WorkersRunesParametersModel;
import sfgamedataeditor.views.main.modules.items.workersrunes.parameters.WorkersRunesParametersModelParameter;

import javax.swing.*;

public class WorkerRunesModelCreator implements ModelCreator<WorkersRunesParametersModel> {
    @Override
    public WorkersRunesParametersModel createModel(int objectId, Icon icon) {
        ItemPriceParametersObject object = ItemPriceParametersTableService.INSTANCE.getObjectByItemId(objectId);
        String runeName = object.name;
        WorkersRunesParametersModelParameter parameter = new WorkersRunesParametersModelParameter(runeName, 1, icon);
        return new WorkersRunesParametersModel(parameter);
    }
}
