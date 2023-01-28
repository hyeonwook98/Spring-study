package study.querydsl.repository;

import org.springframework.data.domain.Page;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

import java.awt.print.Pageable;
import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, org.springframework.data.domain.Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, org.springframework.data.domain.Pageable pageable);

}
