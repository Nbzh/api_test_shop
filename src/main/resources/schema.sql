-- schema.sql
CREATE TABLE IF NOT EXISTS article (
id             VARCHAR(60)     PRIMARY KEY,
categoryId     VARCHAR(60)     NOT NULL,
name           VARCHAR         NOT NULL,
image          VARCHAR,
description    VARCHAR,
isVeggan       BOOLEAN         NOT NULL,
price          DECIMAL(10,2)   NOT NULL,
priceUnit      ENUM('UNIT_PIECE', 'UNIT_MEAL', 'UNIT_GROUP')    NOT NULL
);

CREATE TABLE IF NOT EXISTS label (
id             VARCHAR(60)     PRIMARY KEY,
name           VARCHAR         NOT NULL,
image          VARCHAR
);

CREATE TABLE IF NOT EXISTS category (
id             VARCHAR(60)     PRIMARY KEY,
name           VARCHAR         NOT NULL,
image          VARCHAR,
color          CHAR(6)
);

CREATE TABLE IF NOT EXISTS article_label (
articleId     VARCHAR(60)     NOT NULL,
labelId       VARCHAR(60)     NOT NULL,
PRIMARY KEY (articleId, labelId)
);