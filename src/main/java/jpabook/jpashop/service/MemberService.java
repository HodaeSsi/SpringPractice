package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor
@RequiredArgsConstructor //final이 있는 필드 변수만을 가지고 생성자 생성
public class MemberService {

    private final MemberRepository memberRepository; //파이널 키워드???
/*
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    //회원 가입
    @Transactional
    //readOnly 디폴트 값 = false
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
        //@GenerateValue > 자동으로 key에 해당하는 값(id)을 채워준다 ???
    }

    //벨리데이션?
    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName()); //동시성 문제(?) >> db에 유니크 제약조건 ㄱㄱ
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    //Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
