package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    //@Rollback(false)
    //확실히 인서트문이 맨 마지막(select문 뒤에)에 나가네(DB에 반영까지 된다)
    //오 테스트에서도 application.yml/jpa.hibernate.ddl-auto=create 가 먹히네?
    //>> test/java/resources가(+리소스 하위에 application.yml이) 없어서 그랬던것!
    //>> test에 resources가 있으면 실 운영환경(main)과는 별도의 운영환경 조성이 가능하다
    //test/resources가 없는게 디폴트고, 없으면 실 운영환경과 같은 resources를 통해 동작한다.
    public void 회원가입() throws Exception {
        //왜 test/.../jpashop/MemberRepositoryTest의 오류로 작동이 저지당해야함 ???

        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        //em.flush();
        //@Rollback(false) 말고 insert문을 확인하는 법
        //롤백 어노테이션과 다르게 insert문을 확인하면서도, DB에 반영이 되지 않는다.(insert문까지 롤백)
        //+원래 em.flush()는 "영속성컨텍스트 내용을 DB에 반영하라"는 메서드이다.
        assertEquals(member, memberRepository.findOne(saveId));

        /*
        query 결과에 insert 문이 없다?
        join > save > persist
        persist는 DB에 insert까지 가지 않는다
        트랜잭션이 DB로 커밋될 때 (플러시?라는게 되면서???) insert문이 실행된다.
        test에서의 transactional 어노테이션은 최종단계에서 commit하지 않고 rollback해버린다.(test에서만)
        */
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);
        /*
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return; //테스트 성공 종료, assert만이 테스트 종료의 방법은 아니네
        }
         */ // >> @Test(expected = ~~~)로 치환 가능

        //then
        fail("예외가 발생해야 한다.");
    }
}