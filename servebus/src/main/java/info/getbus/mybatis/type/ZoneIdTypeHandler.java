package info.getbus.mybatis.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

public class ZoneIdTypeHandler extends BaseTypeHandler<ZoneId> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ZoneId parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getId());
    }

    @Override
    public ZoneId getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ZoneId.of(rs.getString(columnName));
    }

    @Override
    public ZoneId getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ZoneId.of(rs.getString(columnIndex));
    }

    @Override
    public ZoneId getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ZoneId.of(cs.getString(columnIndex));
    }
}
