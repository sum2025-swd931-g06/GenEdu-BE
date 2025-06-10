-- Insert data into school_classes
INSERT INTO school_classes (name, description) VALUES
                                                   ('10', 'Lớp 10'),
                                                   ('11', 'Lớp 11');

-- Insert data into subjects
INSERT INTO subjects (name, description, school_class_id) VALUES
                                                              ('Lịch Sử 10', 'Lịch Sử 10 Cánh Diều', 1),
                                                              ('Địa Lý 10', 'Địa Lý 10 Kết Nối', 2);

-- Insert data into chapters
INSERT INTO chapters (order_number, title, description, subject_id) VALUES
                                                                        (1, 'Lịch Sử Và Sử Học', '', 1),
                                                                        (2, 'Vai Trò Của Sử Học', '', 1),
                                                                        (1, 'Sử Dụng Bản Đồ', '', 2);

-- Insert data into lessons
INSERT INTO lessons (title, order_number, description, chapter_id) VALUES
                                                                       ('Hiện thực lịch sử và lịch sử được con người nhận thức', 1, '', 1),
                                                                       ('Tri thức lịch sử và cuộc sống', 2, '', 1),
                                                                       ('Sử học với công tác bảo tồn và phát huy giá trị di sản văn hóa, di sản thiên nhiên và phát triển du lịch', 1, '', 2),
                                                                       ('Phương pháp biểu hiện các đối tượng địa lý trên bản đồ', 1, '', 3),
                                                                       ('Sử dụng bản dồ trong học tập và đời sống', 2, '', 3);
