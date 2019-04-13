-- DROP DATABASE  IF EXISTS impuestos;

DROP TABLE IF EXISTS prueba;

CREATE TABLE prueba(
 id serial PRIMARY KEY,
 depname VARCHAR (50) NOT NULL,
 salary int
);

INSERT INTO prueba (id, depname, salary) values (1,'a',100);
INSERT INTO prueba (id, depname, salary) values (2,'b',90);
INSERT INTO prueba (id, depname, salary) values (3,'a',80);
INSERT INTO prueba (id, depname, salary) values (4,'c',70);
INSERT INTO prueba (id, depname, salary) values (5,'b',60);
INSERT INTO prueba (id, depname, salary) values (6,'d',50);
INSERT INTO prueba (id, depname, salary) values (7,'a',40);
INSERT INTO prueba (id, depname, salary) values (8,'b',30);
INSERT INTO prueba (id, depname, salary) values (9,'d',20);

select * from (SELECT depname, id, salary, max(salary) OVER (PARTITION BY depname) as max FROM prueba) as a where a.salary=a.max;