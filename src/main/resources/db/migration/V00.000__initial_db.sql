create table shout (
    id integer primary key autoincrement,
    shout varchar(512) NOT NULL,
    channel varchar(128) NOT NULL,
    nick varchar(128) NOT NULL,
    is_action boolean NOT NULL,
    recorded timestamp NOT NULL
);