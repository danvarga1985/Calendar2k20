package com.calendar.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"heroku"})
public class PostgreSQLQuery extends SQLQuery {

    public PostgreSQLQuery() {
        super.setQuery("WITH RECURSIVE entries AS(\n" +
                "    SELECT\n" +
                "        id,\n" +
                "        entry_id\n" +
                "    FROM\n" +
                "        entry\n" +
                "    WHERE\n" +
                "        id = (?)\n" +
                "    UNION ALL\n" +
                "        SELECT\n" +
                "            e.id,\n" +
                "            e.entry_id\n" +
                "        FROM\n" +
                "             entry e\n" +
                "        INNER JOIN entries entr ON entr.entry_id = e.id\n" +
                ")\n" +
                "SELECT id FROM entries WHERE entry_id is null;");
    }
}
