package sfgamedataeditor.events.processing.strategies.content.modelcreators;

import sfgamedataeditor.database.spellname.SpellNameObject;
import sfgamedataeditor.database.spellname.SpellNameTableService;
import sfgamedataeditor.database.spellparameters.SpellParametersObject;
import sfgamedataeditor.database.spellparameters.SpellParametersTableService;
import sfgamedataeditor.database.spellschoolname.SpellSchoolNameTableService;
import sfgamedataeditor.mvc.ModelCreator;
import sfgamedataeditor.views.common.ModuleParameter;
import sfgamedataeditor.views.common.ModulesModel;
import sfgamedataeditor.views.main.modules.spells.schools.spells.SpellModel;

public class SpellSchoolsFromSpellsModelCreator implements ModelCreator<ModulesModel, SpellModel> {

    @Override
    public ModulesModel createModel(SpellModel childModel) {
        // no difference which exactly spell we take, cause we WILL select first suitable spell school
        String spellName = childModel.getParameter().getListOfSpells().get(0);
        // TODO maybe store whole SpellParameter inside SpellController, to limit DB queries
        SpellNameObject spellNameDAO = SpellNameTableService.INSTANCE.getSpellName(spellName);
        Integer spellType = spellNameDAO.spellType;
        SpellParametersObject spellParameter = SpellParametersTableService.INSTANCE.getSpellParametersBySpellType(spellType);
        String spellSchoolName = SpellSchoolNameTableService.INSTANCE.getSpellSchoolName(spellParameter);
        ModuleParameter parameter = new ModuleParameter(spellSchoolName);
        return new ModulesModel(parameter);
    }
}
