package sfgamedataeditor.dataextraction;

import org.apache.log4j.Logger;
import sfgamedataeditor.database.tableservices.SkillNameTableService;
import sfgamedataeditor.database.tableservices.SkillParametersTableService;
import sfgamedataeditor.database.tableservices.SpellNameTableService;
import sfgamedataeditor.database.tableservices.SpellParametersTableService;
import sfgamedataeditor.databind.Pair;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public enum DataFilesParser {
    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(DataFilesParser.class);

    public void extractSkillsDataFromFile(RandomAccessFile file) {
        SkillNameTableService.INSTANCE.createSkillNameTable();
        SkillParametersTableService.INSTANCE.createSkillParametersTable();

        List<Pair<Integer, Integer>> skillOffsets = DataOffsetProvider.INSTANCE.getSkillOffsets();
        int dataLength = DataOffsetProvider.INSTANCE.getSkillDataLength();
        List<Pair<byte[], Long>> offsettedData = readData(file, skillOffsets, dataLength);
        SkillParametersTableService.INSTANCE.addRecordsToSkillParametersTable(offsettedData);
    }

    public void extractSpellsDataFromFile(RandomAccessFile file) {
        SpellNameTableService.INSTANCE.createSpellNameTable();
        SpellParametersTableService.INSTANCE.createSpellParametersTable();

        List<Pair<Integer, Integer>> offsets = DataOffsetProvider.INSTANCE.getSpellOffsets();
        int dataLength = DataOffsetProvider.INSTANCE.getSpellDataLength();
        List<Pair<byte[], Long>> offsettedData = readData(file, offsets, dataLength);
        SpellParametersTableService.INSTANCE.addRecordsToSpellParametersTable(offsettedData);
    }

    private List<Pair<byte[], Long>> readData(RandomAccessFile file, List<Pair<Integer, Integer>> dataOffsets, int dataLength) {
        List<Pair<byte[], Long>> result = new ArrayList<>();
        try {
            for (Pair<Integer, Integer> pair : dataOffsets) {
                file.seek(pair.getKey());
                byte[] buffer = new byte[dataLength];
                while (file.getFilePointer() < pair.getValue()) {
                    file.read(buffer);
                    long offset = file.getFilePointer() - dataLength;
                    result.add(new Pair<>(buffer.clone(), offset));
                    file.seek(file.getFilePointer() + dataLength);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return result;
    }

    public void recreateAllMaps() {
        // TODO check necessity and recreate tables if really needed
//        extractSkillsDataFromFile();
//        extractSpellsDataFromFile();
//        TableCreationUtils.createSpellSchoolNameTable();
    }
}