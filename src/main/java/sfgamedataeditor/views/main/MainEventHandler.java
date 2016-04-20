package sfgamedataeditor.views.main;

import sfgamedataeditor.ViewRegister;
import sfgamedataeditor.events.EventHandler;
import sfgamedataeditor.views.common.levelable.ShowLevelableViewEvent;
import sfgamedataeditor.views.common.notimplemented.ShowNotImplementedViewEvent;
import sfgamedataeditor.views.main.modules.common.buttons.ShowButtonsViewEvent;
import sfgamedataeditor.views.main.modules.common.eventhistory.ShowEventHistoryViewEvent;
import sfgamedataeditor.views.main.modules.common.modules.ShowModulesViewEvent;

public class MainEventHandler {

    @EventHandler
    public void onShowModulesView(ShowModulesViewEvent event) {
        ViewRegister.INSTANCE.process(event);
    }

    @EventHandler
    public void onShowButtonsView(ShowButtonsViewEvent event) {
        ViewRegister.INSTANCE.process(event);
    }

    @EventHandler
    public void onShowLevelableView(ShowLevelableViewEvent event) {
        ViewRegister.INSTANCE.process(event);
    }

    @EventHandler
    public void onShowEventHistoryView(ShowEventHistoryViewEvent event) {
        ViewRegister.INSTANCE.process(event);
    }

    @EventHandler
    public void onShowNotImplementedView(ShowNotImplementedViewEvent event) {
        ViewRegister.INSTANCE.process(event);
    }
}
