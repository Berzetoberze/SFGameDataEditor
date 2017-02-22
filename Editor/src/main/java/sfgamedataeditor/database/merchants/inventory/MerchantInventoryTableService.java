package sfgamedataeditor.database.merchants.inventory;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.log4j.Logger;
import sfgamedataeditor.database.common.CommonTableService;
import sfgamedataeditor.database.common.TableCreationService;
import sfgamedataeditor.database.merchants.items.MerchantInventoryItemsTableService;
import sfgamedataeditor.dataextraction.DTOOffsetTypes;
import sfgamedataeditor.views.utility.Pair;
import sfgamedataeditor.views.utility.i18n.I18NService;
import sfgamedataeditor.views.utility.i18n.I18NTypes;

import java.sql.SQLException;
import java.util.List;

public enum MerchantInventoryTableService implements TableCreationService {
    INSTANCE {
        @Override
        public void createTable() {
            CommonTableService.INSTANCE.recreateTable(MerchantInventoryObject.class);
        }

        @Override
        public void addRecordsToTable(List<Pair<byte[], Long>> offsettedData) {
            CommonTableService.INSTANCE.addRecordsToTable(MerchantInventoryObject.class, offsettedData);
        }

        @Override
        public DTOOffsetTypes getDTOOffsetType() {
            return DTOOffsetTypes.MERCHANT_INVENTORY;
        }
    };

    private static final Logger LOGGER = Logger.getLogger(MerchantInventoryTableService.class);

    public String getMerchantNameByItemId(List<Integer> itemIds) {
        ConnectionSource connectionSource = CommonTableService.INSTANCE.getConnectionSource();
        Dao<MerchantInventoryObject, ?> dao;
        try {
            dao = DaoManager.createDao(connectionSource, MerchantInventoryObject.class);
            Integer inventoryId = MerchantInventoryItemsTableService.INSTANCE.getMerchantInventoryIdByInventoryItemIds(itemIds);
            List<MerchantInventoryObject> merchants = dao.queryBuilder().where().eq("inventoryId", inventoryId).query();

            String merchantId = String.valueOf(merchants.get(0).merchantId);
            return I18NService.INSTANCE.getMessage(I18NTypes.CREATURES, merchantId);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}