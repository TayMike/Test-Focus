CREATE TABLE restaurante (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    tipo_cozinha VARCHAR(255) NOT NULL,
    capacidade INT NOT NULL
);

CREATE TABLE restaurante_horarios (
    restaurante_id UUID,
    horario TIME,
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id) ON DELETE CASCADE
);

CREATE TABLE restaurante_avaliacoes (
    restaurante_id UUID,
    avaliacao INT,
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id) ON DELETE CASCADE
);

CREATE TABLE restaurante_comentarios (
    restaurante_id UUID,
    comentario TEXT,
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id) ON DELETE CASCADE
);


INSERT INTO restaurante (id, nome, localizacao, tipo_cozinha, capacidade)
VALUES
  ('c0fd5f16-6d42-4f98-b7d1-d32a5c63bba4', 'Restaurante A', 'Local A', 'Italiana', 50),
  ('fe2b78a9-8e77-45f0-b0be-6c09382d35c1', 'Restaurante B', 'Local B', 'Brasileira', 40);

INSERT INTO mesa (id, restaurante_id, numero)
VALUES
  ('b0d9f6b3-5c7d-4a36-ae5a-d8db5a040d75', 'c0fd5f16-6d42-4f98-b7d1-d32a5c63bba4', 1),
  ('b7f69d7e-cd33-49cf-bd96-174592a158a3', 'fe2b78a9-8e77-45f0-b0be-6c09382d35c1', 2);

INSERT INTO mesa_datahora (mesa_id, data_hora)
VALUES
  ('b0d9f6b3-5c7d-4a36-ae5a-d8db5a040d75', '2025-03-25T12:00:00'),
  ('b7f69d7e-cd33-49cf-bd96-174592a158a3', '2025-03-25T13:00:00');

INSERT INTO mesa_estado (mesa_id, estado)
VALUES
  ('b0d9f6b3-5c7d-4a36-ae5a-d8db5a040d75', true),
  ('b7f69d7e-cd33-49cf-bd96-174592a158a3', false);

INSERT INTO mesa_confirmacao (mesa_id, confirmacao)
VALUES
  ('b0d9f6b3-5c7d-4a36-ae5a-d8db5a040d75', true),
  ('b7f69d7e-cd33-49cf-bd96-174592a158a3', false);

INSERT INTO restaurante_horarios (restaurante_id, horario)
VALUES
  ('c0fd5f16-6d42-4f98-b7d1-d32a5c63bba4', '08:00:00'),
  ('fe2b78a9-8e77-45f0-b0be-6c09382d35c1', '09:00:00');

INSERT INTO restaurante_avaliacoes (restaurante_id, avaliacao)
VALUES
  ('c0fd5f16-6d42-4f98-b7d1-d32a5c63bba4', 5),
  ('fe2b78a9-8e77-45f0-b0be-6c09382d35c1', 4);

INSERT INTO restaurante_comentarios (restaurante_id, comentario)
VALUES
  ('c0fd5f16-6d42-4f98-b7d1-d32a5c63bba4', 'Ótimo restaurante!'),
  ('fe2b78a9-8e77-45f0-b0be-6c09382d35c1', 'Comida boa, mas o atendimento poderia ser melhor.');