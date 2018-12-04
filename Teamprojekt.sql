--CREATE ALL PRIMARY TABLES----------------------------------------------------

CREATE TABLE Thema
(
    Titel_Thema VARCHAR(50) NOT NULL,
    PRIMARY KEY (Titel_Thema)
);

--DROP TABLE Thema;

CREATE TABLE Formulare
(
    Titel_Formulare VARCHAR(50) NOT NULL,
    PRIMARY KEY (Titel_Formulare),
    Datei BLOB NOT NULL
);

--DROP TABLE Formulare;

CREATE TABLE Anliegen
(
    ID_Anliegen INT NOT NULL,
    PRIMARY KEY (ID_Anliegen),
    Titel_Thema VARCHAR(50) NOT NULL,
    Titel_Anliegen VARCHAR(50) NOT NULL,
    Notizen VARCHAR(300)
);

--DROP TABLE Anliegen;

CREATE TABLE Dokument
(
    Titel_Dokument VARCHAR(50) NOT NULL,
    PRIMARY KEY (Titel_Dokument),
    Datei BLOB NOT NULL
);

--DROP TABLE Dokument;

CREATE TABLE Termine
(
    ID_Termine INT NOT NULL,
    ID_Anliegen INT,
    PRIMARY KEY (ID_Termine),
    FOREIGN KEY (ID_Anliegen) REFERENCES Anliegen(ID_Anliegen),
    Start_Datum DATE,
    End_Datum DATE,
    Wecker_Zeit DATE,
    Wecker_Aktiv CHAR(1) NOT NULL CHECK(Wecker_Aktiv in ('1','0'))
);

--DROP TABLE Termine;

CREATE TABLE Erinnerungs_Mail
(
    ID_Erinnerungs_Mail INT NOT NULL,
    PRIMARY KEY (ID_Erinnerungs_Mail),
    Datum DATE NOT NULL,
    Nachricht VARCHAR(512)
);

--DROP TABLE Erinnerungs_Mail

CREATE TABLE Student
(
    Matrikelnummer INT NOT NULL,
    PRIMARY KEY (Matrikelnummer),
    Name VARCHAR(50) NOT NULL,
    Vorname VARCHAR(50) NOT NULL,
    Semester INT NOT NULL,
    ECTS INT NOT NULL,
    Notizen VARCHAR(300),
    Bild BLOB
);

--DROP TABLE Student;

CREATE TABLE E_Mail
(
    ID_Mail INT NOT NULL,
    Matrikelnummer INT,
    PRIMARY KEY (ID_Mail),
    FOREIGN KEY(Matrikelnummer)REFERENCES Student(Matrikelnummer),
    Inhalt VARCHAR(512)
);

--DROP TABLE E_Mail;

CREATE TABLE Po
(
    PO VARCHAR(50) NOT NULL,
    PRIMARY KEY (PO)
);

CREATE TABLE Fach
(
    ID_Fach INT NOT NULL,
    PRIMARY KEY (ID_Fach),
    Titel_Fach VARCHAR(50),
    ECTS INT NOT NULL
);

--DROP TABLE Fach;

--TEST SPACE-------------------------------------------------------------------

SELECT * FROM Thema;
SELECT * FROM Formulare;
SELECT * FROM Anliegen;
SELECT * FROM Dokument;
SELECT * FROM Termine;
SELECT * FROM Erinnerungs_Mail;
SELECT * FROM Student;
SELECT * FROM E_Mail;
SELECT * FROM Po;
SELECT * FROM Fach;

INSERT INTO Thema (Titel_Thema)
VALUES ('Sonstiges');

INSERT INTO Formulare (Titel_Formulare, Datei)
VALUES ('Sonstiges Formular', '1F');

INSERT INTO Anliegen (ID_Anliegen, Titel_Thema, Titel_Anliegen, Notizen)
VALUES ('1', 'Sonstiges', 'Test Anliegen', 'Blablabla');

INSERT INTO Dokument (Titel_Dokument, Datei)
VALUES ('Sonstiges Dokument', '2F');

INSERT INTO Termine (ID_Termine, ID_Anliegen, Start_Datum, End_Datum, Wecker_Zeit, Wecker_Aktiv)
VALUES ('1', '1', '02.03.2018', '03.03.2018', '01.03.2018', '1');

INSERT INTO Erinnerungs_Mail (ID_Erinnerungs_Mail, Datum, Nachricht)
VALUES ('1', '23.02.2018', 'Vergess nicht fuer THI zu lernen!');

INSERT INTO Student (Matrikelnummer, Name, Vorname, Semester, ECTS, Notizen, Bild)
VALUES ('961997', 'Lehmann', 'Martin', '9', '100', 'Faul', '23F');

INSERT INTO E_Mail (ID_Mail, Matrikelnummer, Inhalt)
VALUES ('1', '961997', 'Hoer auf rum zu waffeln!');

INSERT INTO Po (Po)
VALUES ('Informatik 2013');

INSERT INTO Fach (ID_Fach, Titel_Fach, ECTS)
VALUES ('1', 'Datenbanken', '5');

--CREATE ALL SECONDARY TABLES--------------------------------------------------

CREATE TABLE hat_Formulare
(
    Titel_Formulare VARCHAR(50),
    Titel_Thema VARCHAR(50),
    FOREIGN KEY (Titel_Formulare)REFERENCES Formulare(Titel_Formulare),
    FOREIGN KEY (Titel_Thema)REFERENCES Thema(Titel_Thema)
);

--DROP TABLE hat_Formulare;

CREATE TABLE hat_Anliegen
(
    ID_Anliegen INT,
    Matrikelnummer INT,
    FOREIGN KEY(ID_Anliegen)REFERENCES Anliegen(ID_Anliegen),
    FOREIGN KEY(Matrikelnummer)REFERENCES Student(Matrikelnummer)
);

--DROP TABLE hat_Anliegen;

CREATE TABLE hat_Po
(
    Matrikelnummer INT,
    Po VARCHAR(50),
    FOREIGN KEY(Matrikelnummer)REFERENCES Student(Matrikelnummer),
    FOREIGN KEY(Po)REFERENCES Po(Po)
);

--DROP TABLE hat_Po;

CREATE TABLE hat_Fach
(
    ID_Fach INT,
    Po VARCHAR(50),
    FOREIGN KEY(ID_Fach)REFERENCES Fach(ID_Fach),
    FOREIGN KEY(Po)REFERENCES Po(Po)
);

--DROP TABLE hat_Fach;

--TEST SPACE-------------------------------------------------------------------

SELECT * FROM hat_Formulare;
SELECT * FROM hat_Anliegen;
SELECT * FROM hat_Po;
SELECT * FROM hat_Fach;

INSERT INTO hat_Formulare (Titel_Thema, Titel_Formulare)
VALUES ('Sonstiges', 'Sonstiges Formular');

INSERT INTO hat_Anliegen (Matrikelnummer, ID_Anliegen)
VALUES ('961997', '1');

INSERT INTO hat_Po (Matrikelnummer, Po)
VALUES('961997', 'Informatik 2013');

INSERT INTO hat_Fach (Po, ID_Fach)
VALUES ('Informatik 2013', '1');

-------------------------------------------------------------------------------

SELECT s.Name, s.Vorname
FROM Student s, Anliegen a, hat_Anliegen b
WHERE   a.ID_Anliegen= b.ID_Anliegen AND
        s.Matrikelnummer = b.Matrikelnummer;