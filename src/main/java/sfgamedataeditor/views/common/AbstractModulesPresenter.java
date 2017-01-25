package sfgamedataeditor.views.common;

import sfgamedataeditor.events.processing.EventProcessor;
import sfgamedataeditor.events.processing.ViewRegister;
import sfgamedataeditor.events.types.ShowContentViewEvent;
import sfgamedataeditor.mvc.objects.AbstractPresenter;
import sfgamedataeditor.mvc.objects.Model;
import sfgamedataeditor.mvc.objects.PresentableView;
import sfgamedataeditor.views.main.MainView;
import sfgamedataeditor.views.utility.SilentComboBoxValuesSetter;
import sfgamedataeditor.views.utility.ViewTools;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public abstract class AbstractModulesPresenter<M, V extends AbstractModulesView, C extends Model> extends AbstractPresenter<M, V> {
    protected AbstractModulesPresenter(V view) {
        super(view);
        final JComboBox<String> modulesComboBox = getView().getModulesComboBox();
        modulesComboBox.addItemListener(new DefaultComboBoxListener());
    }

    protected abstract C createModel();

    protected boolean isElementExistsInComboBox(String value) {
        JComboBox<String> modulesComboBox = getView().getModulesComboBox();
        int itemCount = modulesComboBox.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (modulesComboBox.getItemAt(i).equals(value)) {
                return true;
            }
        }

        return false;
    }

    protected void setModulesComboBoxValue(final Object value) {
        ViewTools.setComboBoxValuesSilently(new SilentComboBoxValuesSetter<String>(getView().getModulesComboBox()) {
            @Override
            protected void setValues() {
                getView().getModulesComboBox().setSelectedItem(value);
            }
        });
    }

    private final class DefaultComboBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            String selectedItem = (String) getView().getModulesComboBox().getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            Class<? extends PresentableView> classViewToShow = getView().getComboBoxMapping().get(selectedItem);
            Model model = createModel();
            EventProcessor.INSTANCE.process(new ShowContentViewEvent(classViewToShow, model));
        }
    }

    @Override
    public void renderView() {
        MainView mainView = ViewRegister.INSTANCE.getView(MainView.class);
        mainView.renderViewInsideNavigationPanel(getView());
    }

    @Override
    public void unRenderView() {
        MainView mainView = ViewRegister.INSTANCE.getView(MainView.class);
        mainView.unRenderViewInsideNavigationPanel(getView());
    }
}