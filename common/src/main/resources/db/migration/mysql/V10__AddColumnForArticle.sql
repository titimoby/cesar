ALTER TABLE Article ADD LANG VARCHAR(2);

INSERT INTO Article (CONTENT,HEADLINE,NBCONSULTS,POSTEDAT,TITLE,VALID,AUTHOR_ID, LANG) SELECT CONTENT,HEADLINE,NBCONSULTS,POSTEDAT,TITLE,VALID,AUTHOR_ID, 'en' FROM Article;
UPDATE Article SET LANG='fr' where LANG is null;