delete from message;

insert into message (id, text, tag, user_id) values
(1, 'my-text_1', 'my-tag_1', 1),
(2, 'my-text_2', 'my-tag_2', 1),
(3, 'my-text_3', 'my-tag_1', 1),
(4, 'my-text_4', 'my-tag_4', 1);

insert into hibernate_sequence (next_val) value (10);