CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  available BOOLEAN DEFAULT TRUE,
  user_id BIGINT,
  CONSTRAINT fk_items_to_users FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS booking(
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 item_id BIGINT NOT NULL,
 booker_id BIGINT NOT NULL,
 start_booking TIMESTAMP NOT NULL,
 end_booking TIMESTAMP NOT NULL,
 status VARCHAR(50) NOT NULL,
 CONSTRAINT fk_booking_to_user FOREIGN KEY (booker_id) REFERENCES users(id),
 CONSTRAINT fk_booking_to_item FOREIGN KEY (item_id) REFERENCES items(id),
 CONSTRAINT pk_booking PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS comment(
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 text varchar(512) NOT NULL,
 item_id BIGINT NOT NULL,
 author_id BIGINT NOT NULL,
 time_create TIMESTAMP NOT NULL,
 CONSTRAINT fk_comment_to_user FOREIGN KEY (author_id) REFERENCES users(id),
 CONSTRAINT fk_comment_to_item FOREIGN KEY (item_id) REFERENCES items(id),
 CONSTRAINT fk_comment PRIMARY KEY(id)
);