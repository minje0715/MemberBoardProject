package com.its.memberboardproject.controller;

import com.its.memberboardproject.dto.MemberDTO;
import com.its.memberboardproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save-form")
    public String saveForm() {
        return "memberPages/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException {
        memberService.save(memberDTO);
        return "index";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "memberPages/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            session.setAttribute("loginId",loginResult.getId());
            session.setAttribute("loginEmail",loginResult.getMemberEmail());
            return "index";
        }else {
            return "memberPages/login";
        }
    }

    @PostMapping("/duplicateCheck") // 아이디 중복체크
    public @ResponseBody String  duplicateCheck(@RequestParam("email") String memberEmail) {
       String result = memberService.duplicateCheck(memberEmail);
       return result;
    }

    @GetMapping("/detail/{id}")
    public String findById(@PathVariable Long id, Model model) {
    MemberDTO memberDTO = memberService.findById(id);
    model.addAttribute("member", memberDTO);
    return "memberPages/myPage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/update-form/{id}")
    public String updateForm(@PathVariable Long id, Model model){
       MemberDTO memberDTO = memberService.findById(id);
       model.addAttribute("member", memberDTO);
        return "memberPages/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        System.out.println("memberDTO cc = " + memberDTO);
     memberService.update(memberDTO);
     return "redirect:/member/detail/" + memberDTO.getId();
    }

    @GetMapping("/deleteById/{id}")
    public String deleteById(@PathVariable Long id, HttpSession session) {
        memberService.deleteById(id);
        session.invalidate();
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "memberPages/admin";
    }

    @GetMapping("/memberFindAll")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);

        System.out.println("memberDTOList = " + memberDTOList);
        return "memberPages/findAll";
    }

}
