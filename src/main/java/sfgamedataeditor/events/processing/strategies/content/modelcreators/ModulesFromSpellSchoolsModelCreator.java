package sfgamedataeditor.events.processing.strategies.content.modelcreators;

import sfgamedataeditor.mvc.ModelCreator;
import sfgamedataeditor.utils.I18N;
import sfgamedataeditor.views.common.ModuleParameter;
import sfgamedataeditor.views.common.ModulesModel;

public class ModulesFromSpellSchoolsModelCreator implements ModelCreator<ModulesModel, ModulesModel> {

    @Override
    public ModulesModel createModel(ModulesModel childModel) {
        ModuleParameter parameter = new ModuleParameter(I18N.INSTANCE.getMessage("spells"));
        return new ModulesModel(parameter);
    }
}
