CREATE TABLE user_info
(
    user_id UNIQUEIDENTIFIER NOT NULL,
    name VARCHAR(64) NOT NULL,
    gender VARCHAR(2) NOT NULL,
    phone_num VARCHAR(15) NOT NULL,
    fav_genre VARCHAR(20) NOT NULL,
    birthday DATE NOT NULL,
    email VARCHAR(254) NOT NULL,
    zipcode CHAR(5) NOT NULL,
    bio VARCHAR(500) NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (phone_num),
    UNIQUE (email)
);

CREATE TABLE prompt
(
    prompt_id TINYINT NOT NULL,
    question VARCHAR(100) NOT NULL,
    answer VARCHAR(250) NOT NULL,
    user_id UNIQUEIDENTIFIER NOT NULL,
    PRIMARY KEY (prompt_id, user_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE preference
(
    low_target_age TINYINT NOT NULL,
    high_target_age TINYINT NOT NULL,
    within_x_miles SMALLINT NOT NULL,
    user_id UNIQUEIDENTIFIER NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE target_gender
(
    gender CHAR(2) NOT NULL,
    user_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE user_book
(
    book_id VARCHAR(12) NOT NULL,
    user_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE login_info
(
    salt TEXT NOT NULL,
    hash TEXT NOT NULL,
    user_id UNIQUEIDENTIFIER NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE likes
(
    is_mutual BIT NOT NULL,
    liker_user_id UNIQUEIDENTIFIER NOT NULL,
    liked_user_id UNIQUEIDENTIFIER NOT NULL,
    PRIMARY KEY (liker_user_id, liked_user_id),
    FOREIGN KEY (liker_user_id) REFERENCES user_info(user_id),
    FOREIGN KEY (liked_user_id) REFERENCES user_info(user_id)
);

CREATE TABLE chat
(
    chat_id UNIQUEIDENTIFIER NOT NULL,
    user_id1 UNIQUEIDENTIFIER NOT NULL,
    user_id2 UNIQUEIDENTIFIER NOT NULL,
    PRIMARY KEY (chat_id),
    FOREIGN KEY (user_id1) REFERENCES user_info(user_id),
    FOREIGN KEY (user_id2) REFERENCES user_info(user_id)
);

CREATE TABLE chat_line
(
    line_id INT NOT NULL,
    line_text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    chat_id UNIQUEIDENTIFIER NOT NULL,
    PRIMARY KEY (line_id, chat_id),
    FOREIGN KEY (chat_id) REFERENCES chat(chat_id)
);