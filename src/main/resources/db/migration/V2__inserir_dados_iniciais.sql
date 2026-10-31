INSERT INTO TB_SETOR (nome, sigla)
VALUES
    ('Coordenacao de Tecnologia da Informacao', 'COTIC'),
    ('Manutencao e Infraestrutura', 'MANUT');


INSERT INTO TB_CATEGORIA (nome, descricao, setor_id)
VALUES
    ('Computadores', 'Problemas em computadores e notebooks',
     (SELECT id FROM TB_SETOR WHERE sigla = 'COTIC')),

    ('Rede e Wi-Fi', 'Problemas de conexao e acesso a internet',
     (SELECT id FROM TB_SETOR WHERE sigla = 'COTIC')),

    ('Projetores', 'Problemas em projetores e equipamentos de exibicao',
     (SELECT id FROM TB_SETOR WHERE sigla = 'COTIC')),

    ('Infraestrutura', 'Problemas em salas, instalacoes e estrutura fisica',
     (SELECT id FROM TB_SETOR WHERE sigla = 'MANUT'));

INSERT INTO TB_USUARIO (nome, email, matricula, tipo, setor_id)
VALUES
    ('Maria Silva', 'maria.silva@example.com', '2026001', 'SOLICITANTE', NULL),

    ('Joao Santos', 'joao.santos@example.com', '2026002', 'SOLICITANTE', NULL),

    ('Ana Oliveira', 'ana.oliveira@example.com', 'TEC001', 'TECNICO',
     (SELECT id FROM TB_SETOR WHERE sigla = 'COTIC')),

    ('Carlos Souza', 'carlos.souza@example.com', 'TEC002', 'TECNICO',
     (SELECT id FROM TB_SETOR WHERE sigla = 'MANUT'));