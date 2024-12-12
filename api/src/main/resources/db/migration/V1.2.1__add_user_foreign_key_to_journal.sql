use simple_journal_entry_db;

ALTER TABLE journals
ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE journals
ADD FOREIGN KEY fk_user(user_id) REFERENCES users(id);
