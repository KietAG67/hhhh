CREATE DATABASE IF NOT EXISTS student_management
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE student_management;

CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_code VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    major VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
               ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_student_code (student_code),
    INDEX idx_email (email)
) ENGINE=InnoDB;

INSERT IGNORE INTO students (student_code, full_name, email, major) VALUES
('ITITIU21003', 'Bui Van Phuc', 'buivanphuc@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21004', 'Vo Thi Ha', 'vothiha@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21005', 'Do Thi Nga', 'dothinga@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21006', 'Duong Thi Thao', 'duongthithao@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21007', 'Cao Thi Yen', 'caothiyen@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21008', 'Trinh Thi Oanh', 'trinhthioanh@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21009', 'Phan Minh Triet', 'phanminhtriet@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21010', 'Nguyen Hoang Long', 'nguyenhoanglong@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21011', 'Tran Gia Huy', 'trangiahuy@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21012', 'Le Khanh Vy', 'lekhanhvy@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21013', 'Pham Quynh Anh', 'phamquynhanh@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21014', 'Hoang Duc Thinh', 'hoangducthinh@student.hcmiu.edu.vn', 'Information Technology'),
('ITITIU21015', 'Bui Ngoc Mai', 'buingocmai@student.hcmiu.edu.vn', 'Information Technology'),
('ITCSIU21002', 'Dang Van Khoa', 'dangvankhoa@student.hcmiu.edu.vn', 'Computer Science'),
('ITCSIU21003', 'Phan Van Phong', 'phanvanphong@student.hcmiu.edu.vn', 'Computer Science'),
('ITCSIU21004', 'Hoang Van Tuan', 'hoangvantuan@student.hcmiu.edu.vn', 'Computer Science'),
('ITCSIU21005', 'Dinh Van Vinh', 'dinhvanvinh@student.hcmiu.edu.vn', 'Computer Science'),
('ITCSIU21006', 'Doan Van Phuc', 'doanvanphuc@student.hcmiu.edu.vn', 'Computer Science'),
('ITCSIU21007', 'Ngo Gia Bao', 'ngogiabao@student.hcmiu.edu.vn', 'Computer Science'),
('ITCSIU21008', 'Vuong Quoc Bao', 'vuongquocbao@student.hcmiu.edu.vn', 'Computer Science'),
('ITDSIU21002', 'Bui Thi Lan', 'buithilan@student.hcmiu.edu.vn', 'Data Science'),
('ITDSIU21003', 'Vu Thi Quynh', 'vuthiquynh@student.hcmiu.edu.vn', 'Data Science'),
('ITDSIU21004', 'Lam Thi Vy', 'lamthivy@student.hcmiu.edu.vn', 'Data Science'),
('ITDSIU21005', 'Ly Thi My', 'lythimy@student.hcmiu.edu.vn', 'Data Science'),
('ITDSIU21006', 'Huynh Thi Ha', 'huynhthiha@student.hcmiu.edu.vn', 'Data Science'),
('ITSEIU21002', 'Ngo Van Minh', 'ngovanminh@student.hcmiu.edu.vn', 'Software Engineering'),
('ITSEIU21003', 'Truong Van Son', 'truongvanson@student.hcmiu.edu.vn', 'Software Engineering'),
('ITSEIU21004', 'Mai Van Vuong', 'maivanvuong@student.hcmiu.edu.vn', 'Software Engineering'),
('ITSEIU21005', 'Phung Van Nam', 'phungvannam@student.hcmiu.edu.vn', 'Software Engineering'),
('ITSEIU21006', 'Luong Van Khoa', 'luongvankhoa@student.hcmiu.edu.vn', 'Software Engineering');
