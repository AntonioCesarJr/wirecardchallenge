delete from `payment`;
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

select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT1;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT2;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT3;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT4;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT5;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT6;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT7;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT8;
select (UNHEX(REPLACE(UUID(), '-', ''))) into @UUIDPAYMENT9;
select `id` into @ID7 from `card` limit 0,1;
select `id` into @ID8 from `card` limit 1,1;
insert into `payment`(`public_id`, `amount`, `payment_status`, `type`, `card_id`, `buyer_id`, `created_at`, `updated_at`)
values (@UUIDPAYMENT1, 100.34, 'Pending', 'Credit_Card',@ID7, @ID1, now(), now());
insert into `payment`(`public_id`, `amount`, `payment_status`, `type`, `card_id`, `buyer_id`, `created_at`, `updated_at`)
values (@UUIDPAYMENT2, 2345.87, 'Success', 'Credit_Card',@ID7, @ID1, now(), now());
insert into `payment`(`public_id`, `amount`, `payment_status`, `type`, `card_id`, `buyer_id`, `created_at`, `updated_at`)
values (@UUIDPAYMENT3, 87.45, 'Canceled', 'Credit_Card',@ID7, @ID1, now(), now());
insert into `payment`(`public_id`, `amount`, `payment_status`, `type`, `card_id`, `buyer_id`, `created_at`, `updated_at`)
values (@UUIDPAYMENT4, 584.78, 'Rejected', 'Credit_Card',@ID8, @ID2, now(), now());
insert into `payment`(`public_id`, `amount`, `payment_status`, `type`, `card_id`, `buyer_id`, `created_at`, `updated_at`)
values (@UUIDPAYMENT5, 487.21, 'Success', 'Credit_Card',@ID8, @ID2, now(), now());
insert into `payment`(`public_id`, `amount`, `payment_status`, `type`, `card_id`, `buyer_id`, `created_at`, `updated_at`)
values (@UUIDPAYMENT6, 1257.54, 'Success', 'Credit_Card',@ID8, @ID2, now(), now());
insert into `payment`(`public_id`, `amount`, `payment_status`, `type`, `Bank_Slip_number`, `created_at`, `updated_at`)
values (@UUIDPAYMENT7, 877.44, 'Canceled', 'Bank_Slip', 'd8c0ed27-da75-409a-964e-a36df85e48f6', now(), now());
insert into `payment`(`public_id`, `amount`, `payment_status`, `type`, `Bank_Slip_number`, `created_at`, `updated_at`)
values (@UUIDPAYMENT8, 6114.44, 'Complete', 'Bank_Slip', '1210dba7-983d-4e3a-9d7c-4ede66e14333', now(), now());
