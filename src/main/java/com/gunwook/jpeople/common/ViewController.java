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


}
