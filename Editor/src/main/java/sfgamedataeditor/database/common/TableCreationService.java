package sfgamedataeditor.database.common;

import sfgamedataeditor.views.utility.Pair;

import java.util.List;

public interface TableCreationService {
    void createTable();
    void addRecordsToTable(List<Pair<byte[], Long>> offsettedData);
    int getDataLength();
    Pair<Integer, Integer> getOffsetInterval();
    Class<? extends OffsetableObject> getDTOClass();
}
