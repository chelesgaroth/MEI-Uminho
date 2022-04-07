
INSERT INTO users (IdUser, Name, Email, Passwd,DOB) 
VALUES 
 ('eteli01' ,'Eltevyna Santos', 'etelvina@gmail.com','passws1234','1980-04-18'),
 ('bosques' , 'Mario Matos e Bosques','mmb@gmail.com','passws1234','1969-06-04'),
 ('margarida123' , 'Margarida Sampaio', 'msampaio@gmail.com','passws1234','2000-07-07'),
 ('fonseca04' , 'Manuel Fonseca ', 'manelFonsi@gmail.com','passws1234','1995-05-08'),
 ('josefaMaria1973' , 'Josefa Maria', 'jsefM@gmail.com','passws1234','1973-02-01');


INSERT INTO [dbo].[bookmakers] (IdBmaker, Email, Passwd)
VALUES
('admin1', 'admin@rasbet.com', 'rasbet1234');


INSERT INTO wallets(IdCurrency,IdUser,Balance)
VALUES
 ('EUR','eteli01',100),
 ('BTC','eteli01',0.005),
 ('USD','margarida123',50),
 ('EUR','margarida123',5),
 ('USD','fonseca04',23),
 ('BTC','fonseca04',0.002),
 ('USD','bosques',12),
 ('BTC','josefaMaria1973',76);


 INSERT INTO currencies(IdCurrency,Name)
 VALUES
 ('EUR','Euro'),
 ('BTC','Bitcoin'),
 ('USD','American Dollar');


INSERT INTO currencyTax(IdcurrencyOri,IdCurrencyDest,Tax)
VALUES
  ('EUR','USD',1.132),
  ('EUR','BTC',0.000027),
  ('USD','EUR',0.883),
  ('USD','BTC',0.000024),
  ('BTC','EUR',36675.24),
  ('BTC','USD',41520);


INSERT INTO sports(IdSport,Name,Type,Typology)
VALUES
('FOT','Football',0,0),
('BASK','Basketball',0,1),
('F1','Formula 1',1,2);

INSERT INTO competitions(IdComp,Name,IdSport)
VALUES
('F1WC','Formula One World Championship','F1'),
('CL','Champions League','FOT'),
('EL','European League','FOT'),
('NBA','American Basketball Championship','BASK');


INSERT INTO events(IdEvent,InitialDate,IdBmaker,IdComp,FinalResult,EventStatus,Description)
VALUES
('Event1','2022-02-01 13:30:00','admin1','CL',NULL,0,'Benfica against Ajax quarter finals'),
('Event2','2022-02-02 14:30:00','admin1','F1WC',NULL,0,'Fifth race before the final'),
('Event3','2022-02-03 15:30:00','admin1','NBA',NULL,0,'Celtics against Lakers'),
('Event4','2022-03-01 17:30:00','admin1','EL','2-0',1,'Home team won the game'),
('Event5','2022-04-01 11:30:00','admin1','BASK',NULL,2,'Covid outbreak');


/*INSERT INTO participants(IdParticipant,Name,IdSport,IdEvent) VALUES*/



/* 1 -> home team wins , X -> Tie , 2-> Guest team wins*/
INSERT INTO choices(IdChoice,Odd,IdEvent,Status,Result)
VALUES
('Choice1',8.0,'Event1',1,'1'),
('Choice2',4.40,'Event1',1,'X'),
('Choice3',1.38,'Event1',1,'2'),
('Choice4',10,'Event2',1,'Lewis Hamilton'),
('Choice5',2.56,'Event3',2,'1'),
('Choice6',4.78,'Event3',2,'2');

INSERT INTO bets(IdBet,IdUser,IdChoice,Amount,Odd,IdCurrency) 
VALUES
('Bet1','eteli01','Choice1',20,9.2,'EUR'),
('Bet2','eteli01','Choice2',0.0025,3.7,'BTC'),
('Bet3','margarida123','Choice3',4,1.42,'EUR'),
('Bet4','fonseca04','Choice3',7.5,1.42,'USD');



INSERT INTO participant(IdParticipant,Name,IdSport)
VALUES
('Racer1','Lewis Hamilton','F1'),
('Racer2','Max Verstappen','F1'),
('Racer3','Valtteri Bottas','F1'),
('SLB','Benfica','FOT'),
('AJX','Ajax','FOT'),
('LK','Lakers','BASK'),
('CB','Chicago Bulls','BASK');


INSERT INTO participantEvent(IdParticipant,IdEvent)
VALUES 
('Racer1','Event2'),
('Racer2','Event2'),
('Racer3','Event2'),
('SLB','Event1'),
('SLB','Event4'),
('AJX','Event1'),
('CB','Event3'),
('LK','Event3');
