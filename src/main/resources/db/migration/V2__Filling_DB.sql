insert into usr (email, login, hash_password, state, role)
  values ('cross-@mail.ru', 'framingham', '$2a$10$LZBsGKON3nwNyJ9Kq8QPxOwXYvgwjCOfERwZzUHIykbW5xb3rb2tu', 'ACTIVE', 'USER'),
  ('randall.jamiie@gmail.com', 'jamie', '$2a$10$zMTH4fIuVstrhm7/GcvB2.oGgoDlpPXb7xf0tMdcVngxFcU4JPkSK', 'ACTIVE', 'USER');

insert into club (description, state, title, owner_id)
  values ('We were born to be kings', 'ACTIVE', 'Kings', 1),
  ('I''ve set up my house at bookshelf and i love it', 'ACTIVE', 'Book worms', 1);

insert into token (value, owner_id)
  values ('mQTXYMHFL1', 1);

insert into usrs_clubs(user_id, club_id)
  values (1, 1), (1, 2), (2, 1);

