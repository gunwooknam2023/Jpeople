package com.gunwook.jpeople.common;

import com.gunwook.jpeople.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/view")
public class ViewController {
    @GetMapping("/notification")
    public String notificationPage(){
        return "notification";
    }

    @GetMapping("/board")
    public String boardPage(){
        return "board";
    }

    @GetMapping("/schedule")
    public String schedulePage(){
        return "schedule";
    }

    @GetMapping("/mypage")
    public String mypagePage(){
        return "mypage";
    }

    @GetMapping("/boarddetail")
    public String boardDetail(){
        return "boarddetail";
    }

    @GetMapping("/createboard")
    public String createboard(){
        return "createboard";
    }

    @GetMapping("/adminpage")
    public String adminpage(){
        return "adminpage";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/calculator")
    public String calculator(){
        return "calculator";
    }

    @GetMapping("/updateboard")
    public String updateboard(){
        return "updateboard";
    }

    @GetMapping("/chatgpt")
    public String chatgpt(){
        return "chatgpt";
    }
    @GetMapping("/paintboard")
    public String paintboard(){
        return "paintboard";
    }
}
