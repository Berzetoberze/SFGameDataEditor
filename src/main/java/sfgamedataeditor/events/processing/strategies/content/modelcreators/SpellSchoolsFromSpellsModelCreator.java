package sfgamedataeditor.events.processing.strategies.content.modelcreators;

import sfgamedataeditor.database.objects.SpellName;
import sfgamedataeditor.database.objects.SpellParameters;
import sfgamedataeditor.database.tableservices.SpellNameTableService;
import sfgamedataeditor.database.tableservices.SpellParametersTableService;
import sfgamedataeditor.database.tableservices.SpellSchoolNameTableService;
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
        SpellName spellNameDAO = SpellNameTableService.INSTANCE.getSpellName(spellName);
        Integer spellType = spellNameDAO.spellType;
        SpellParameters spellParameter = SpellParametersTableService.INSTANCE.getSpellParametersBySpellType(spellType);
        String spellSchoolName = SpellSchoolNameTableService.INSTANCE.getSpellSchoolName(spellParameter);
        ModuleParameter parameter = new ModuleParameter(spellSchoolName);
        return new ModulesModel(parameter);
    }
}