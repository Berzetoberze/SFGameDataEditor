package sfgamedataeditor.views.main.modules.items.miscellaneous.parameters;

import org.apache.log4j.Logger;
import sfgamedataeditor.common.GUIElement;
import sfgamedataeditor.common.widgets.AbstractWidget;
import sfgamedataeditor.database.items.price.parameters.ItemPriceParametersObject;
import sfgamedataeditor.events.processing.ViewRegister;
import sfgamedataeditor.mvc.objects.AbstractPresenter;
import sfgamedataeditor.views.main.MainView;

import javax.swing.*;
import java.lang.reflect.Field;

public class MiscellaneousParametersPresenter extends AbstractPresenter<MiscellaneousParametersModelParameter, MiscellaneousParametersView> {

    private static final Logger LOGGER = Logger.getLogger(MiscellaneousParametersPresenter.class);

    public MiscellaneousParametersPresenter(MiscellaneousParametersView view) {
        super(view);
    }

    @Override
    public void updateView() {
        MiscellaneousParametersModelParameter parameter = getModel().getParameter();
        ItemPriceParametersObject priceParametersObject = parameter.getPriceParametersObject();

        Field[] declaredFields = getView().getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            GUIElement annotation = declaredField.getAnnotation(GUIElement.class);
            if (annotation == null) {
                continue;
            }

            try {
                declaredField.setAccessible(true);
                JPanel panel = (JPanel) declaredField.get(getView());
                AbstractWidget widget = (AbstractWidget) panel.getComponent(0);
                widget.getListener().updateWidgetValue(priceParametersObject);
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void renderView() {
        MainView mainView = ViewRegister.INSTANCE.getView(MainView.class);
        mainView.renderViewInsideContentPanel(getView());
    }

    @Override
    public void unRenderView() {
        MainView mainView = ViewRegister.INSTANCE.getView(MainView.class);
        mainView.unRenderViewInsideContentPanel(getView());
    }
}