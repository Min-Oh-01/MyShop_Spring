package trade.trade.member;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import trade.trade.FileService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FileService fileService;

    // 회원가입 페이지
    @GetMapping("memberSignUp")
    public String memberSignUp() {
        return "member/memberSignUp";
    }

    // ***회원가입 진행***
    @PostMapping("/memberSignUp")
    String memberSignUp(@ModelAttribute MemberCreateDTO memberCreateDTO, Model model) throws IOException {
        try {
            memberService.signUp(memberCreateDTO);
            return "redirect:/memberLogin";
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();

            // 각 필드별로 고정된 오류 메시지 설정
            if (errorMessage.contains("이메일")) {
                model.addAttribute("emailError", "이미 사용 중인 이메일입니다.");
            } else if (errorMessage.contains("비밀번호는 최소")) {
                model.addAttribute("passwordError", "비밀번호는 8자 이상 입력해야 합니다.");
            } else if (errorMessage.contains("비밀번호가 일치")) {
                model.addAttribute("confirmPasswordError", "입력한 비밀번호가 서로 다릅니다.");
            } else {
                model.addAttribute("generalError", "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요.");
            }

            return "member/memberSignUp";
        } catch (IOException e) {
            model.addAttribute("error", "파일 저장 중 오류 발생");
            return "member/memberSignUp";
        }
    }

    // 유저 로그인
    @GetMapping("/memberLogin")
    String memberLogin() {
        return "member/memberLogin";
    }

    // 로그인 이후 마이페이지 접속 가능
    @GetMapping("/memberPage")
    String memberPage(@AuthenticationPrincipal User user, Model model) {
        Optional<MemberDTO> member = memberService.findMemberByEmail(user.getUsername());

        if (member.isPresent()) {
            model.addAttribute("Member", member.get());
            return "member/memberPage";
        } else {
            System.out.println("회원 정보 없음");
            return "redirect:/memberLogin";
        }
    }

    // 마이페이지 수정
    @GetMapping("/memberEdit")
    String memberEdit(@AuthenticationPrincipal User user, Model model) {
        Optional<MemberDTO> member = memberService.findMemberByEmail(user.getUsername());
        if (member.isPresent()) {
            model.addAttribute("Member", member.get());
            return "member/memberEdit";
        } else {
            return "redirect:/home";
        }
    }

    // 마이페이지 수정 데이터 저장
    @PostMapping("/memberEdit")
    String memberEdit(@ModelAttribute MemberUpdateDTO memberUpdateDTO) {
        memberService.memberUpdate(memberUpdateDTO);
        return "redirect:/memberPage";
    }

    // 마이페이지 삭제 버튼
    @PostMapping("/memberDelete")
    String memberDelete(@ModelAttribute MemberDeleteDTO memberDeleteDTO) {
        memberService.memberDelete(memberDeleteDTO);
        return "redirect:/home";
    }
}
