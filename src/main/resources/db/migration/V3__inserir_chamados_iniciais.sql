INSERT INTO TB_CHAMADO (
    titulo,
    descricao,
    local,
    prioridade,
    status,
    data_abertura,
    data_fechamento,
    solucao,
    solicitante_id,
    tecnico_id,
    categoria_id
)
VALUES (
           'Computador nao liga',
           'O computador da sala nao liga ao pressionar o botao de energia.',
           'Sala 10',
           'MEDIA',
           'ABERTO',
           CURRENT_TIMESTAMP - INTERVAL '2 days',
           NULL,
           NULL,
           (SELECT id FROM TB_USUARIO WHERE email = 'maria.silva@example.com'),
           NULL,
           (SELECT id FROM TB_CATEGORIA WHERE nome = 'Computadores')
       );

-- Chamado 2: em atendimento
INSERT INTO TB_CHAMADO (
    titulo,
    descricao,
    local,
    prioridade,
    status,
    data_abertura,
    data_fechamento,
    solucao,
    solicitante_id,
    tecnico_id,
    categoria_id
)
VALUES (
           'Problema na conexao Wi-Fi',
           'Os computadores da sala estao apresentando falhas de conexao com a rede sem fio.',
           'Sala 12',
           'ALTA',
           'EM_ATENDIMENTO',
           CURRENT_TIMESTAMP - INTERVAL '1 day',
           NULL,
           NULL,
           (SELECT id FROM TB_USUARIO WHERE email = 'joao.santos@example.com'),
           (SELECT id FROM TB_USUARIO WHERE email = 'ana.oliveira@example.com'),
           (SELECT id FROM TB_CATEGORIA WHERE nome = 'Rede e Wi-Fi')
       );


INSERT INTO TB_CHAMADO (
    titulo,
    descricao,
    local,
    prioridade,
    status,
    data_abertura,
    data_fechamento,
    solucao,
    solicitante_id,
    tecnico_id,
    categoria_id
)
VALUES (
           'Projetor sem imagem',
           'O projetor da sala nao apresenta imagem durante as aulas.',
           'Sala 15',
           'MEDIA',
           'RESOLVIDO',
           CURRENT_TIMESTAMP - INTERVAL '3 days',
           CURRENT_TIMESTAMP - INTERVAL '2 days',
           'Foi identificado e corrigido um problema na conexao do cabo HDMI.',
           (SELECT id FROM TB_USUARIO WHERE email = 'maria.silva@example.com'),
           (SELECT id FROM TB_USUARIO WHERE email = 'ana.oliveira@example.com'),
           (SELECT id FROM TB_CATEGORIA WHERE nome = 'Projetores')
       );