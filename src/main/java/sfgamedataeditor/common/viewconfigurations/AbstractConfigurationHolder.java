package sfgamedataeditor.common.viewconfigurations;

import sfgamedataeditor.mvc.objects.Model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfigurationHolder {

    private Map<Model, AbstractConfiguration> configurationMap = new HashMap<>();

    public AbstractConfigurationHolder() {
        fillConfigurationMapping();
    }

    protected abstract void fillConfigurationMapping();

    public void addConfiguration(Model model, AbstractConfiguration configuration) {
        configurationMap.put(model, configuration);
    }

    public AbstractConfiguration getConfiguration(Model model) {
        AbstractConfiguration configuration = configurationMap.get(model);
        if (configuration != null) {
            return configuration;
        }

        AbstractConfiguration defaultConfiguration = configurationMap.get(null);
        if (defaultConfiguration != null) {
            return defaultConfiguration;
        }

        return null;
    }
}