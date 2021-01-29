/*
 *
 *  *
 *  *  *    Copyright 2020-2021 Luter.me
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *
 */

package com.luter.heimdall.starter.jpa.Inspectors;

import com.luter.heimdall.starter.jpa.config.JpaProperties;
import com.luter.heimdall.starter.utils.SpringUtils;
import com.luter.heimdall.starter.utils.exception.LuterException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.List;

@Slf4j
public class ShowCaseInspector implements StatementInspector {
    @Override
    public String inspect(String sql) {
        log.debug("sql inspect . \nsql:{}", sql);
        JpaProperties config = SpringUtils.getBean(JpaProperties.class);
        List<String> excludeTables = config.getExcludeTables();
        if (config.getEnabled()) {
            log.warn("当前处于演示模式，只允许进行查询操作,如需要退出演示模式，可在配置参数中设置：luter.showcase.enabled=false进行关闭");
            SqlInfo info = getSqlInfo(sql);
            if (null != info) {
                if (info.isDelete() || info.isUpdate()) {
                    log.warn("拒绝语句:{}", sql);
                    throw new LuterException("演示环境不允许修改数据");
                }
                //允许这些表插入数据
                if (info.isInsert() && !isExcludeTable(info, excludeTables)) {
                    log.warn("拒绝语句:{}", sql);
                    throw new LuterException("演示环境不允许修改数据");
                }
            }
        }
        return sql;
    }


    private boolean isExcludeTable(SqlInfo info, List<String> excludeTables) {
        if (excludeTables.isEmpty()) {
            return false;
        }
        List<String> tableList = info.getTables();
        final int count = (int) tableList.stream().filter(excludeTables::contains).count();
        return count > 0;
    }

    private SqlInfo getSqlInfo(String sql) {
        SqlInfo sqlInfo = null;
        try {
            sqlInfo = new SqlInfo();
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Select) {
                sqlInfo.setCommandType("Select");
            }
            if (statement instanceof Delete) {
                sqlInfo.setCommandType("Delete");
            }
            if (statement instanceof Update) {
                sqlInfo.setCommandType("Update");
            }
            if (statement instanceof Insert) {
                sqlInfo.setCommandType("Insert");
            }
            sqlInfo.setTables(new TablesNamesFinder().getTableList(statement));
        } catch (JSQLParserException ignored) {

        }
        return sqlInfo;
    }

    @Data
    static
    class SqlInfo {
        List<String> tables;
        String commandType;

        public boolean isSelect() {
            return "Select".equals(this.getCommandType());
        }

        public boolean isDelete() {
            return "Delete".equals(this.getCommandType());
        }

        public boolean isUpdate() {
            return "Update".equals(this.getCommandType());
        }

        public boolean isInsert() {
            return "Insert".equals(this.getCommandType());
        }
    }
}
