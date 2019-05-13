delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '202cb962ac59075b964b07152d234b70', 'admin'),
(2, true, '{Jitoh+wui04BgQkvrGb08+U9nBOoux7fKmmlTZJLOz0=}d1df9fd45d3c5524de93d1edd8b66a4e', '1234');

insert into user_role(user_id, roles) values
(1, 'USER'), (1,'ADMIN'),
(2, 'USER');