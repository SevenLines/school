insert into trajectory(description, name)
values ('Это специалист по проектированию и конструированию механических конструкций, систем и агрегатов летательных аппаратов (ЛА)',
        'авиаконструктор');
insert into trajectory(description, name)
values ('Это специалист по обслуживанию и ремонту любой электротехники', 'электромеханик');
insert into trajectory(description, name)
values ('Это литературный работник, который занимается сбором, созданием, редактированием, подготовкой и оформлением информации для редакции средства массовой информации',
        'журналист');
insert into trajectory(description, name)
values ('Это учёный или специалист, получивший образование и специализирующийся на изучении химии как науки, а также обладающий навыками работы с химикатами',
        'химик');

insert into study_activity(name, description, start_date, end_date, participation_point, max_participants,
                           activity_type)
values ('Научный прорыв', 'Региональный конкурс «Научный прорыв» для обучающихся образовательных организаций 15-18 лет',
        '01-10-2022', '10-10-2022', 10, 100, 'мероприятие');
insert into study_activity(name, description, start_date, end_date, participation_point, max_participants,
                           activity_type)
values ('Лаборатория творчества',
        'Образовательный центр «Персей» в рамках Федерального проекта «Успех каждого ребенка» проводит профильную смену «Лаборатория творчества: театральный класс» для обучающихся 4-8-х классов',
        '21-10-2022', '02-11-2022', 10, 1000, 'программа');
insert into study_activity(name, description, start_date, end_date, participation_point, max_participants,
                           activity_type)
values ('Педагогические каникулы', 'Профориентационное образовательное событие «Педагогические каникулы',
        '07-10-2022', '07-11-2022', 10, 500, 'мероприятие');


-- Пароль admin
INSERT INTO schoolchild (id, name, password, email, father_name, phone_number, educational_class, surname)
VALUES (1, 'Админ', '$2a$12$wfriCDniBA.kZZKp9jZjDuvx5R1dlPuJEeCwXWd33eVJHl2GKpiJu', 'admin@a.ru', null, null,
        null, null);

INSERT INTO role
VALUES (1, 'USER');
INSERT INTO role
VALUES (2, 'ADMIN');

-- у всех пароль - "1"
insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (2, 'Алексей', 'Алексеевич', 'Алексеев', 'Лицей №1', '8А', 'alex@mail.ru', '888888',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');

insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (3, 'Андрей', 'Адреевич', 'Андреев', 'Лицей РЖД', '8Б', 'andru@mail.ru', '999999',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');

insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (4, 'Дмитрий', 'Дмитриевич', 'Дмитров', 'Лицей №3', '10А', 'dmitriy@mail.ru', '777777',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');

insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (5, 'Антон', 'Антонович', 'Антонов', 'Лицей ИГУ', '9Е', 'anton@mail.ru', '666666',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');

insert into schoolchild(id, name, surname, father_name, educational_organization, educational_class, email,
                        phone_number, password)
values (6, 'Михаил', 'Михайлович', 'Михайлов', 'Лицей 2', '11Г', 'mihailo@mail.ru', '555555',
        '$2a$12$o3ta1q2RrDSrdxmb2U7NJunK9mZ1tMsUciTOB9K3xy.Ev73QpJiJG');


INSERT INTO schoolchild_roles
VALUES (1, 2),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1);