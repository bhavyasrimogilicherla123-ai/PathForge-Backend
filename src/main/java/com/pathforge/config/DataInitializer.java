package com.pathforge.config;

import com.pathforge.model.*;
import com.pathforge.repository.*;
import com.pathforge.service.ProgressTrackingService;
import com.pathforge.service.RoadmapEngineService;
import com.pathforge.service.StageClassificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final SkillRepository skillRepository;
    private final CareerRoleRepository careerRoleRepository;
    private final CareerRoleSkillRepository careerRoleSkillRepository;
    private final AssessmentQuestionRepository questionRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final LearningResourceRepository resourceRepository;
    private final ProgressTrackerRepository progressTrackerRepository;
    private final PasswordEncoder passwordEncoder;
    private final StageClassificationService stageClassificationService;
    private final RoadmapEngineService roadmapEngineService;

    public DataInitializer(UserRepository userRepository,
                           StudentProfileRepository studentProfileRepository,
                           SkillRepository skillRepository,
                           CareerRoleRepository careerRoleRepository,
                           CareerRoleSkillRepository careerRoleSkillRepository,
                           AssessmentQuestionRepository questionRepository,
                           ProjectRepository projectRepository,
                           ProjectSkillRepository projectSkillRepository,
                           StudentSkillRepository studentSkillRepository,
                           LearningResourceRepository resourceRepository,
                           ProgressTrackerRepository progressTrackerRepository,
                           PasswordEncoder passwordEncoder,
                           StageClassificationService stageClassificationService,
                           RoadmapEngineService roadmapEngineService) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.skillRepository = skillRepository;
        this.careerRoleRepository = careerRoleRepository;
        this.careerRoleSkillRepository = careerRoleSkillRepository;
        this.questionRepository = questionRepository;
        this.projectRepository = projectRepository;
        this.projectSkillRepository = projectSkillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.resourceRepository = resourceRepository;
        this.progressTrackerRepository = progressTrackerRepository;
        this.passwordEncoder = passwordEncoder;
        this.stageClassificationService = stageClassificationService;
        this.roadmapEngineService = roadmapEngineService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            if (skillRepository.count() > 0) {
                return; // Data already seeded
            }
        } catch(Exception e) {
            // Table creation might still be initializing
        }

        // 1. Seed Skills
        Map<String, Skill> skillsMap = new HashMap<>();
        String[][] skillsData = {
                {"Java", "Programming", "Core Java, OOP principles, collection framework, multithreading, and JVM execution."},
                {"Python", "Programming", "Python fundamentals, data structures, list comprehensions, object-oriented programming, and scripts."},
                {"C", "Programming", "Procedural programming, memory management, pointers, and low-level computer architecture."},
                {"JavaScript", "Programming", "Modern ES6+ syntax, asynchronous promises, DOM manipulation, and frontend events."},
                {"SQL", "Database", "Relational querying, JOINs, subqueries, indexing, aggregates, and data definition language."},
                {"DBMS", "Database", "Database normalization, ACID transactions, ER modeling, indexing, and concurrency control."},
                {"DSA", "Core CS", "Data Structures (Arrays, Trees, Graphs) and Algorithms (Sorting, Dynamic Programming)."},
                {"Git", "Tools", "Distributed version control, branching, merging, pull requests, and commit history tracking."},
                {"GitHub", "Tools", "Remote repository hosting, code collaboration, pull requests, and GitHub Actions CI/CD."},
                {"Spring Boot", "Framework", "Enterprise Java framework for production-grade REST APIs, dependency injection, and JPA."},
                {"REST API", "Framework", "Representational State Transfer protocol, HTTP verbs, JSON payloads, and API status codes."},
                {"JPA/Hibernate", "Framework", "Java Persistence API Object-Relational Mapping (ORM) and entity relationships."},
                {"HTML", "Frontend", "HTML5 semantic markup, structure, forms, and browser validation."},
                {"CSS", "Frontend", "CSS3 styling, Flexbox, Grid layouts, responsive media queries, and animations."},
                {"React", "Frontend", "Component-driven frontend development, state management, hooks, and virtual DOM."},
                {"Cloud", "Infrastructure", "Cloud platform services (AWS/GCP), EC2, S3, IAM, and deployment pipelines."},
                {"AI/ML", "Specialization", "Machine learning algorithms, neural networks, supervised learning, and scikit-learn."},
                {"Cyber Security", "Specialization", "Network security, cryptography, OWASP top 10 vulnerabilities, and ethical hacking."}
        };

        for (String[] s : skillsData) {
            Skill skill = skillRepository.save(new Skill(s[0], s[1], s[2]));
            skillsMap.put(s[0], skill);
        }

        // 2. Seed Career Roles
        Map<String, CareerRole> rolesMap = new HashMap<>();
        String[][] rolesData = {
                {"Backend Developer", "Designs server-side architecture, REST APIs, microservices, and databases.", "Software Engineering"},
                {"Full Stack Developer", "Builds end-to-end web applications handling both frontend interfaces and backend APIs.", "Software Engineering"},
                {"Data Analyst", "Extracts insights from structured data using SQL, statistical tools, and data visualization.", "Data Science"},
                {"AI/ML Engineer", "Builds predictive machine learning models, neural networks, and AI-driven solutions.", "Artificial Intelligence"},
                {"Cyber Security Analyst", "Protects IT infrastructure, performs security audits, penetration testing, and threat analysis.", "Security"}
        };

        for (String[] r : rolesData) {
            CareerRole role = careerRoleRepository.save(new CareerRole(r[0], r[1], r[2]));
            rolesMap.put(r[0], role);
        }

        // 3. Map Skills to Backend Developer Career Role (Requirement #5)
        CareerRole backendRole = rolesMap.get("Backend Developer");
        Object[][] backendSkillsData = {
                {skillsMap.get("Java"), 60, 1},
                {skillsMap.get("SQL"), 60, 2},
                {skillsMap.get("DBMS"), 60, 3},
                {skillsMap.get("DSA"), 60, 4},
                {skillsMap.get("Git"), 50, 5},
                {skillsMap.get("REST API"), 60, 6},
                {skillsMap.get("Spring Boot"), 60, 7},
                {skillsMap.get("JPA/Hibernate"), 50, 8}
        };

        for (Object[] row : backendSkillsData) {
            careerRoleSkillRepository.save(new CareerRoleSkill(backendRole, (Skill) row[0], (Integer) row[1], (Integer) row[2]));
        }

        // 4. Seed Assessment Questions (Levels 1-4, Requirement #6)
        List<AssessmentQuestion> questions = new ArrayList<>();

        // Level 1 Basic MCQs
        questions.add(new AssessmentQuestion(skillsMap.get("Java"), "Programming", 1,
                "Which keyword is used to prevent method overriding in Java?",
                "static", "final", "abstract", "private", "B",
                "The `final` keyword applied to a method prevents child classes from overriding it.", 10));

        questions.add(new AssessmentQuestion(skillsMap.get("SQL"), "Database", 1,
                "Which SQL clause is used to filter records returned by a GROUP BY query?",
                "WHERE", "HAVING", "FILTER", "ORDER BY", "B",
                "The `HAVING` clause filters aggregated data created by `GROUP BY`.", 10));

        // Level 2 Intermediate Conceptual
        questions.add(new AssessmentQuestion(skillsMap.get("DSA"), "Core CS", 2,
                "What is the average time complexity of searching for an element in a Balanced Binary Search Tree (AVL / Red-Black)?",
                "O(1)", "O(N)", "O(log N)", "O(N log N)", "C",
                "Balanced BSTs maintain height h = log N, giving O(log N) lookup time complexity.", 10));

        questions.add(new AssessmentQuestion(skillsMap.get("Git"), "Tools", 2,
                "Which command creates a new local git branch named 'feature-auth' and switches to it in one step?",
                "git branch feature-auth", "git checkout -b feature-auth", "git switch -c feature-auth", "Both B and C", "D",
                "Both `git checkout -b` and modern `git switch -c` create and switch branches in a single command.", 10));

        // Level 3 Code/Output Questions
        questions.add(new AssessmentQuestion(skillsMap.get("Spring Boot"), "Framework", 3,
                "What will happen when accessing an endpoint defined as `@GetMapping('/user/{id}') public User getUser(@PathVariable Long id)` with request GET /user/42?",
                "Returns 404 error", "Binds path variable '42' to Java parameter id as 42L", "Throws ClassCastException", "Requires `@RequestParam` annotation", "B",
                "`@PathVariable` automatically parses the URI template variable `{id}` into the Java `Long id` parameter.", 10));

        // Level 4 Scenario-based Questions
        questions.add(new AssessmentQuestion(skillsMap.get("REST API"), "Framework", 4,
                "Scenario: You are designing a REST API where a client submits payment info. If the payment succeeds, a new transaction record is created. What HTTP status code should your server return?",
                "200 OK", "201 Created with Location header", "202 Accepted", "204 No Content", "B",
                "HTTP Status Code 201 Created indicates successful resource creation along with a URI reference in the Location header.", 10));

        for (AssessmentQuestion q : questions) {
            questionRepository.save(q);
        }

        // 5. Seed Real-World Projects (Requirement #15)
        Project p1 = projectRepository.save(new Project(
                "Smart Clinic Queue Management System",
                "Digital queue tracking app allowing patients to check real-time doctor availability, book appointments, and receive automated SMS/notification status updates.",
                "Patients spend hours waiting in hospital waiting rooms without real-time token tracking.",
                "Intermediate",
                backendRole,
                "Because: You already know Java and SQL, but need practical Spring Boot & REST API experience. This project bridges your backend fundamentals to real-world software architecture."
        ));
        projectSkillRepository.save(new ProjectSkill(p1, skillsMap.get("Java")));
        projectSkillRepository.save(new ProjectSkill(p1, skillsMap.get("Spring Boot")));
        projectSkillRepository.save(new ProjectSkill(p1, skillsMap.get("REST API")));
        projectSkillRepository.save(new ProjectSkill(p1, skillsMap.get("SQL")));

        Project p2 = projectRepository.save(new Project(
                "College Complaint Escalation & Governance Platform",
                "Workflow management platform for B.Tech colleges to submit grievance tickets, auto-assign faculty, track SLA deadlines, and escalate unresolved complaints.",
                "Manual paper complaints get lost and lack transparent resolution timelines.",
                "Intermediate",
                backendRole,
                "Because: You need hands-on experience with database relational design, state machine workflows, and automated email/notification APIs."
        ));
        projectSkillRepository.save(new ProjectSkill(p2, skillsMap.get("Java")));
        projectSkillRepository.save(new ProjectSkill(p2, skillsMap.get("Spring Boot")));
        projectSkillRepository.save(new ProjectSkill(p2, skillsMap.get("SQL")));
        projectSkillRepository.save(new ProjectSkill(p2, skillsMap.get("Git")));

        Project p3 = projectRepository.save(new Project(
                "Student Peer Skill Exchange & Mentorship Network",
                "Peer-to-peer platform connecting engineering students who want to teach programming languages in exchange for learning other technical skills.",
                "Students struggle to find compatible study partners within their campus.",
                "Intermediate",
                rolesMap.get("Full Stack Developer"),
                "Because: Gives you complete full-stack experience combining JavaScript frontend dynamic UI with Spring Boot backend security."
        ));
        projectSkillRepository.save(new ProjectSkill(p3, skillsMap.get("JavaScript")));
        projectSkillRepository.save(new ProjectSkill(p3, skillsMap.get("Spring Boot")));
        projectSkillRepository.save(new ProjectSkill(p3, skillsMap.get("SQL")));

        // 6. Seed Learning Resources (Requirement #13)
        resourceRepository.save(new LearningResource(
                "Spring Boot 3.0 & REST API Masterclass",
                "Step-by-step introduction to building REST Controllers, handling JSON requests, and handling exception responses.",
                "Spring Web", skillsMap.get("Spring Boot"), "Beginner", "Video",
                "https://www.youtube.com/watch?v=vtPkZShrvXQ", "45 mins", "YouTube"
        ));
        resourceRepository.save(new LearningResource(
                "Git & GitHub Workflow for Developers",
                "Master git init, commit, push, pull, branching, merge conflict resolution, and pull requests.",
                "Version Control", skillsMap.get("Git"), "Beginner", "Video",
                "https://www.youtube.com/watch?v=RGOj5yH7evk", "30 mins", "YouTube"
        ));

        // 7. Seed Admin User
        User adminUser = new User("Platform Admin", "admin@pathforge.com", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN);
        userRepository.save(adminUser);

        // 8. SEED DEMO STUDENT (Requirement #33)
        User demoUser = new User("Demo Student", "demo@pathforge.com", passwordEncoder.encode("password123"), Role.ROLE_STUDENT);
        demoUser = userRepository.save(demoUser);

        StudentProfile demoProfile = new StudentProfile(
                demoUser,
                "CSE",
                "3rd Year",
                "Semester 5",
                backendRole,
                "Backend Engineering, System Design, REST APIs, Microservices"
        );
        demoProfile.setCurrentStage("Foundation");
        demoProfile.setStageReason("• Programming Fundamentals: Good (70%)\n• Database & SQL: Good (65%)\n• DSA & Problem Solving: 40% (Needs Improvement)\n• Version Control (Git): 20% (Beginner)\n• Frameworks (Spring Boot / REST API): Major Gap");
        demoProfile = studentProfileRepository.save(demoProfile);

        // Initialize Demo Student baseline scores (Requirement #33)
        // Java -> 70%, Python -> 60%, SQL -> 65%, DBMS -> 60%, DSA -> 40%, Git -> 20%, Spring Boot -> 0%, REST API -> 10%
        studentSkillRepository.save(new StudentSkill(demoProfile, skillsMap.get("Java"), 70));
        studentSkillRepository.save(new StudentSkill(demoProfile, skillsMap.get("Python"), 60));
        studentSkillRepository.save(new StudentSkill(demoProfile, skillsMap.get("SQL"), 65));
        studentSkillRepository.save(new StudentSkill(demoProfile, skillsMap.get("DBMS"), 60));
        studentSkillRepository.save(new StudentSkill(demoProfile, skillsMap.get("DSA"), 40));
        studentSkillRepository.save(new StudentSkill(demoProfile, skillsMap.get("Git"), 20));
        studentSkillRepository.save(new StudentSkill(demoProfile, skillsMap.get("Spring Boot"), 0));
        studentSkillRepository.save(new StudentSkill(demoProfile, skillsMap.get("REST API"), 10));

        // Generate personalized initial roadmap for Demo Student
        stageClassificationService.evaluateAndUpdateStage(demoProfile);
        roadmapEngineService.generateNewRoadmap(demoProfile, backendRole, "Backend Developer");

        // Initialize progress tracker
        ProgressTracker demoTracker = new ProgressTracker(demoProfile);
        demoTracker.setTechnicalSkillsProgress(47.0);
        demoTracker.setDsaProgress(40.0);
        demoTracker.setProjectProgress(0.0);
        demoTracker.setGitProgress(20.0);
        demoTracker.setInterviewPrepProgress(43.5);
        demoTracker.setOverallProgress(32.8);
        progressTrackerRepository.save(demoTracker);

        System.out.println(">>> PathForge Initializer: Demo Student & Database Sample Data Successfully Seeded!");
    }
}
