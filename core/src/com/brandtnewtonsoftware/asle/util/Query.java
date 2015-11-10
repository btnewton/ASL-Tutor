package com.brandtnewtonsoftware.asle.util;

import java.util.ArrayList;
import java.util.List;

public final class Query {

    private final static int AND_OP = 0;
    private final static int OR_OP = 1;
    public final static int DEFAULT_RESULT_LIMIT = 500;

    private int limit;
    private StringBuilder whereClause = new StringBuilder();
    private String orderBy;
    private List<String> whereArgs = new ArrayList<>();

    public Query() {
        limit = DEFAULT_RESULT_LIMIT;
    }

    public void addAndWhereClause(String whereClause) {
        addAndWhereClause(whereClause, null);
    }
    public void addAndWhereClause(String whereClause, String whereArg) {
        addWhereClause(AND_OP, whereClause, whereArg);
    }

    public void addOrWhereClause(String whereClause) {
        addOrWhereClause(whereClause, null);
    }
    public void addOrWhereClause(String whereClause, String whereArg) {
        addWhereClause(OR_OP, whereClause, whereArg);
    }

    private void addWhereClause(int operator, String whereClause, String whereArg) {
        if (this.whereClause.length() == 0) {
            this.whereClause.append(whereClause);
        } else {
            this.whereClause.append(getOperator(operator) + whereClause);
        }

        if (whereArg != null) {
            whereArgs.add(whereArg);
        }
    }

    public void overrideDefaultResultLimit(int limit) {
        this.limit = limit;
    }

    public static String wildCardWrapper(String value) {
        return "%" + value + "%";
    }

    public String getWhereClause() {
        return  whereClause.toString();
    }

    public String[] getWhereArgs() {
        String[] whereArgs = new String[this.whereArgs.size()];
        for (int i = 0; i < whereArgs.length; i++) {
            whereArgs[i] = this.whereArgs.get(i);
        }
        return whereArgs;
    }

    public void setOrderBy(String orderBy, boolean orderAsc) {
        if (orderBy == null)
            this.orderBy = null;
        else
            this.orderBy = orderBy + ((orderAsc)? " ASC " : " DESC ");
    }

    public String getOrderBy() {
        return orderBy;
    }

    public int getLimit() {
        return limit;
    }

    private String getOperator(int operatorCode) {
        if (operatorCode == AND_OP) {
            return " AND ";
        } else {
            return " OR ";
        }
    }
}
