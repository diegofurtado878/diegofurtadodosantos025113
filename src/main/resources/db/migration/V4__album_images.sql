CREATE TABLE album_images (
    id BIGSERIAL PRIMARY KEY,
    album_id INTEGER NOT NULL,
    object_key VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_album_images_album FOREIGN KEY (album_id) REFERENCES ALBUM(ID_ALBUM) ON DELETE CASCADE
);

CREATE INDEX idx_album_images_album_id ON album_images(album_id);
