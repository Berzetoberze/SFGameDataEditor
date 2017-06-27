package sfgamedataeditor.views.main.modules.items.unitplans.units;

import sfgamedataeditor.mvc.objects.AbstractSubModuleParameter;
import sfgamedataeditor.mvc.objects.PresentableView;
import sfgamedataeditor.views.common.ObjectTuple;
import sfgamedataeditor.views.main.modules.items.unitplans.units.parameters.UnitsPlansParametersView;

import java.util.List;

public class UnitsPlanListModelParameter extends AbstractSubModuleParameter {

    public UnitsPlanListModelParameter(List<ObjectTuple> panelTuples, ObjectTuple selectedObjectName) {
        super(panelTuples, selectedObjectName);
    }

    @Override
    public Class<? extends PresentableView> getSubPanelsViewClass() {
        return UnitsPlansParametersView.class;
    }
}
