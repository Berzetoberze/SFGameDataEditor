package sfgamedataeditor.views.main.modules.items.weapons.pieces.list;

import sfgamedataeditor.views.common.AbstractModulesController;
import sfgamedataeditor.views.main.modules.items.weapons.pieces.list.parameters.WeaponParametersModel;
import sfgamedataeditor.views.main.modules.items.weapons.pieces.list.parameters.WeaponParametersView;
import sfgamedataeditor.views.main.modules.merchants.inventory.items.models.WeaponModelCreator;
import sfgamedataeditor.views.utility.ViewTools;
import sfgamedataeditor.views.utility.i18n.I18NTypes;

import java.util.List;

public class WeaponPiecesController extends AbstractModulesController<WeaponPiecesModelParameter, WeaponPiecesView, WeaponParametersModel> {

    private WeaponModelCreator modelCreator = new WeaponModelCreator();

    public WeaponPiecesController(WeaponPiecesView view) {
        super(view);
    }

    @Override
    protected WeaponParametersModel createModel() {
        String selectedWeaponPiece = getView().getSelectedModuleValue();
        int itemId = ViewTools.getKeyByPropertyValue(selectedWeaponPiece, I18NTypes.ITEMS);
        return modelCreator.createModel(itemId);
    }

    @Override
    public void updateView() {
        List<String> weaponPiecesNames = getModel().getParameter().getWeaponPiecesNames();
        getView().clearComboBoxAndMapping();

        for (String weaponPiece : weaponPiecesNames) {
            getView().addMapping(weaponPiece, WeaponParametersView.class);
        }

        getView().reinitializeComboBox();
        setModulesComboBoxValue(getModel().getParameter().getSelectedWeaponPieceName());
    }
}
