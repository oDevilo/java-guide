package com.devil.guide.orm.constant;

/**
 * @author Devil
 * @date 2022/4/10
 */
public interface SqlConstants {

    String BLANK = "";
    String SPACE = " ";
    String ASTERISK = "*";

    String PRECOMPILE_MARK = "?";

    String LEFT_BRACKET = "(";
    String RIGHT_BRACKET = ")";
    String AND = "AND";
    String OR = "OR";

    String COMMA = ",";
    // 不使用 Mybatis Plus 的 ISqlSegment 方式了 避免创建过多对象
    String IN = "IN";
    String NOT ="NOT";
    String LIKE ="LIKE";
    String EQ ="=";
    String NE ="<>";
    String GT =">";
    String GE =">=";
    String LT ="<";
    String LE ="<=";
    String IS_NULL ="IS NULL";
    String IS_NOT_NULL ="IS NOT NULL";
    String GROUP_BY ="GROUP BY";
    String HAVING ="HAVING";
    String ORDER_BY ="ORDER BY";
    String LIMIT ="LIMIT";
    String EXISTS ="EXISTS";
    String BETWEEN ="BETWEEN";
    String ASC ="ASC";
    String DESC ="DESC";

}
