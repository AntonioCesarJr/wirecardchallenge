delete from `card`;
delete from `buyer`;
delete from `client`;

select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDCLIENT1;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDCLIENT2;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDCLIENT3;
insert into `client` (`public_id`,`created_at`,`updated_at`)
values (@UUIDCLIENT1, now(), now());
insert into `client` (`public_id`,`created_at`,`updated_at`)
values (@UUIDCLIENT2, now(), now());
insert into `client` (`public_id`,`created_at`,`updated_at`)
values (@UUIDCLIENT3, now(), now());


select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDBUYER1;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDBUYER2;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDBUYER3;

select `id` into @ID1 from `client` limit 0,1;
select `id` into @ID2 from `client` limit 1,1;
select `id` into @ID3 from `client` limit 2,1;

insert into `buyer` (`cpf`,`email`,`name`,`client_id`,`public_id`,`created_at`,`updated_at`)
values ('668.854.000-25', 'email1@wirecadr.com', 'Buyer 1', @ID1, @UUIDBUYER1, now(), now());
insert into `buyer` (`cpf`,`email`,`name`,`client_id`,`public_id`,`created_at`,`updated_at`)
values ('619.521.580-52', 'email2@wirecadr.com', 'Buyer 2', @ID2, @UUIDBUYER2, now(), now());
insert into `buyer` (`cpf`,`email`,`name`,`client_id`,`public_id`,`created_at`,`updated_at`)
values ('641.357.770-31', 'email3@wirecadr.com', 'Buyer 3', @ID3, @UUIDBUYER3, now(), now());


select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDCARD1;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDCARD2;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDCARD3;

select `id` into @ID4 from `buyer` limit 0,1;
select `id` into @ID5 from `buyer` limit 1,1;
select `id` into @ID6 from `buyer` limit 2,1;
insert into `card`(`public_id`,`name`,`number`,`expiration_date`,`CVV`,`buyer_id`,`created_at`,`updated_at`)
values (@UUIDCARD1, 'NAME 1','1234432112344321','20/30','123',@ID4, now(), now());
insert into `card`(`public_id`,`name`,`number`,`expiration_date`,`CVV`,`buyer_id`,`created_at`,`updated_at`)
values (@UUIDCARD2, 'NAME 2','1234432112344322','20/30','123',@ID5, now(), now());
insert into `card`(`public_id`,`name`,`number`,`expiration_date`,`CVV`,`buyer_id`,`created_at`,`updated_at`)
values (@UUIDCARD3, 'NAME 3','1234432112344323','20/30','123',@ID6, now(), now());
