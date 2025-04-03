package trade.trade.member;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trade.trade.FileService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    // 이메일 중복 확인
    boolean memberFindByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    // 회원가입 등록
//    void signUp(Member member) {
//        member.setPassword(passwordEncoder.encode(member.getPassword()));
//        memberRepository.save(member);
//    }

    void signUp(MemberCreateDTO memberCreateDTO) throws IOException {
        // 이메일 중복 검사
        if (memberRepository.findByEmail(memberCreateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 검증
        if (memberCreateDTO.getPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
        }

        // 비밀번호 일치 여부 확인
        if (!memberCreateDTO.getPassword().equals(memberCreateDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 휴대폰 번호, 숫자만 입력
//        if (!memberCreateDTO.getPhoneNumber().matches("\\d+"))

        String imagePath;

        // 이미지가 null 이거나 비어있다면 기본 이미지 경로 설정
        if (memberCreateDTO.getUserImage() != null && !memberCreateDTO.getUserImage().isEmpty()) {
            imagePath = fileService.imageSave(memberCreateDTO.getUserImage());
        } else {
            String[] defaultImages = {"/images/icon1.png", "/images/icon2.png"};

            // 랜덤으로 이미지 선택
            Random random = new Random();
            imagePath = defaultImages[random.nextInt(defaultImages.length)];
        }

        // 비밀번호 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(memberCreateDTO.getPassword());

        Member member = new Member();

        member.setUserImage(imagePath);
        member.setName(memberCreateDTO.getName());
        member.setPassword(encryptedPassword); // 암호화
        member.setEmail(memberCreateDTO.getEmail());
        member.setDescription(memberCreateDTO.getDescription());
        member.setPhoneNumber(memberCreateDTO.getPhoneNumber());
        member.setJoinDate(memberCreateDTO.getJoinDate());

        memberRepository.save(member);
    }

    // 이메일로 해당 사용자의 마이페이지 이동
    Optional<MemberDTO> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).map(member -> {
            MemberDTO memberDTO = new MemberDTO();

            memberDTO.setId(member.getId());
            memberDTO.setEmail(member.getEmail());
            memberDTO.setPassword(member.getPassword());
            memberDTO.setName(member.getName());
            memberDTO.setUserImage(member.getUserImage());
            memberDTO.setDescription(member.getDescription());
            memberDTO.setPhoneNumber(member.getPhoneNumber());
            memberDTO.setJoinDate(member.getJoinDate());

            return memberDTO;
        });
    }

//    회원정보 수정
    void memberUpdate(MemberUpdateDTO memberUpdateDTO) {
        Member member = memberRepository.findByEmail(memberUpdateDTO.getEmail())
                .orElseThrow(()-> new NoSuchElementException("회원 정보가 존재하지 않습니다."));

        String imagePath = memberUpdateDTO.getOriginalImage();

        if (memberUpdateDTO.getUserImage() != null && !memberUpdateDTO.getUserImage().isEmpty()) {
            try {
                fileService.fileDelete(String.valueOf(Paths.get(memberUpdateDTO.getOriginalImage()).getFileName()));
                imagePath = fileService.imageSave(memberUpdateDTO.getUserImage());
            } catch (IOException e) {
                throw new RuntimeException("이미지 처리 중 오류 발생", e);
            }
        }

        member.setUserImage(imagePath);
        member.setEmail(memberUpdateDTO.getEmail());
        member.setPassword(memberUpdateDTO.getPassword());
        member.setDescription(memberUpdateDTO.getDescription());
        member.setPhoneNumber(memberUpdateDTO.getPhoneNumber());
        member.setName(memberUpdateDTO.getName());

        memberRepository.save(member);
    }

    // 회원 탈퇴
    void memberDelete(MemberDeleteDTO memberDeleteDTO) {
        Member member = memberRepository.findById(memberDeleteDTO.getId())
                .orElseThrow(()-> new NoSuchElementException("해당 회원이 존재하지 않습니다."));
        String imagePath = member.getUserImage();
        if (imagePath != null && imagePath.startsWith("/upload/images/")) {
            String fileName = Paths.get(imagePath).getFileName().toString();
            try {
                fileService.fileDelete(fileName);
            } catch (IOException e) {
                throw new RuntimeException("이미지 삭제 중 오류 발생", e);
            }
        }
        memberRepository.deleteById(member.getId());
    }
}
