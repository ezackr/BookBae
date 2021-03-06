CREATE TABLE user_info
(
    user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    name VARCHAR(64),
    gender VARCHAR(2),
    fav_genre VARCHAR(20),
    birthday DATE,
    email VARCHAR(254) NOT NULL,
    zipcode CHAR(5),
    bio VARCHAR(500),
    phone_num VARCHAR(15),
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
    low_target_age TINYINT,
    high_target_age TINYINT,
    within_x_miles SMALLINT,
    preferred_gender VARCHAR(6),
    user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE user_book
(
    book_id VARCHAR(2048) NOT NULL,
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
    is_mutual CHAR(1) NOT NULL,
    like_id UNIQUEIDENTIFIER NOT NULL,
    liker_user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    liked_user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    PRIMARY KEY (like_id),
    FOREIGN KEY (liker_user_id) REFERENCES user_info(user_id),
    FOREIGN KEY (liked_user_id) REFERENCES user_info(user_id)
);

CREATE TABLE chat_line
(
    line_id INT IDENTITY(1,1) NOT NULL,
    line_text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    like_id UNIQUEIDENTIFIER NOT NULL,
    sender_user_id UNIQUEIDENTIFIER default NEWID() NOT NULL,
    PRIMARY KEY (line_id),
    FOREIGN KEY (like_id) REFERENCES likes(like_id),
    FOREIGN KEY (sender_user_id) REFERENCES user_info(user_id)
);