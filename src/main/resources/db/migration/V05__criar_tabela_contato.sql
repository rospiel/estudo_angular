CREATE TABLE contato (
	codigo BIGINT(20) 		PRIMARY KEY AUTO_INCREMENT,
	codigo_pessoa BIGINT(20) 	NOT NULL,
	nome VARCHAR(50) 		NOT NULL,
	email VARCHAR(100) 		NOT NULL,
	telefone VARCHAR(20) 		NOT NULL,
  FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into contato values (null, 8, 'Kaique Bernardo Cauã Drumond', 	'kaiquebernardocauadrumond@integrasjc.com.br', 			'61 99705-1905');
insert into contato values (null, 8, 'João Luiz Gonçalves', 		'jjoaoluizgoncalves@vivalle.com.br', 				'62 99953-3395');
insert into contato values (null, 8, 'Renata Josefa Louise Fernandes', 	'renatajosefalouisefernandes_@balloons.com.br', 		'62 2711-3387');
insert into contato values (null, 8, 'Manuela Allana Araújo', 		'mmanuelaallanaaraujo@tasaut.com.br', 				'83 99667-9203');
insert into contato values (null, 8, 'Isabela Andrea Sophia Nunes', 	'iisabelaandreasophianunes@distribuidorapetfarm.com.br', 	'83 3780-2728');
insert into contato values (null, 8, 'Evelyn Manuela da Silva', 	'eevelynmanueladasilva@ozzape.com', 				'83 99870-9454');
insert into contato values (null, 8, 'Cláudia Aparecida Bernardes', 	'claudiaaparecidabernardes@zipmail.com', 			'83 3765-8673');
insert into contato values (null, 8, 'Bárbara Luiza Fogaça', 		'barbaraluizafogaca-84@urbam.com.br', 				'83 3857-0388');