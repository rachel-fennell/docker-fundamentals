-- task-api/src/main/resources/db/migration/V1__create_tasks.sql
create table tasks (
  id           bigint generated always as identity primary key,
  title        varchar(200) not null,
  status       varchar(20)  not null default 'OPEN',
  due_date     date,
  completed_at timestamptz
);
insert into tasks (title, due_date) values
  ('Write the README', current_date + 3),
  ('Containerise the API', current_date + 7);