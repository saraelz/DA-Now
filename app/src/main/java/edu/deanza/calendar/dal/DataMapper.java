package edu.deanza.calendar.dal;

import java.util.Map;

/**
 * Created by karinaantonio on 8/29/16.
 */

interface DataMapper<T> {

    T map(String name, Map<Object, Object> rawData);

}
