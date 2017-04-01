package edu.deanza.calendar.util.dal.mappers;

import java.util.Map;

/**
 * Created by karinaantonio on 8/29/16.
 */

public interface DataMapper<T> {

    T map(String name, Map<Object, Object> rawData);

}
