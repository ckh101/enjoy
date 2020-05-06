package com.hbnet.fastsh.web.service.page;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @ClassName: SearchFilter
 * @Auther: zoulr@qq.com
 * @Date: 2019/6/3 11:17
 * @Description: 数据库查询条件封装类
 */
public class SearchFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFilter.class);
    private static final String parsePattern = "yyyy-MM-dd HH:mm:ss.SSS";

    static {
        ConvertUtils.register(new DateLocaleConverter(), Date.class); // 使ConvertUtils支持java.util.Date的转换
    }

    public enum Operator {
        EQ("="), LIKE("like"), GT(" >"), LT(" <"), GTE(">="), LTE(" <="), NEQ("<>"), IN("in");
        // OREQ, ORLIKE, ANDLIKE 后面必须带的是String

        private String value;

        private Operator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    /** 属性数据类型. */
    public enum PropertyType {
        S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class), E(
                Enum.class), M(BigDecimal.class);

        private Class<?> clazz;

        PropertyType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getValue() {
            return clazz;
        }

    }

    public String fieldName;
    public Object value;
    public Operator operator;
    private String operatorName;
    // 表字段实体
    private String columnName;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.operatorName = operator.getValue();
    }

    public SearchFilter(String fieldName, String columnName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.operatorName = operator.getValue();
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return "SearchFilter [fieldName=" + fieldName + ", value=" + value + ", operator=" + operator
                + ", operatorName=" + operatorName + ", columnName=" + columnName + "]";
    }

    public String searchStr() {
        String sql = MessageFormat.format(operator.getValue(), fieldName);
        sql = (" and ").concat(sql).concat(" ? ");
        return sql;
    }

    /**
     * searchParams中key的格式为LIKES_name
     */
    public static List<SearchFilter> parse(Map<String, String> searchParams) {
        List<SearchFilter> filters = new ArrayList<>();

        for (Entry<String, String> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();

            Object value = entry.getValue();
            if (StringUtils.isBlank((String) value)) {
                continue;
            }
            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "_");
            if (names.length != 2 && names.length != 3) {
                // throw new IllegalArgumentException(key +
                // " is not a valid search filter name");
                LOGGER.warn(key + " is not a valid search filter name");
                continue;
            }

            Class<?> propertyClass = null;
            if (names.length == 3) {
                try {
                    propertyClass = Enum.valueOf(PropertyType.class, names[2]).getValue();
                    LOGGER.debug(key + ":" + propertyClass.getName());
                    if (propertyClass != null) {
                        if (propertyClass.getName().equals("java.util.Date")) {
                            String str = value.toString();
                            if (str.length() == 10) {
                                if (names[0].equals("GT") || names[0].equals("GTE")) {
                                    str += " 00:00:00.000";
                                } else if (names[0].equals("LT") || names[0].equals("LTE")) {
                                    str += " 23:59:59.999";
                                }
                            }

                            if (str.length() == 19) {
                                if (names[0].equals("GT") || names[0].equals("GTE")) {
                                    str += ".000";
                                } else if (names[0].equals("LT") || names[0].equals("LTE")) {
                                    str += ".999";
                                }
                            }

                            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(parsePattern);
                            value = dateTimeFormat.parseObject(str);
                        } else {
                            value = ConvertUtils.convert(value, propertyClass);
                        }

                    }

                } catch (RuntimeException e) {
                    LOGGER.warn(key + " PropertyType is not a valid type!", e);
                } catch (ParseException e) {
                    LOGGER.warn(key + " PropertyType is not a valid type!", e);
                }
            }
            String filedName = names[1];
            Operator operator = Operator.valueOf(names[0]);
            switch (operator) {
                case LIKE:
                    value = "%".concat(unescape(value.toString())).concat("%");
                    break;
                case IN:
                    value = "(".concat(value.toString()).concat(")");
                    filedName = filedName.replace("[]", "");
                    break;
                default:
                    break;
            }
            // 创建searchFilter
            SearchFilter filter = new SearchFilter(filedName, operator, value);
            filters.add(filter);
        }

        return filters;
    }

    private static String unescape(String x) {
        int stringLength = x.length();
        StringBuilder buf = new StringBuilder((int) (x.length() * 1.1));
        CharsetEncoder charsetEncoder = Charset.forName("UTF-8").newEncoder();
        //
        // Note: buf.append(char) is _faster_ than appending in blocks, because the block append requires a System.arraycopy().... go figure...
        //

        for (int i = 0; i < stringLength; ++i) {
            char c = x.charAt(i);

            switch (c) {
                case 0: /* Must be escaped for 'mysql' */
                    buf.append('\\');
                    buf.append('0');

                    break;

                case '\n': /* Must be escaped for logs */
                    buf.append('\\');
                    buf.append('n');

                    break;

                case '\r':
                    buf.append('\\');
                    buf.append('r');

                    break;

                case '\\':
                    buf.append('\\');
                    buf.append('\\');

                    break;

                case '\'':
                    buf.append('\\');
                    buf.append('\'');

                    break;

                case '"': /* Better safe than sorry */
                    buf.append('\\');
                    buf.append('"');
                    break;

                case '%':
                    buf.append('\\');
                    buf.append('%');
                    break;

                case '_':
                    //buf.append('\\');
                    buf.append('_');
                    break;

                case '\032': /* This gives problems on Win32 */
                    buf.append('\\');
                    buf.append('Z');

                    break;

                case '\u00a5':
                case '\u20a9':
                    // escape characters interpreted as backslash by mysql
                    CharBuffer cbuf = CharBuffer.allocate(1);
                    ByteBuffer bbuf = ByteBuffer.allocate(1);
                    cbuf.put(c);
                    cbuf.position(0);
                    charsetEncoder.encode(cbuf, bbuf, true);
                    if (bbuf.get(0) == '\\') {
                        buf.append('\\');
                    }

                    buf.append(c);
                    break;

                default:
                    buf.append(c);
            }
        }

        return buf.toString();
    }
}