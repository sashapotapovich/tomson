package com.server.dao.helper;

import com.common.annotation.CrearecNotSql;
import com.common.model.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class RowMapperImpl<T extends Entity> implements RowMapper<T> {

	@Setter
	private T obj;

	@Override
	public T rowMap(final ResultSet rs) throws IllegalAccessException, SQLException {
		List<Field> fields = ClassUtils.getFields(obj.getClass());
		int i = 1;
		for (final Field field : fields) {
			if (!field.isAnnotationPresent(CrearecNotSql.class)) {
				field.setAccessible(true);
				field.set(obj, rs.getObject(i++));
				field.setAccessible(false);
			}
		}
		return obj;
	}
}
