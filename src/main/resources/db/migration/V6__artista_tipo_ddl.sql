-- Popula TIPO_ARTISTA conforme exemplos do edital: Serj Tankian, Mike Shinoda, Michel Teló = CANTOR; Guns N' Roses = BANDA
-- (coluna já criada em V2__add_artist_type.sql)
UPDATE ARTISTA SET TIPO_ARTISTA = 'CANTOR' WHERE NOME_ARTISTA IN ('SERJ TANKIAN', 'MIKE SHINODA', 'MICHEL TELÓ');
UPDATE ARTISTA SET TIPO_ARTISTA = 'BANDA' WHERE NOME_ARTISTA = 'GUNS N'' ROSES';

-- Garante NOT NULL para novos registros (opcional; mantendo nullable para compatibilidade)
-- ALTER TABLE ARTISTA ALTER COLUMN TIPO_ARTISTA SET NOT NULL;
