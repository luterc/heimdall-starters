package com.luter.heimdall.starter.jpa.Inspectors;

import cn.hutool.core.collection.CollectionUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.ArrayList;
import java.util.List;

public class JSqlParserTest {

    static final String selectSQL = "select * from user left join role where 1=1 order by user.id limit 0,2";
    static final String deleteSQL = "delete from user where 1=1 and id= 2";
    static final String updateSQL = "update from user set  id = 2";

    static final String[] excludes = new String[]{"user", "role"};

    public static void main(String[] args) {
        try {
            Statement statement = CCJSqlParserUtil.parse(selectSQL);
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableList = tablesNamesFinder.getTableList(statement);
            for (String name : tableList) {
                System.out.println("table: " + name);
            }

            final ArrayList<String> strings = CollectionUtil.toList(excludes);
            final int count = (int) tableList.stream().filter(strings::contains).count();
            System.out.println(count);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }
}
