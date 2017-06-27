package sfgamedataeditor.views.main.modules.items.buildingplans;

import sfgamedataeditor.mvc.objects.AbstractPresenter;
import sfgamedataeditor.views.common.ObjectTuple;
import sfgamedataeditor.views.common.views.AbstractModulesView;
import sfgamedataeditor.views.main.modules.items.buildingplans.buildings.BuildingsPlanListView;
import sfgamedataeditor.views.utility.i18n.I18NService;
import sfgamedataeditor.views.utility.i18n.I18NTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildingPlansRacesView extends AbstractModulesView {

    public BuildingPlansRacesView() {
        super(I18NService.INSTANCE.getMessage(I18NTypes.COMMON, "races"));
    }

    @Override
    public Class<? extends AbstractPresenter> getPresenterClass() {
        return BuildingPlansRacesPresenter.class;
    }

    @Override
    public void fillSubViewsMappings() {
        List<ObjectTuple> mappings = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : BuildingRaceToItemPlansMapping.INSTANCE.mappings.entrySet()) {
            mappings.add(new ObjectTuple(entry.getKey(), entry.getValue()));
        }

        addMappings(mappings, BuildingsPlanListView.class);
    }
}
