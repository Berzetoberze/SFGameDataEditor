package sfgamedataeditor.views.main.modules.spells.schools.spells;

import java.util.List;

public class SpellModelParameter {
    private List<String> listOfSpells;
    private String selectedSpell;

    public SpellModelParameter(List<String> listOfSpells, String selectedSpell) {
        this.listOfSpells = listOfSpells;
        this.selectedSpell = selectedSpell;
    }

    public List<String> getListOfSpells() {
        return listOfSpells;
    }

    public String getSelectedSpell() {
        return selectedSpell;
    }
}