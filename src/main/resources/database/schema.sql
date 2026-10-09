-- PathForge Relational Database Schema (MySQL / H2 Compatible)
CREATE TABLE IF NOT EXISTS pf_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_STUDENT',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS career_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    category VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS career_role_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    career_role_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    required_score_percentage INT NOT NULL DEFAULT 60,
    priority_order INT DEFAULT 1,
    FOREIGN KEY (career_role_id) REFERENCES career_roles(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

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
);

CREATE TABLE IF NOT EXISTS student_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    score_percentage INT NOT NULL DEFAULT 0,
    level_name VARCHAR(50) DEFAULT 'Beginner',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_student_skill UNIQUE (student_profile_id, skill_id),
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

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
);

CREATE TABLE IF NOT EXISTS assessment_attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL,
    total_score INT DEFAULT 0,
    max_score INT DEFAULT 0,
    percentage DOUBLE DEFAULT 0.0,
    completed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assessment_answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option VARCHAR(50),
    is_correct BOOLEAN DEFAULT FALSE,
    marks_obtained INT DEFAULT 0,
    FOREIGN KEY (attempt_id) REFERENCES assessment_attempts(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES assessment_questions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roadmaps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    career_role_id BIGINT,
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (career_role_id) REFERENCES career_roles(id) ON DELETE SET NULL
);

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
);

CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    problem_statement TEXT,
    difficulty_level VARCHAR(50) DEFAULT 'Intermediate',
    career_role_id BIGINT,
    why_this_project TEXT,
    FOREIGN KEY (career_role_id) REFERENCES career_roles(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS project_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

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
);

CREATE TABLE IF NOT EXISTS learning_resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    topic VARCHAR(100),
    skill_id BIGINT NOT NULL,
    difficulty VARCHAR(50) DEFAULT 'Beginner',
    resource_type VARCHAR(50) NOT NULL,
    url VARCHAR(255) NOT NULL,
    duration VARCHAR(50),
    source VARCHAR(50),
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS video_summaries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT,
    video_url VARCHAR(255) NOT NULL,
    video_title VARCHAR(255) NOT NULL,
    duration VARCHAR(50),
    channel VARCHAR(100),
    summary_text TEXT,
    important_concepts TEXT,
    beginner_explanation TEXT,
    practical_examples TEXT,
    important_points TEXT,
    quiz_json TEXT,
    interview_questions_json TEXT,
    mapped_skills TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_profile_id BIGINT NOT NULL,
    skill_id BIGINT,
    total_questions INT,
    correct_answers INT,
    score_percentage DOUBLE,
    attempted_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_profile_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
);

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
);
