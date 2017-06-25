package sfgamedataeditor.views.main.modules.units.races.units;

import sfgamedataeditor.mvc.objects.AbstractSubModuleParameter;
import sfgamedataeditor.mvc.objects.PresentableView;
import sfgamedataeditor.views.common.SubViewPanelTuple;
import sfgamedataeditor.views.main.modules.units.races.units.parameters.UnitsParametersView;

import java.util.List;

public class UnitListModelParameter extends AbstractSubModuleParameter {

    public UnitListModelParameter(List<SubViewPanelTuple> panelTuples, String selectedObjectName) {
        super(panelTuples, selectedObjectName);
    }

    @Override
    public Class<? extends PresentableView> getSubPanelsViewClass() {
        return UnitsParametersView.class;
    }
}
