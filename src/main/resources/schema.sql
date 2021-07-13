CREATE DATABASE simbirsoft;

CREATE TYPE task_status AS ENUM
  ('BACKLOG', 'IN_PROGRESS', 'DONE');

CREATE TABLE users
(
  id bigint NOT NULL,
  name character varying NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE customers
(
  id bigint NOT NULL,
  name character varying NOT NULL,
  CONSTRAINT customers_pkey PRIMARY KEY (id)
);

CREATE TABLE projects
(
  id bigint NOT NULL,
  name character varying NOT NULL,
  description character varying,
  customer_id bigint NOT NULL,
  CONSTRAINT projects_pkey PRIMARY KEY (id),
  CONSTRAINT projects_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE TABLE releases
(
  id bigint NOT NULL,
  name character varying NOT NULL,
  date_start date NOT NULL,
  date_stop date NOT NULL,
  CONSTRAINT releases_pkey PRIMARY KEY (id)
);

CREATE TABLE borders
(
  id bigint NOT NULL,
  name character varying NOT NULL,
  project_id bigint NOT NULL,
  CONSTRAINT borders_pkey PRIMARY KEY (id),
  CONSTRAINT borders_project_id_key UNIQUE (project_id),
  CONSTRAINT borders_project_id_fkey FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE tasks
(
  id bigint NOT NULL,
  name character varying NOT NULL,
  description character varying,
  creator_id bigint NOT NULL,
  executor_id bigint NOT NULL,
  release_id bigint NOT NULL,
  border_id bigint NOT NULL,
  status task_status NOT NULL,
  CONSTRAINT tasks_pkey PRIMARY KEY (id),
  CONSTRAINT tasks_border_id_fkey FOREIGN KEY (border_id) REFERENCES borders (id),
  CONSTRAINT tasks_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES users (id),
  CONSTRAINT tasks_executor_id_fkey FOREIGN KEY (executor_id) REFERENCES users (id),
  CONSTRAINT tasks_release_id_fkey FOREIGN KEY (release_id) REFERENCES releases (id)
);