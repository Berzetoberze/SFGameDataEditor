package sfgamedataeditor.database.items.price.parameters;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.log4j.Logger;
import sfgamedataeditor.database.common.CommonTableService;
import sfgamedataeditor.database.common.DTODecorator;
import sfgamedataeditor.database.common.OffsetableObject;
import sfgamedataeditor.database.common.TableCreationService;
import sfgamedataeditor.views.utility.Pair;
import sfgamedataeditor.views.utility.i18n.I18NService;
import sfgamedataeditor.views.utility.i18n.I18NTypes;

import java.sql.SQLException;
import java.util.*;

public enum ItemPriceParametersTableService implements TableCreationService {
    INSTANCE {
        @Override
        public void createTable() {
            CommonTableService.INSTANCE.recreateTable(ItemPriceParametersObject.class);
        }

        @Override
        public void addRecordsToTable(List<Pair<byte[], Long>> offsettedData) {
            CommonTableService.INSTANCE.addRecordsToTable(ItemPriceParametersObject.class, offsettedData, new ItemsObjectDecorator());
        }

        @Override
        public int getDataLength() {
            return 22;
        }

        @Override
        public Pair<Integer, Integer> getOffsetInterval() {
            return new Pair<>(0x0006359E, 0x000897DB);
        }

        @Override
        public Class<? extends OffsetableObject> getDTOClass() {
            return ItemPriceParametersObject.class;
        }
    };

    private static final Logger LOGGER = Logger.getLogger(ItemPriceParametersTableService.class);

    public Set<String> getItemsByItemType(int typeId) {
        ConnectionSource connectionSource = CommonTableService.INSTANCE.getConnectionSource();
        final Dao<ItemPriceParametersObject, String> dao;
        try {
            dao = DaoManager.createDao(connectionSource, ItemPriceParametersObject.class);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptySet();
        }

        try {
            List<ItemPriceParametersObject> objects = dao.queryBuilder().where().eq("typeId", typeId).query();
            Set<String> itemNames = new TreeSet<>();
            for (ItemPriceParametersObject object : objects) {
                itemNames.add(I18NService.INSTANCE.getMessage(I18NTypes.ITEMS, String.valueOf(object.itemId)));
            }

            return itemNames;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptySet();
        }
    }

    public int getItemTypeIdByItemId(int itemId) {
        ItemPriceParametersObject object = getObjectByItemId(itemId);
        if (object == null) {
            return 0;
        } else {
            return object.typeId;
        }
    }

    public ItemPriceParametersObject getObjectByItemId(int itemId) {
        ConnectionSource connectionSource = CommonTableService.INSTANCE.getConnectionSource();
        final Dao<ItemPriceParametersObject, String> dao;
        try {
            dao = DaoManager.createDao(connectionSource, ItemPriceParametersObject.class);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }

        try {
            List<ItemPriceParametersObject> objects = dao.queryBuilder().where().eq("itemId", itemId).query();
            // one of possible cases - corpse loot object trying to get so called "Epmty" slot - item with "itemId = 0"
            if (objects.isEmpty()) {
                return null;
            }

            return objects.get(0);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public List<Pair<String, Integer>> getItemNameIdPairByItemNamePart(String namePart, Long limit, Integer... typeId) {
        List<ItemPriceParametersObject> objects = getItemObjectsByItemNamePartAndType(namePart, limit, typeId);
        List<Pair<String, Integer>> result = new ArrayList<>();
        for (ItemPriceParametersObject object : objects) {
            result.add(new Pair<>(object.name, object.itemId));
        }

        return result;
    }

    public Integer getItemIdByItemName(String name) {
        return getItemIdByItemNameAndType(name);
    }

    public Integer getItemIdByItemNameAndType(String name, Integer... typeId) {
        ConnectionSource connectionSource = CommonTableService.INSTANCE.getConnectionSource();
        final Dao<ItemPriceParametersObject, String> dao;
        try {
            dao = DaoManager.createDao(connectionSource, ItemPriceParametersObject.class);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }

        try {
            SelectArg selectArg = new SelectArg(name);
            QueryBuilder<ItemPriceParametersObject, String> builder = dao.queryBuilder();
            Where<ItemPriceParametersObject, String> where = builder.where().like("name", selectArg);
            if (typeId != null && typeId.length != 0) {
                where = where.and().in("typeId", (Object[]) typeId);
            }

            List<ItemPriceParametersObject> objects = where.query();
            if (objects.isEmpty()) {
                return null;
            } else {
                return objects.get(0).itemId;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private List<ItemPriceParametersObject> getItemObjectsByItemNamePartAndType(String namePart, Long limit, Integer... typeId) {
        ConnectionSource connectionSource = CommonTableService.INSTANCE.getConnectionSource();
        final Dao<ItemPriceParametersObject, String> dao;
        try {
            dao = DaoManager.createDao(connectionSource, ItemPriceParametersObject.class);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }

        try {
            SelectArg selectArg = new SelectArg("%" + namePart + "%");
            QueryBuilder<ItemPriceParametersObject, String> builder = dao.queryBuilder();
            if (limit != null) {
                builder = builder.limit(limit);
            }

            Where<ItemPriceParametersObject, String> where = builder.where().like("name", selectArg);
            if (typeId != null && typeId.length != 0) {
                where = where.and().in("typeId", (Object[]) typeId);
            }

            return where.query();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private static final class ItemsObjectDecorator implements DTODecorator<ItemPriceParametersObject> {

        @Override
        public ItemPriceParametersObject decorateObject(ItemPriceParametersObject object) {
            Integer itemId = object.itemId;
            object.name = I18NService.INSTANCE.getMessage(I18NTypes.ITEMS, String.valueOf(itemId));
            return object;
        }
    }
}
