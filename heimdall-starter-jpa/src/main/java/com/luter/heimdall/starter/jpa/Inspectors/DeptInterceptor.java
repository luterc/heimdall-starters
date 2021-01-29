/*
 *
 *  *
 *  *
 *  *      Copyright 2020-2021 Luter.me
 *  *
 *  *      Licensed under the Apache License, Version 2.0 (the "License");
 *  *      you may not use this file except in compliance with the License.
 *  *      You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *      Unless required by applicable law or agreed to in writing, software
 *  *      distributed under the License is distributed on an "AS IS" BASIS,
 *  *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *      See the License for the specific language governing permissions and
 *  *      limitations under the License.
 *  *
 *  *
 *
 */

package com.luter.heimdall.starter.jpa.Inspectors;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Accessors(chain = true)
@SuppressWarnings("ALL")
public class DeptInterceptor implements StatementInspector {


    private Long orgId;

    private List<Long> orgIds;

    private List<Long> orgTables;

    private String orgIdColumn = "org_id";


    @Override
    public String inspect(String sql) {
        try {
//            /**
//             * 未登录用户，系统用户不做解析
//             */
//            CurrentUser current = UserContext.current();
//            if (UserContext.current() == null || UserContext.current().getAdministrator()) {
//                return null;
//            }
//            /**
//             * 初始化需要进行解析的组织表,
//             */
//            if (orgTables == null) {
//                synchronized (OrganizationInterceptor.class) {
//                    OrganizationProperties bean = SpringContextUtil.getBean(OrganizationProperties.class);
//                    if (bean != null) {
//                        orgTables = bean.getTables();
//                    } else {
//                        throw new RuntimeException("未能获取TenantProperties参数配置");
//                    }
//                }
//            }
//
//            /**
//             * 从当前线程获取登录用户的所属用户组织ID及其子孙组织ID
//             */
//            CurrentUser user = UserContext.current();
//            orgId = user.getOrganizationId();
//            orgIds = user.getOrganizationIds();
            log.info("组织筛选解析开始，原始SQL：{}", sql);
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            StringBuilder sqlStringBuilder = new StringBuilder();
            int i = 0;
            for (Statement statement : statements.getStatements()) {
                if (null != statement) {
                    if (i++ > 0) {
                        sqlStringBuilder.append(';');
                    }
                    sqlStringBuilder.append(this.processParser(statement));
                }
            }
            String newSql = sqlStringBuilder.toString();
            log.info("组织筛选解析结束，解析后SQL：{}", newSql);
            return newSql;
        } catch (Exception e) {
            log.error("组织筛选解析失败，解析SQL异常{}", e.getMessage());
            e.printStackTrace();
        } finally {
            orgId = null;
        }
        return null;
    }

    private String processParser(Statement statement) {
        if (statement instanceof Insert) {
            this.processInsert((Insert) statement);
        } else if (statement instanceof Select) {
            this.processSelectBody(((Select) statement).getSelectBody());
        } else if (statement instanceof Update) {
            this.processUpdate((Update) statement);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete) statement);
        }
        /**
         * 返回处理后的SQL
         */
        return statement.toString();
    }

    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                operationList.getSelects().forEach(this::processSelectBody);
            }
        }
    }

    public void processInsert(Insert insert) {
        if (orgTables.contains(insert.getTable().getFullyQualifiedName())) {
            insert.getColumns().add(new Column(orgIdColumn));
            if (insert.getSelect() != null) {
                processPlainSelect((PlainSelect) insert.getSelect().getSelectBody(), true);
            } else if (insert.getItemsList() != null) {
                // fixed github pull/295
                ItemsList itemsList = insert.getItemsList();
                if (itemsList instanceof MultiExpressionList) {
                    ((MultiExpressionList) itemsList).getExprList().forEach(el -> el.getExpressions().add(new LongValue(orgId)));
                } else {
                    ((ExpressionList) insert.getItemsList()).getExpressions().add(new LongValue(orgId));
                }
            } else {
                throw new RuntimeException("Failed to process multiple-table update, please exclude the tableName or statementId");
            }
        }
    }

    public void processUpdate(Update update) {
        final Table table = update.getTable();
        if (orgTables.contains(table.getFullyQualifiedName())) {
            update.setWhere(this.andExpression(table, update.getWhere()));
        }
    }

    public void processDelete(Delete delete) {
        if (orgTables.contains(delete.getTable().getFullyQualifiedName())) {
            delete.setWhere(this.andExpression(delete.getTable(), delete.getWhere()));
        }
    }

    protected BinaryExpression andExpression(Table table, Expression where) {
        //获得where条件表达式
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(this.getAliasColumn(table));
        equalsTo.setRightExpression(new LongValue(orgId));
        if (null != where) {
            if (where instanceof OrExpression) {
                return new AndExpression(equalsTo, new Parenthesis(where));
            } else {
                return new AndExpression(equalsTo, where);
            }
        }
        return equalsTo;
    }

    protected void processPlainSelect(PlainSelect plainSelect) {
        if (plainSelect.getWhere() != null) {
            processPlainSelect(plainSelect, true);
        } else {
            processPlainSelect(plainSelect, false);
        }

    }

    protected void processPlainSelect(PlainSelect plainSelect, boolean addColumn) {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            if (orgTables.contains(fromTable.getFullyQualifiedName())) {
                //#1186 github
                plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable));
                if (addColumn) {
                    plainSelect.getSelectItems().add(new SelectExpressionItem(new Column(orgIdColumn)));
                }
            }
        } else {
            processFromItem(fromItem);
        }
        List<Join> joins = plainSelect.getJoins();
        if (joins != null && joins.size() > 0) {
            joins.forEach(j -> {
                processJoin(j);
                processFromItem(j.getRightItem());
            });
        }
    }

    protected void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoinList() != null) {
                subJoin.getJoinList().forEach(this::processJoin);
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            log.debug("Perform a subquery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    protected void processJoin(Join join) {
        if (join.getRightItem() instanceof Table) {
            Table fromTable = (Table) join.getRightItem();
            if (orgTables.contains(fromTable.getFullyQualifiedName())) {
                join.setOnExpression(builderExpression(join.getOnExpression(), fromTable));
            }
        }
    }

    protected Expression builderExpression(Expression currentExpression, Table table) {
        final InExpression organizationExpression = new InExpression();
        List<Expression> expressions = new ArrayList<>();
        orgIds.forEach(organizatinId -> {
            expressions.add(new LongValue(organizatinId));
        });
        ExpressionList expressionList = new ExpressionList(expressions);
        organizationExpression.setLeftExpression(this.getAliasColumn(table));
        organizationExpression.setRightItemsList(expressionList);

        Expression appendExpression = null;
        if (!(organizationExpression instanceof SupportsOldOracleJoinSyntax)) {
            appendExpression = new EqualsTo();
            ((EqualsTo) appendExpression).setLeftExpression(this.getAliasColumn(table));
            ((EqualsTo) appendExpression).setRightExpression(organizationExpression);
        }
        if (currentExpression == null) {
            return organizationExpression;
        } else {
            appendExpression = organizationExpression;
        }
        if (currentExpression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
            doExpression(binaryExpression.getLeftExpression());
            doExpression(binaryExpression.getRightExpression());
        } else if (currentExpression instanceof InExpression) {
            InExpression inExp = (InExpression) currentExpression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new Parenthesis(currentExpression), appendExpression);
        } else {
            return new AndExpression(currentExpression, appendExpression);
        }
    }

    protected void doExpression(Expression expression) {
        if (expression instanceof FromItem) {
            processFromItem((FromItem) expression);
        } else if (expression instanceof InExpression) {
            InExpression inExp = (InExpression) expression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
    }


    protected Column getAliasColumn(Table table) {
        StringBuilder column = new StringBuilder();
        if (null == table.getAlias()) {
            column.append(table.getName());
        } else {
            column.append(table.getAlias().getName());
        }
        column.append(".");
        column.append(orgIdColumn);
        return new Column(column.toString());
    }

}
