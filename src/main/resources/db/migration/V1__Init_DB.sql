create table club (
  id serial primary key,
  description varchar(2048),
  state varchar(10) not null,
  title varchar(50) not null,
  owner_id int8,
  );


create table request (
  id  bigserial not null,
  cover_letter varchar(255),
  status varchar(255) not null,
  club_id int8, user_id int8,
  primary key (id)
  );

create table token (
  id  bigserial not null,
  value varchar(255) not null,
  owner_id int8,
  primary key (id)
  );

create table usr (
  id  bigserial not null,
  activation_code varchar(255),
  email varchar(255) not null,
  first_name varchar(255),
  hash_password varchar(255) not null,
  last_name varchar(255),
  login varchar(255) not null,
  role varchar(255) not null,
  state varchar(255) not null,
  ufid varchar(255),
  primary key (id)
  );

create table usrs_clubs (
  user_id int8 not null,
  club_id int8 not null
  );

alter table usr add constraint unique_email unique (email)
alter table usr add constraint unique_login unique (login)
alter table club add constraint unique_club_title unique (title)
alter table club add constraint fk_owner_id foreign key (owner_id) references usr
alter table request add constraint fk_club_id foreign key (club_id) references club
alter table request add constraint fk_user_id foreign key (user_id) references usr
alter table token add constraint fk_token_owner foreign key (owner_id) references usr
alter table usrs_clubs add constraint fk_clubs_ids foreign key (club_id) references club
alter table usrs_clubs add constraint fk_users_ids foreign key (user_id) references usr