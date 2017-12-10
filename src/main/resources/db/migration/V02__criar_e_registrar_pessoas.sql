CREATE TABLE pessoa
(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(150) NOT NULL,
	ativo TINYINT(1) NOT NULL,
	logradouro VARCHAR(150) NULL,
	numero VARCHAR(6) NULL,
	complemento VARCHAR(30) NULL,
	bairro VARCHAR(30) NULL,
	cep VARCHAR(9) NULL,
	cidade VARCHAR(30) NULL,
	estado VARCHAR(3) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa VALUES 
(
	NULL,
	'Lillian J. Brice',
	0,
	'Rua Seis',
	'529',
	'Casa',
	'',
	'18113-741',
	'Votorantim',
	'SP'
);

INSERT INTO pessoa VALUES 
(
	NULL,
	'Michael M. Bradley',
	0,
	'2ª Travessa Francisco Viana',
	'35',
	'Casa',
	'',
	'55816-272',
	'Carpina',
	'PE'
);

INSERT INTO pessoa VALUES 
(
	NULL,
	'Richard C. Austin',
	0,
	'Rua Antônio Frederico Ozanam',
	'1079',
	'Apartamento',
	'',
	'87302-280',
	'Campo Mourão',
	'PR'
);

INSERT INTO pessoa VALUES 
(
	NULL,
	'Randall B. Rauch',
	0,
	'Rua Henrique Alvarenga',
	'1822',
	'Apartamento',
	'',
	'36302-094',
	'São João Del Rei',
	'MG'
);

INSERT INTO pessoa VALUES 
(
	NULL,
	'Robert J. Hardy',
	0,
	'Rua Santo Inácio',
	'1434',
	'Trailer',
	'',
	'09040-420',
	'Santo André',
	'SP'
);

INSERT INTO pessoa VALUES 
(
	NULL,
	'Melissa R. Ainsworth',
	0,
	'Quadra Quadra 605 Conjunto 12',
	'66',
	'Casa',
	'',
	'72641-112',
	'Recanto das Emas',
	'DF'
);

INSERT INTO pessoa VALUES 
(
	NULL,
	'Rick C. Bose',
	1,
	'Rua A CBAM da Vendinha',
	'68',
	'Sobrado',
	'',
	'23030-230',
	'Rio de Janeiro',
	'RJ'
);

