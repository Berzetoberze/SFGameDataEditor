package sfgamedataeditor.views;

import sfgamedataeditor.I18N;
import sfgamedataeditor.databind.files.FileData;
import sfgamedataeditor.databind.files.FileUtils;
import sfgamedataeditor.databind.files.FilesContainer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class FileSelectionView implements IView {
    public static final String CFF_FILE_EXTENSION = "cff";
    public static final String SFMOD_FILE_EXTENSION = "sfmod";

    private JPanel mainPanel;
    private JTextField originalFileField;
    private JLabel originalFileLabel;
    private JButton originalFileSelectorButton;
    private JLabel modificationFileLabel;
    private JTextField modificationFileField;
    private JButton modificationFileSelectorButton;
    private JButton okButton;

    public FileSelectionView() {
        originalFileLabel.setText(I18N.getMessage("fileSelectionWindowOriginalFileTextFieldCaption"));
        modificationFileLabel.setText(I18N.getMessage("fileSelectionWindowModificationFileTextFieldCaption"));
        okButton.setText(I18N.getMessage("ok"));
    }

    public static void showFileSelectionView() {
        final JFrame frame = new JFrame(I18N.getMessage("fileSelectionWindowCaption"));
        final FileSelectionView view = new FileSelectionView();
        frame.setContentPane(view.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        view.getOkButton().setEnabled(false);
        addOpenParametersEditorListener(frame, view);
        addOriginalFileSelectionListener(view);
        addModificationFileSelectionListener(view);
    }

    private static void addOriginalFileSelectionListener(final FileSelectionView view) {
        view.getOriginalFileSelectorButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileFilter fileFilter = new FileNameExtensionFilter(I18N.getMessage("cffFilesDescription"), CFF_FILE_EXTENSION);
                chooser.setFileFilter(fileFilter);
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.showOpenDialog(view.getMainPanel());
                File selectedFile = chooser.getSelectedFile();
                if (selectedFile == null) {
                    view.getOkButton().setEnabled(FilesContainer.getOriginalFile() != null);
                    return;
                }

                view.getOriginalFileField().setText(selectedFile.getAbsolutePath());
                RandomAccessFile file;
                try {
                    file = new RandomAccessFile(selectedFile.getAbsolutePath(), "r");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                    view.getOkButton().setEnabled(false);
                    return;
                }

                FilesContainer.setOriginalFile(new FileData(file, selectedFile.getParent() + File.separator, selectedFile.getName()));
                view.getOkButton().setEnabled(true);
            }
        });
    }

    private static void addModificationFileSelectionListener(final FileSelectionView view) {
        view.getModificationFileSelectorButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileFilter fileFilter = new FileNameExtensionFilter(I18N.getMessage("sfmodFilesDescription"), SFMOD_FILE_EXTENSION);
                chooser.setFileFilter(fileFilter);
                chooser.setAcceptAllFileFilterUsed(false);
                JPanel mainPanel = view.getMainPanel();
                chooser.showOpenDialog(mainPanel);
                File selectedFile = chooser.getSelectedFile();
                if (selectedFile == null) {
                    return;
                }

                ViewTools.setComponentsEnableStatus(mainPanel, false);
                if (!FileUtils.isModificationFileBasedOnOriginalFile(selectedFile.getPath())) {
                    String errorCaption = I18N.getMessage("error");
                    String errorMessage = I18N.getMessage("sfmodFilePrefix") + selectedFile.getName() + I18N.getMessage("basedOnAnotherCffFile");
                    JOptionPane.showMessageDialog(null, errorMessage, errorCaption, JOptionPane.ERROR_MESSAGE);
                    ViewTools.setComponentsEnableStatus(mainPanel, true);
                    return;
                }
                ViewTools.setComponentsEnableStatus(mainPanel, true);

                view.getModificationFileField().setText(selectedFile.getAbsolutePath());
                RandomAccessFile file;
                try {
                    file = new RandomAccessFile(selectedFile.getAbsolutePath(), "r");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                    return;
                }

                FilesContainer.setModificationFile(new FileData(file, selectedFile.getParent() + File.separator, selectedFile.getName()));
            }
        });
    }

    private static void addOpenParametersEditorListener(final JFrame frame,
            final FileSelectionView view) {
        view.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton okButton = view.getOkButton();
                JPanel mainPanel = view.getMainPanel();
                ViewTools.setComponentsEnableStatus(mainPanel, false);
                ViewTools.repaintButtonTextContent(okButton, frame, mainPanel, I18N.getMessage("temporaryModificationFileCreation"));
                boolean creationSuccess = FileUtils.createTemporaryModificationFile();
                if (!creationSuccess) {
                    String errorCaption = I18N.getMessage("error");
                    String errorMessage = I18N.getMessage("temporaryModificationFileCreationError");
                    JOptionPane.showMessageDialog(null, errorMessage, errorCaption, JOptionPane.ERROR_MESSAGE);
                    ViewTools.setComponentsEnableStatus(mainPanel, true);
                    ViewTools.repaintButtonTextContent(okButton, frame, mainPanel, I18N.getMessage("ok"));
                    return;
                }

                ViewTools.repaintButtonTextContent(okButton, frame, mainPanel, I18N.getMessage("processingData"));
                MainView.showMainView();
                frame.dispose();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getOriginalFileSelectorButton() {
        return originalFileSelectorButton;
    }

    public JButton getModificationFileSelectorButton() {
        return modificationFileSelectorButton;
    }

    public JTextField getOriginalFileField() {
        return originalFileField;
    }

    public JTextField getModificationFileField() {
        return modificationFileField;
    }
}
