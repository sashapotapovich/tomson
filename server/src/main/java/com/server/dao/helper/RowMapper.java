package com.server.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {

	void setObj(T obj);

    T rowMap(ResultSet rs) throws SQLException, IllegalAccessException;
}
