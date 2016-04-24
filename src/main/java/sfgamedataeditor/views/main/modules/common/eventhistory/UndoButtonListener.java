package sfgamedataeditor.views.main.modules.common.eventhistory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UndoButtonListener implements ActionListener {

    private final JButton redoButton;
    private final JButton undoButton;

    public UndoButtonListener(JButton redoButton, JButton undoButton) {
        this.redoButton = redoButton;
        this.undoButton = undoButton;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        EventHistory.INSTANCE.undo();
        redoButton.setEnabled(EventHistory.INSTANCE.isRedoPossible());
        undoButton.setEnabled(EventHistory.INSTANCE.isUndoPossible());
    }
}
