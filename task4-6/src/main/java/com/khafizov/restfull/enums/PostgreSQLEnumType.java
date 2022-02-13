package com.khafizov.restfull.enums;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PostgreSQLEnumType extends org.hibernate.type.EnumType<CatColor> {

    public void nullSafeSet(
            PreparedStatement st,
            Object value,
            int index,
            SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if(value == null) {
            st.setNull( index, Types.OTHER );
        }
        else {
            st.setObject(
                    index,
                    ((CatColor) value).getName(),
                    Types.OTHER
            );
        }
    }

    public Object nullSafeGet(
            ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        String label = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        for (Object value : returnedClass().getEnumConstants()) {
            if (value instanceof CatColor) {
                CatColor catColor = (CatColor) value;
                if (catColor.getName().equals(label)) {
                    return value;
                }
            }
        }
        throw new IllegalStateException("Unknown " + returnedClass().getSimpleName() + " label");
    }
}
