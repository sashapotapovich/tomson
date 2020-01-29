package com.server.dao.helper;


import com.common.annotation.CrearecAliasSql;
import com.common.annotation.CrearecNotSql;
import com.common.model.Entity;
import org.test.di.annotations.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public class SqlBuilder {

	private static final String SQL_INSERT = "INSERT INTO %s.%s (%s) VALUES (%s)";
	private static final String SQL_DELETE = "DELETE FROM %s.%s WHERE id = ?";
	private static final String SQL_NEXT_ID = "SELECT nextval('%s.%s_id_seq'::regclass)";
	private static final String SQL_SELECT_BY_ID = "SELECT %s FROM %s.%s WHERE id = ?";
	private static final String SQL_SELECT = "SELECT %s FROM %s.%s";

	private final String schema = "public";

	public <T extends Entity> String getInsertSQL(final T obj) {
		List<Field> fields = ClassUtils.getFields(obj.getClass());
		String tableName = obj.getClass().getSimpleName().toLowerCase();

		StringBuilder values = new StringBuilder();
		StringBuilder fieldsInLine = new StringBuilder();
		for (final Field field : fields) {
			if (!field.isAnnotationPresent(CrearecNotSql.class)) {
				String fieldName = field.getName();
				if (field.isAnnotationPresent(CrearecAliasSql.class)) {
					fieldName = field.getAnnotation(CrearecAliasSql.class).value();
				}
				fieldsInLine.append(String.format("%s, ", fieldName));
				values.append("?, ");
			}
		}
		removeLastComma(values);
		removeLastComma(fieldsInLine);

		System.out.println("INSERT SQL");
		System.out.println(String.format(SQL_INSERT, schema, tableName, fieldsInLine, values));

		return String.format(SQL_INSERT, schema, tableName, fieldsInLine, values);
	}

	public String getNextIdSQL(final Class<? extends Entity> clazz) {
		return String.format(SQL_NEXT_ID, schema, clazz.getCanonicalName());
	}

	public String getSelectByIdSQL(final Class<? extends Entity> clazz) {
		List<Field> fields = ClassUtils.getFields(clazz);
		StringBuilder fieldsInLine = new StringBuilder();
		for (final Field field : fields) {
			if (!field.isAnnotationPresent(CrearecNotSql.class)) {
				String fieldName = field.getName();
				if (field.isAnnotationPresent(CrearecAliasSql.class)) {
					fieldName = field.getAnnotation(CrearecAliasSql.class).value();
				}
				fieldsInLine.append(fieldName);
				fieldsInLine.append(", ");
			}
		}
		removeLastComma(fieldsInLine);
		return String.format(SQL_SELECT_BY_ID, fieldsInLine, schema, clazz.getSimpleName());
	}

	public String getSelectSQL(final Class<? extends Entity> clazz) {
		List<Field> fields = ClassUtils.getFields(clazz);
		StringBuilder fieldsInLine = new StringBuilder();
		for (final Field field : fields) {
			if (!field.isAnnotationPresent(CrearecNotSql.class)) {
				String fieldName = field.getName();
				if (field.isAnnotationPresent(CrearecAliasSql.class)) {
					fieldName = field.getAnnotation(CrearecAliasSql.class).value();
				}
				fieldsInLine.append(fieldName);
				fieldsInLine.append(", ");
			}
		}
		removeLastComma(fieldsInLine);
		return String.format(SQL_SELECT, fieldsInLine, schema, clazz.getSimpleName().toLowerCase());
	}

	public String getDeleteSQL(final Class<? extends Entity> clazz) {
		return String.format(SQL_DELETE, schema, clazz.getCanonicalName());
	}

	private static void removeLastComma(final StringBuilder stringBuilder) {
		if (stringBuilder.lastIndexOf(",") > 0) {
			stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());
		}
	}
}
