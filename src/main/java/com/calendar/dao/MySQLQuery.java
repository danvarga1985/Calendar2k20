package com.calendar.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"mysql", "default"})
public class MySQLQuery extends SQLQuery {

    public MySQLQuery() {
        super.setQuery("WITH RECURSIVE Entry_path(id, parent_id) AS(\n" +
                "    SELECT id,parent_id\n" +
                "    FROM entry\n" +
                "    WHERE id = (?)\n" +
                "    UNION ALL\n" +
                "    SELECT e.id, e.parent_id\n" +
                "    FROM Entry_path AS cp JOIN entry AS e\n" +
                "                                  ON cp.parent_id = e.id\n" +
                ")\n" +
                "SELECT id FROM Entry_path WHERE parent_id is null;");
    }
}
