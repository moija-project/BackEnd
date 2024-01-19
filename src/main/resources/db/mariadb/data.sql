INSERT INTO `user` (
    `user_id`,
    `nickname`,
    `gender`,
    `birth`,
    `phone_number`,
    `name`,
    `profile`,
    `time_join`,
    `reliability_user`,
    `password`,
    `is_available`
) VALUES (
             'testman1',
             '테스트맨1',
             TRUE,
             '2001-01-01',
             123456788,
             '김수정',
             TRUE,
             NOW(),
             2.5,
             'hashed_password',
             true
         );

INSERT INTO `user` (
    `user_id`,
    `nickname`,
    `gender`,
    `birth`,
    `phone_number`,
    `name`,
    `profile`,
    `time_join`,
    `reliability_user`,
    `password`,
    `is_available`
) VALUES (
             'testman2',
             '테스트맨2',
             TRUE,
             '2001-01-01',
             123456788,
             '김수정',
             TRUE,
             NOW(),
             2.5,
             'hashed_password',
             true
         );