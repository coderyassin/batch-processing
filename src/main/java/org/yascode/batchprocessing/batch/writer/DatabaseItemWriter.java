package org.yascode.batchprocessing.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yascode.batchprocessing.model.User;

@Component
@RequiredArgsConstructor
public class DatabaseItemWriter implements ItemWriter<User> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void write(Chunk<? extends User> chunk) throws Exception {
        for (User user:chunk) {
            String sql = "insert into user (last_name, first_name, age, country) values (?, ?, ?, ?)";
            jdbcTemplate.update(sql, user.getLastName(), user.getFirstName(), user.getAge(), user.getCountry());
        }
    }
}
