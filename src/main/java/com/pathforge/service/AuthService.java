package com.pathforge.service;

import com.pathforge.config.JwtTokenProvider;
import com.pathforge.dto.AuthRequest;
import com.pathforge.dto.AuthResponse;
import com.pathforge.dto.RegisterRequest;
import com.pathforge.model.*;
import com.pathforge.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CareerRoleRepository careerRoleRepository;
    private final ProgressTrackerRepository progressTrackerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, StudentProfileRepository studentProfileRepository,
                       CareerRoleRepository careerRoleRepository, ProgressTrackerRepository progressTrackerRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.careerRoleRepository = careerRoleRepository;
        this.progressTrackerRepository = progressTrackerRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email address is already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_STUDENT);
        user = userRepository.save(user);

        CareerRole careerGoal = null;
        if (request.getCareerGoalId() != null) {
            careerGoal = careerRoleRepository.findById(request.getCareerGoalId()).orElse(null);
        } else {
            // Default to Backend Developer if available
            careerGoal = careerRoleRepository.findByTitleIgnoreCase("Backend Developer").orElse(null);
        }

        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setBranch(request.getBranch());
        profile.setYear(request.getYear());
        profile.setSemester(request.getSemester() != null ? request.getSemester() : "Semester 5");
        profile.setCareerGoal(careerGoal);
        profile.setCurrentStage("Explorer");
        profile.setStageReason("Initial registration complete. Take your diagnostic skill assessment to determine your technical stage.");
        profile = studentProfileRepository.save(profile);

        // Initialize progress tracker
        ProgressTracker progressTracker = new ProgressTracker(profile);
        progressTrackerRepository.save(progressTracker);

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name(), profile.getId());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        StudentProfile profile = studentProfileRepository.findByUser(user).orElse(null);
        Long profileId = profile != null ? profile.getId() : null;

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name(), profileId);
    }
}
