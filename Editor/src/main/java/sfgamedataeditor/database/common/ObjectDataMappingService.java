package sfgamedataeditor.database.common;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public enum ObjectDataMappingService {
    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(ObjectDataMappingService.class);

    private final Map<Class<?>, Map<DataPair, Field>> map = new HashMap<>();

    public void fillObjectWithData(Object object, byte[] buffer) {
        Class<?> aClass = object.getClass();
        Map<DataPair, Field> objectMapping = getObjectMapping(aClass);
        for (Map.Entry<DataPair, Field> dataPairFieldEntry : objectMapping.entrySet()) {
            int offset = dataPairFieldEntry.getKey().getOffset();
            int length = dataPairFieldEntry.getKey().getLength();

            int temp = getValue(buffer, offset, length);
            try {
                Field field = dataPairFieldEntry.getValue();
                field.setAccessible(true);
                field.set(object, temp);
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private Map<DataPair, Field> getObjectMapping(Class<?> ormObject) {
        if (!map.containsKey(ormObject)) {
            Map<DataPair, Field> innerMap = new TreeMap<>();
            Field[] declaredFields = ormObject.getDeclaredFields();
            for (Field field : declaredFields) {
                boolean isDataField = field.isAnnotationPresent(Data.class);
                if (!isDataField) {
                    continue;
                }

                Data data = field.getAnnotation(Data.class);
                DataPair pair = new DataPair(data.length(), data.offset());
                innerMap.put(pair, field);
            }

            map.put(ormObject, innerMap);
        }

        return map.get(ormObject);
    }

    private int getValue(byte[] value, int offset, int length) {
        int temp = 0;
        for (int i = offset; i < offset + length; i++) {
            temp += (value[i] & 0xFF) << ((i - offset) * 8);
        }

        return temp;
    }

    public byte[] serializeObject(Object daoObject) {
        Class<?> aClass = daoObject.getClass();
        Map<DataPair, Field> objectMapping = getObjectMapping(aClass);
        DataPair lastDataPair = (DataPair) ((TreeMap) objectMapping).lastKey();
        int objectByteLength = lastDataPair.getOffset() + lastDataPair.getLength();
        byte[] result = new byte[objectByteLength];

        for (Map.Entry<DataPair, Field> dataPairFieldEntry : objectMapping.entrySet()) {
            int offset = dataPairFieldEntry.getKey().getOffset();
            int length = dataPairFieldEntry.getKey().getLength();
            Field field = dataPairFieldEntry.getValue();
            field.setAccessible(true);
            try {
                int value = (int) field.get(daoObject);
                for (int i = offset; i < offset + length; i++) {
                    byte b = (byte) (value & 0xFF);
                    result[i] = b;
                    value >>= 8;
                }
            } catch (IllegalAccessException | ArrayIndexOutOfBoundsException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return result;
    }

    private static final class DataPair implements Comparable<DataPair> {
        private final int length;
        private final int offset;

        public DataPair(int length, int offset) {
            this.length = length;
            this.offset = offset;
        }

        public int getLength() {
            return length;
        }

        public int getOffset() {
            return offset;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(DataPair o) {
            return offset == o.getOffset() ? 0 : (offset > o.getOffset() ? 1 : -1);
        }
    }
}
