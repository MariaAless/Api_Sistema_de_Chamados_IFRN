CREATE TABLE TB_SETOR (
                          id    BIGSERIAL PRIMARY KEY,
                          nome  VARCHAR(100) NOT NULL,
                          sigla VARCHAR(20)  NOT NULL UNIQUE
);

CREATE TABLE TB_CATEGORIA (
                              id        BIGSERIAL PRIMARY KEY,
                              nome      VARCHAR(100) NOT NULL,
                              descricao VARCHAR(255),
                              setor_id  BIGINT NOT NULL REFERENCES TB_SETOR(id)
);

CREATE TABLE TB_USUARIO (
                            id        BIGSERIAL PRIMARY KEY,
                            nome      VARCHAR(150) NOT NULL,
                            email     VARCHAR(150) NOT NULL UNIQUE,
                            matricula VARCHAR(30)  NOT NULL,
                            tipo      VARCHAR(20)  NOT NULL,
                            setor_id  BIGINT REFERENCES TB_SETOR(id)
);

CREATE TABLE TB_CHAMADO (
                            id              BIGSERIAL PRIMARY KEY,
                            titulo          VARCHAR(150) NOT NULL,
                            descricao       TEXT         NOT NULL,
                            local           VARCHAR(150) NOT NULL,
                            prioridade      VARCHAR(10)  NOT NULL,
                            status          VARCHAR(20)  NOT NULL,
                            data_abertura   TIMESTAMP    NOT NULL,
                            data_fechamento TIMESTAMP,
                            solucao         TEXT,
                            solicitante_id  BIGINT NOT NULL REFERENCES TB_USUARIO(id),
                            tecnico_id      BIGINT REFERENCES TB_USUARIO(id),
                            categoria_id    BIGINT NOT NULL REFERENCES TB_CATEGORIA(id)
);

CREATE TABLE TB_INTERACAO (
                              id         BIGSERIAL PRIMARY KEY,
                              texto      TEXT      NOT NULL,
                              data_hora  TIMESTAMP NOT NULL,
                              chamado_id BIGINT NOT NULL REFERENCES TB_CHAMADO(id),
                              autor_id   BIGINT NOT NULL REFERENCES TB_USUARIO(id)
);

CREATE INDEX idx_chamado_status      ON TB_CHAMADO(status);
CREATE INDEX idx_chamado_solicitante ON TB_CHAMADO(solicitante_id);
CREATE INDEX idx_chamado_tecnico     ON TB_CHAMADO(tecnico_id);
CREATE INDEX idx_chamado_categoria   ON TB_CHAMADO(categoria_id);
CREATE INDEX idx_interacao_chamado   ON TB_INTERACAO(chamado_id);