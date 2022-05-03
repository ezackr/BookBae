CREATE TABLE user_info
(
    user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    name VARCHAR(64),
    gender VARCHAR(2),
    phone_num VARCHAR(15) NOT NULL,
    fav_genre VARCHAR(20),
    birthday DATE,
    email VARCHAR(254) NOT NULL,
    zipcode CHAR(5),
    bio VARCHAR(500),
    preferred_gender CHAR(10),
    PRIMARY KEY (user_id)
);

CREATE TABLE prompt
(
    prompt_id TINYINT NOT NULL,
    question VARCHAR(100) NOT NULL,
    answer VARCHAR(250) NOT NULL,
    user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    PRIMARY KEY (prompt_id, user_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE preference
(
    low_target_age TINYINT NOT NULL,
    high_target_age TINYINT NOT NULL,
    within_x_miles SMALLINT NOT NULL,
    user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE user_book
(
    book_id VARCHAR(12) NOT NULL,
    user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE login_info
(
    salt VARCHAR(29) NOT NULL,
    hash VARCHAR(60) NOT NULL,
    user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE likes
(
    is_mutual BIT NOT NULL,
    liker_user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    liked_user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    PRIMARY KEY (liker_user_id, liked_user_id),
    FOREIGN KEY (liker_user_id) REFERENCES user_info(user_id),
    FOREIGN KEY (liked_user_id) REFERENCES user_info(user_id)
);

CREATE TABLE chat
(
    chat_id UNIQUEIDENTIFIER NOT NULL,
    user_id1 UNIQUEIDENTIFIER default NEWID() NOT NULL,
    user_id2 UNIQUEIDENTIFIER default NEWID() NOT NULL,
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