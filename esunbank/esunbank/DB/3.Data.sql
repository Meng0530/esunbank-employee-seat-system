USE [EmployeeSeatDB]
GO

INSERT INTO SEATING_CHART(FLOOR_SEAT_SEQ, FLOOR_NO, SEAT_NO) VALUES
(1,1,1),(2,1,2),(3,1,3),(4,1,4),
(5,2,1),(6,2,2),(7,2,3),(8,2,4),
(9,3,1),(10,3,2),(11,3,3),(12,3,4),
(13,4,1),(14,4,2),(15,4,3),(16,4,4);

INSERT INTO EMPLOYEE(EMP_ID, NAME, EMAIL, FLOOR_SEAT_SEQ) VALUES
('12006','王明明','ming@example.com',3),
('16142','陳華華','hua@example.com',7),
('13040','林美美','mei@example.com',9),
('17081','張文文','wen@example.com',10),
('11221','李安安','an@example.com',12),
('16722','趙強強','qiang@example.com',15);
