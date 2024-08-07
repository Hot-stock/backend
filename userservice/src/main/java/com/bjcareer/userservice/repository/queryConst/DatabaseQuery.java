package com.bjcareer.userservice.repository.queryConst;

public class DatabaseQuery {
    public static final String finedUsertQuery = "select u from User u where u.alais = :alais";
    public static final String finedRoleQuery = "select u from Role r where r.type = :type";
}
