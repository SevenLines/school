-- Пароль admin
INSERT INTO schoolchild (id, name, password, email, father_name, phone_number, educational_class, surname)
VALUES (10000, 'Админ', '$2a$12$wfriCDniBA.kZZKp9jZjDuvx5R1dlPuJEeCwXWd33eVJHl2GKpiJu', 'admin@a.ru', null, null,
        null, null);

INSERT INTO role
VALUES (1, 'USER');
INSERT INTO role
VALUES (2, 'ADMIN');

INSERT INTO schoolchild_roles
VALUES (10000, 2);

-- у всех пароль - "1"
insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (10002, 'Алексей', 'Алексеевич', 'Алексеев', 'Лицей №1', '8А', 'alex@mail.ru', '888888',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');

insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (10003, 'Андрей', 'Адреевич', 'Андреев', 'Лицей РЖД', '8Б', 'andru@mail.ru', '999999',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');

insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (10004, 'Дмитрий', 'Дмитриевич', 'Дмитров', 'Лицей №3', '10А', 'dmitriy@mail.ru', '777777',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');

insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (10005, 'Антон', 'Антонович', 'Антонов', 'Лицей ИГУ', '9Е', 'anton@mail.ru', '666666',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');

insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (10006, 'Михаил', 'Михайлович', 'Михайлов', 'Лицей 2', '11Г', 'mihailo@mail.ru', '555555',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');


INSERT INTO schoolchild_roles
VALUES (10002, 1),
       (10003, 1),
       (10004, 1),
       (10005, 1),
       (10006, 1);