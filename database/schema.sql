-- PathForge Relational Database Schema (MySQL 8.0)
CREATE DATABASE IF NOT EXISTS pathforge_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pathforge_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS pf_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_STUDENT',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Skills Table
CREATE TABLE IF NOT EXISTS skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL,
    description TEXT
) ENGINE=InnoDB;

-- 3. Career Roles Table
CREATE TABLE IF NOT EXISTS career_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    category VARCHAR(100)
) ENGINE=InnoDB;

-- 4. Career Role Skills Mapping Table
CREATE TABLE IF NOT EXISTS career_role_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    career_role_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    required_score_percentage INT NOT NULL DEFAULT 60,
    priority_order INT DEFAULT 1,
    FOREIGN KEY (career_role_id) REFERENCES career_roles(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Student Profiles Table
CREATE TABLE IF NOT EXISTS student_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    branch VARCHAR(100) NOT NULL,
    academic_year VARCHAR(50) NOT NULL,
    semester VARCHAR(50),
    career_goal_id BIGINT,
    interests TEXT,
    current_stage VARCHAR(50) DEFAULT 'Explorer',
    stage_reason TEXT,
    FOREIGN KEY (user_id) REFERENCES pf_users(id) ON DELETE CASCADE,
    FOREIGN KEY (career_goal_id) REFERENCES career_roles(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 6. Student Skills Table
CREATE TABLE IF NOT EXISTS student_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    score_percentage INT NOT NULL DEFAULT 0,
    level_name VARCHAR(50) DEFAULT 'Beginner',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_student_skill (student_profile_id, skill_id),
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 7. Assessment Questions Table
CREATE TABLE IF NOT EXISTS assessment_questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    skill_id BIGINT NOT NULL,
    category VARCHAR(100),
    level INT NOT NULL DEFAULT 1,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option VARCHAR(10) NOT NULL,
    explanation TEXT,
    marks INT DEFAULT 10,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. Assessment Attempts Table
CREATE TABLE IF NOT EXISTS assessment_attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL,
    total_score INT DEFAULT 0,
    max_score INT DEFAULT 0,
    percentage DOUBLE DEFAULT 0.0,
    completed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 9. Roadmaps Table
CREATE TABLE IF NOT EXISTS roadmaps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    career_role_id BIGINT,
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (career_role_id) REFERENCES career_roles(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 10. Roadmap Items Table
CREATE TABLE IF NOT EXISTS roadmap_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    roadmap_id BIGINT NOT NULL,
    phase_number INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    skill_id BIGINT,
    status VARCHAR(50) DEFAULT 'NOT_STARTED',
    order_index INT DEFAULT 1,
    why_recommended TEXT,
    FOREIGN KEY (roadmap_id) REFERENCES roadmaps(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 11. Real-World Projects Table
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    problem_statement TEXT,
    difficulty_level VARCHAR(50) DEFAULT 'Intermediate',
    career_role_id BIGINT,
    why_this_project TEXT,
    FOREIGN KEY (career_role_id) REFERENCES career_roles(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 12. Project Skills Table
CREATE TABLE IF NOT EXISTS project_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 13. Student Projects Enrollment Table
CREATE TABLE IF NOT EXISTS student_projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    status VARCHAR(50) DEFAULT 'IN_PROGRESS',
    github_link VARCHAR(255),
    started_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME,
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 14. Progress Trackers Table
CREATE TABLE IF NOT EXISTS progress_trackers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL UNIQUE,
    overall_progress DOUBLE DEFAULT 0.0,
    technical_skills_progress DOUBLE DEFAULT 0.0,
    dsa_progress DOUBLE DEFAULT 0.0,
    project_progress DOUBLE DEFAULT 0.0,
    git_progress DOUBLE DEFAULT 0.0,
    interview_prep_progress DOUBLE DEFAULT 0.0,
    resume_completed BOOLEAN DEFAULT FALSE,
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE
) ENGINE=InnoDB;
