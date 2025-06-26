CREATE SEQUENCE IF NOT EXISTS chapters_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS lesson_content_media_files_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS lesson_contents_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS lessons_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS materials_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS school_classes_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS subjects_id_seq START WITH 1 INCREMENT BY 1;

-- Alter table để id tự tăng theo sequence
ALTER TABLE school_classes ALTER COLUMN id SET DEFAULT nextval('school_classes_id_seq');
ALTER TABLE subjects ALTER COLUMN id SET DEFAULT nextval('subjects_id_seq');
ALTER TABLE materials ALTER COLUMN id SET DEFAULT nextval('materials_id_seq');
ALTER TABLE chapters ALTER COLUMN id SET DEFAULT nextval('chapters_id_seq');
ALTER TABLE lessons ALTER COLUMN id SET DEFAULT nextval('lessons_id_seq');
ALTER TABLE lesson_contents ALTER COLUMN id SET DEFAULT nextval('lesson_contents_id_seq');
ALTER TABLE lesson_content_media_files ALTER COLUMN id SET DEFAULT nextval('lesson_content_media_files_id_seq');


-- SCHOOL CLASSES
INSERT INTO school_classes (name, description, is_deleted) VALUES
                                                                   ('Lớp 10', 'Khối lớp 10 THPT', false),
                                                                   ('Lớp 11', 'Khối lớp 11 THPT', false),
                                                                   ('Lớp 12', 'Khối lớp 12 THPT', false);

-- SUBJECTS (id tự tăng)
INSERT INTO subjects (name, description, school_class_id, is_deleted) VALUES
                                                                          ('Lịch sử', 'Lịch sử lớp 10', 1, false),
                                                                          ('Toán học', 'Toán học lớp 10', 1, false),
                                                                          ('Ngữ văn', 'Ngữ văn lớp 10', 1, false),
                                                                          ('Tiếng Anh', 'Tiếng Anh lớp 10', 1, false),
                                                                          ('Địa lý', 'Địa lý lớp 10', 1, false),
                                                                          ('Vật lý', 'Vật lý lớp 10', 1, false),
                                                                          ('Hóa học', 'Hóa học lớp 10', 1, false),
                                                                          ('Sinh học', 'Sinh học lớp 10', 1, false),
                                                                          ('Tin học', 'Tin học lớp 10', 1, false),
                                                                          ('GDCD', 'Giáo dục công dân lớp 10', 1, false);

-- MATERIALS (id tự tăng, mapping subject_id theo thứ tự insert)
-- Lịch sử: 3 sách
INSERT INTO materials (title, order_number, description, subject_id, is_deleted) VALUES
                                                                                     ('Lịch sử 10 Kết nối tri thức', 1, 'SGK Lịch sử 10 - Kết nối tri thức', 1, false),
                                                                                     ('Lịch sử 10 Cánh diều', 2, 'SGK Lịch sử 10 - Cánh diều', 1, false),
                                                                                     ('Lịch sử 10 Chân trời sáng tạo', 3, 'SGK Lịch sử 10 - Chân trời sáng tạo', 1, false),
-- Toán học: 2 sách
                                                                                     ('Toán 10 Kết nối tri thức', 1, 'SGK Toán 10 - Kết nối tri thức', 2, false),
                                                                                     ('Toán 10 Cánh diều', 2, 'SGK Toán 10 - Cánh diều', 2, false),
-- Ngữ văn: 2 sách
                                                                                     ('Ngữ văn 10 Kết nối tri thức', 1, 'SGK Ngữ văn 10 - Kết nối tri thức', 3, false),
                                                                                     ('Ngữ văn 10 Cánh diều', 2, 'SGK Ngữ văn 10 - Cánh diều', 3, false),
-- Các môn khác (mỗi môn 1 sách)
                                                                                     ('Tiếng Anh 10 Friends Global', 1, 'SGK Tiếng Anh 10 Friends Global', 4, false),
                                                                                     ('Địa lý 10', 1, 'SGK Địa lý 10', 5, false),
                                                                                     ('Vật lý 10', 1, 'SGK Vật lý 10', 6, false);

-- CHAPTERS (id tự tăng, mapping material_id theo thứ tự insert)
-- Lịch sử 10 Kết nối tri thức (material_id=1): 3 chương
INSERT INTO chapters (material_id, title, order_number, description, is_deleted) VALUES
                                                                                     (1, 'Lịch sử thế giới cận đại', 1, 'Chương 1: Lịch sử thế giới cận đại', false),
                                                                                     (1, 'Lịch sử Việt Nam 1858-1918', 2, 'Chương 2: Lịch sử Việt Nam 1858-1918', false),
                                                                                     (1, 'Cách mạng tháng Tám 1945', 3, 'Chương 3: Cách mạng tháng Tám', false),
-- Lịch sử 10 Cánh diều (material_id=2): 2 chương
                                                                                     (2, 'Lịch sử thế giới hiện đại', 1, 'Chương 1: Lịch sử thế giới hiện đại', false),
                                                                                     (2, 'Việt Nam sau 1975', 2, 'Chương 2: Việt Nam sau 1975', false),
-- Toán 10 Kết nối tri thức (material_id=4): 2 chương
                                                                                     (4, 'Hàm số bậc nhất', 1, 'Chương 1: Hàm số bậc nhất', false),
                                                                                     (4, 'Hàm số bậc hai', 2, 'Chương 2: Hàm số bậc hai', false),
-- Ngữ văn 10 Kết nối tri thức (material_id=6): 2 chương
                                                                                     (6, 'Văn học dân gian', 1, 'Chương 1: Văn học dân gian', false),
                                                                                     (6, 'Thơ hiện đại', 2, 'Chương 2: Thơ hiện đại', false);

-- LESSONS (id tự tăng, mapping chapter_id theo thứ tự insert)
INSERT INTO lessons (chapter_id, title, order_number, description, status, is_deleted) VALUES
                                                                                           (1, 'Cách mạng công nghiệp', 1, 'Bài 1: Cách mạng công nghiệp', 'PUBLISHED', false),
                                                                                           (1, 'Cách mạng tư sản Pháp', 2, 'Bài 2: Cách mạng tư sản Pháp', 'PUBLISHED', false),
                                                                                           (2, 'Phong trào Cần Vương', 1, 'Bài 1: Phong trào Cần Vương', 'PUBLISHED', false),
                                                                                           (2, 'Khởi nghĩa Yên Thế', 2, 'Bài 2: Khởi nghĩa Yên Thế', 'PUBLISHED', false),
                                                                                           (3, 'Diễn biến Cách mạng tháng Tám', 1, 'Bài 1: Diễn biến Cách mạng tháng Tám', 'PUBLISHED', false),
                                                                                           (4, 'Chiến tranh thế giới thứ nhất', 1, 'Bài 1: Chiến tranh thế giới thứ nhất', 'PUBLISHED', false),
                                                                                           (5, 'Đổi mới và hội nhập', 1, 'Bài 1: Đổi mới và hội nhập', 'PUBLISHED', false),
                                                                                           (6, 'Khái niệm hàm số bậc nhất', 1, 'Bài 1: Khái niệm hàm số bậc nhất', 'PUBLISHED', false),
                                                                                           (6, 'Đồ thị hàm số bậc nhất', 2, 'Bài 2: Đồ thị hàm số bậc nhất', 'PUBLISHED', false),
                                                                                           (7, 'Khái niệm hàm số bậc hai', 1, 'Bài 1: Khái niệm hàm số bậc hai', 'PUBLISHED', false),
                                                                                           (8, 'Truyền thuyết và cổ tích', 1, 'Bài 1: Truyền thuyết và cổ tích', 'PUBLISHED', false),
                                                                                           (8, 'Truyện ngụ ngôn', 2, 'Bài 2: Truyện ngụ ngôn', 'PUBLISHED', false),
                                                                                           (9, 'Thơ mới', 1, 'Bài 1: Thơ mới', 'PUBLISHED', false);

-- LESSON CONTENTS (id tự tăng, mapping lesson_id theo thứ tự insert)
INSERT INTO lesson_contents (lesson_id, title, order_number, content, is_deleted) VALUES
                                                                                      (1, 'Lý thuyết Cách mạng công nghiệp', 1, '# Cách mạng công nghiệp\n\nCách mạng công nghiệp là quá trình chuyển đổi từ sản xuất thủ công sang sản xuất bằng máy móc.', false),
                                                                                      (1, 'Bài tập Cách mạng công nghiệp', 2, '## Bài tập\n1. Trình bày nguyên nhân bùng nổ cách mạng công nghiệp.', false),
                                                                                      (2, 'Lý thuyết Cách mạng tư sản Pháp', 1, '# Cách mạng tư sản Pháp\n\nCách mạng tư sản Pháp là cuộc cách mạng lật đổ chế độ phong kiến.', false),
                                                                                      (3, 'Lý thuyết Phong trào Cần Vương', 1, '# Phong trào Cần Vương\n\nPhong trào Cần Vương là phong trào yêu nước chống Pháp cuối thế kỷ 19.', false),
                                                                                      (4, 'Lý thuyết Khởi nghĩa Yên Thế', 1, '# Khởi nghĩa Yên Thế\n\nKhởi nghĩa Yên Thế là cuộc khởi nghĩa nông dân lớn cuối thế kỷ 19.', false),
                                                                                      (5, 'Lý thuyết Cách mạng tháng Tám', 1, '# Cách mạng tháng Tám 1945\n\nTổng khởi nghĩa tháng 8/1945 diễn ra trên toàn quốc.', false),
                                                                                      (6, 'Lý thuyết Chiến tranh thế giới thứ nhất', 1, '# Chiến tranh thế giới thứ nhất\n\nChiến tranh thế giới thứ nhất diễn ra từ 1914-1918.', false),
                                                                                      (7, 'Lý thuyết Đổi mới và hội nhập', 1, '# Đổi mới và hội nhập\n\nTừ 1986, Việt Nam thực hiện đổi mới kinh tế.', false),
                                                                                      (8, 'Lý thuyết hàm số bậc nhất', 1, '# Hàm số bậc nhất\n\nHàm số bậc nhất có dạng y = ax + b, a ≠ 0.', false),
                                                                                      (8, 'Bài tập hàm số bậc nhất', 2, '## Bài tập\n1. Vẽ đồ thị hàm số y = 2x + 1.', false),
                                                                                      (9, 'Lý thuyết đồ thị hàm số bậc nhất', 1, '# Đồ thị hàm số bậc nhất\n\nĐồ thị là đường thẳng.', false),
                                                                                      (10, 'Lý thuyết hàm số bậc hai', 1, '# Hàm số bậc hai\n\nHàm số bậc hai có dạng y = ax^2 + bx + c, a ≠ 0.', false),
                                                                                      (11, 'Lý thuyết truyền thuyết', 1, '# Truyền thuyết\n\nTruyền thuyết là thể loại tự sự dân gian.', false),
                                                                                      (12, 'Lý thuyết truyện ngụ ngôn', 1, '# Truyện ngụ ngôn\n\nTruyện ngụ ngôn là truyện kể bằng hình ảnh ẩn dụ.', false),
                                                                                      (13, 'Lý thuyết thơ mới', 1, '# Thơ mới\n\nThơ mới là phong trào thơ ca Việt Nam đầu thế kỷ 20.', false);

-- LESSON CONTENT MEDIA FILES (id tự tăng, mapping lesson_content_id theo thứ tự insert, media_file_id giả định 201-220)
INSERT INTO lesson_content_media_files (lesson_content_id, media_file_id, order_number, description) VALUES
                                                                                                         (1, 201, 1, 'Ảnh máy hơi nước'),
                                                                                                         (2, 202, 1, 'Bản đồ công nghiệp Anh'),
                                                                                                         (3, 203, 1, 'Tranh cách mạng Pháp'),
                                                                                                         (4, 204, 1, 'Ảnh vua Hàm Nghi'),
                                                                                                         (5, 205, 1, 'Ảnh quảng trường Ba Đình'),
                                                                                                         (6, 206, 1, 'Bản đồ chiến tranh thế giới'),
                                                                                                         (7, 207, 1, 'Ảnh hội nhập kinh tế'),
                                                                                                         (8, 208, 1, 'Đồ thị hàm số bậc nhất'),
                                                                                                         (9, 209, 1, 'Đồ thị hàm số bậc nhất'),
                                                                                                         (10, 210, 1, 'Đồ thị hàm số bậc hai'),
                                                                                                         (11, 211, 1, 'Tranh truyền thuyết'),
                                                                                                         (12, 212, 1, 'Tranh ngụ ngôn'),
                                                                                                         (13, 213, 1, 'Ảnh thơ mới');
