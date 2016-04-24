package sfgamedataeditor.views.main.modules.skills.schools;

import sfgamedataeditor.ViewRegister;
import sfgamedataeditor.datamapping.Mappings;
import sfgamedataeditor.events.AbstractMetaEvent;
import sfgamedataeditor.events.ClassTuple;
import sfgamedataeditor.events.EventHandlerRegister;
import sfgamedataeditor.utils.I18N;
import sfgamedataeditor.views.common.AbstractModulesView;
import sfgamedataeditor.views.common.levelable.LevelableView;
import sfgamedataeditor.views.main.modules.common.modules.ModulesView;
import sfgamedataeditor.views.main.modules.skills.schools.parameters.ShowSkillParameterViewEvent;
import sfgamedataeditor.views.main.modules.skills.schools.parameters.SkillEventParameter;
import sfgamedataeditor.views.main.modules.skills.schools.parameters.SkillParametersMetaEvent;

public class SkillSchoolsView extends AbstractModulesView<ModulesView> {

    private final SkillEventParameter parameter;

    public SkillSchoolsView(ModulesView parentView) {
        super(parentView, I18N.INSTANCE.getMessage("skillSchools"));
        parameter = new SkillEventParameter();
        EventHandlerRegister.INSTANCE.addEventHandler(new SkillEventHandler());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillComboBoxMapping() {
        SkillParametersMetaEvent event = new SkillParametersMetaEvent();
        for (String s : Mappings.INSTANCE.SKILL_SCHOOL_MAP.keySet()) {
            addMapping(s, event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setEventParameter(AbstractMetaEvent event) {
        parameter.setSkillSchoolId(getSelectedSchoolId());

        LevelableView<SkillSchoolsView> levelableView = (LevelableView<SkillSchoolsView>) ViewRegister.INSTANCE.getView(new ClassTuple(LevelableView.class, SkillSchoolsView.class));
        if (levelableView != null) {
            parameter.setSkillLevel(levelableView.getSelectedLevel());
        }

        event.setEventParameter(ShowSkillParameterViewEvent.class, parameter);
    }

    private int getSelectedSchoolId() {
        String selectedSkillSchool = (String) getSelectedModuleValue();
        return Mappings.INSTANCE.SKILL_SCHOOL_MAP.get(selectedSkillSchool);
    }
}
