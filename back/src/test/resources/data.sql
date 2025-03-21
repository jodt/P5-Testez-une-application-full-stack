INSERT IGNORE INTO TEACHERS (id, first_name, last_name, created_at, updated_at)
VALUES (1, 'teacher', 'teacherName', '2025-03-21 10:00:00', '2025-03-21 10:00:00'),
       (2, 'otherTeacher', 'otherTeacherName', '2025-03-21 10:00:00', '2025-03-21 10:00:00');

INSERT IGNORE INTO USERS (id, first_name, last_name, admin, email, password)
VALUES (1, 'userFirstName', 'userLastName', true, 'user@mail.fr', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
       (2, 'otherUserFirstName', 'otherUserLastName', true, 'otherUser@mail.fr', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

INSERT IGNORE INTO SESSIONS (id, name, description, date, teacher_id, created_at, updated_at)
VALUES (1, 'Yoga', 'Yoga for beginners','2025-06-05 02:00:00',  1, '2025-03-21 10:00:00', '2025-03-21 10:00:00'),
       (2, 'Relaxation', 'Relaxation for beginners','2025-06-10 02:00:00',  1, '2025-03-21 10:00:00', '2025-03-21 10:00:00');

INSERT IGNORE INTO PARTICIPATE (user_id, session_id)
VALUES (1,1);