package com.memorylink.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
 @GetMapping({"/","/index"}) public String home(){return "index";}
 @GetMapping("/login") public String login(){return "login";}
 @GetMapping("/register") public String register(){return "register";}
 @GetMapping("/dashboard") public String dashboard(){return "dashboard";}
 @GetMapping("/student_dashboard") public String studentDashboard(){return "student_dashboard";}
 @GetMapping("/strands") public String strands(){return "strands";}
 @GetMapping("/subjects") public String subjects(){return "subjects";}
 @GetMapping("/lessons") public String lessons(){return "lessons";}
 @GetMapping("/study") public String study(){return "study";}
 @GetMapping("/flashcards") public String flashcards(){return "flashcards";}
 @GetMapping({"/quiz","/quizzes"}) public String quizzes(){return "quizzes";}
 @GetMapping("/admin_strands") public String adminStrands(){return "admin_strands";}
 @GetMapping("/admin_lessons") public String adminLessons(){return "admin_lessons";}
 @GetMapping("/admin_flashcards") public String adminFlashcards(){return "admin_flashcards";}
 @GetMapping("/admin_quizzes") public String adminQuizzes(){return "admin_quizzes";}
}
