package kh.st.boot.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.AdminUserDAO;
import kh.st.boot.model.dto.AdMemberCheckDTO;
import kh.st.boot.model.vo.AdmMemberVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.pagination.UserCriteria;

@Service
public class AdminUserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AdminUserDAO adminuserDAO;

	public List<AdmMemberVO> getAdminMem(Criteria cri) {
		return adminuserDAO.selectAdmUser(cri);
	}

	public PageMaker getPageMaker(Criteria cri) {
		int count = adminuserDAO.selectCountList(cri);
		return new PageMaker(10, cri, count);
	}

	public AdmMemberVO getAdmUseSel(int mb_no) {
		return adminuserDAO.UseSelect(mb_no);
	}

	public boolean getAdmUserUpd(AdmMemberVO admMemberVO) {
		AdmMemberVO existingMember = getAdmUseSel(admMemberVO.getMb_no());
	    int emailing = admMemberVO.getMb_emailing_test() != null ? 1 : 0;
	    String encodePw;
	    // 입력된 비밀번호와 기존 비밀번호를 비교
	    if (!admMemberVO.getMb_password().equals(existingMember.getMb_password())) {
	        // 기존 비밀번호와 다르면 인코딩 진행
	        encodePw = passwordEncoder.encode(admMemberVO.getMb_password());
	    } else {
	        // 기존 비밀번호와 같으면 그대로 사용
	        encodePw = admMemberVO.getMb_password();
	    }
		/*
		int emailing;
		if(admMemberVO.getMb_emailing_test() != null) {
			emailing = 1;
		}else {
			emailing = 0;
		}
		String encodePw = passwordEncoder.encode(admMemberVO.getMb_password());
		*/
		admMemberVO.setMb_emailing(emailing);
		admMemberVO.setMb_password(encodePw);
		adminuserDAO.UseUpdate(admMemberVO);
		return true;
	}


	public boolean getAdmUseDel(int mb_no) {
		int result = adminuserDAO.UserDelete(mb_no);
		return result > 0; // 1 이상의 값을 반환하면 삭제 성공
	}

    // 페이지네이션이 적용된 검색 결과 가져오기
    public List<AdmMemberVO> getSearchUser(String use_sh, UserCriteria cri) {
    	
    	
        return adminuserDAO.selectUser(use_sh, cri);
    }

    // 검색 결과에 따른 PageMaker 생성
    public PageMaker getPageMakerSearch(UserCriteria cri, String use_sh) {
        int totalCount = adminuserDAO.selectUserCount(use_sh, cri); // 검색된 전체 결과 수 반환
        return new PageMaker(10, cri, totalCount); // PageMaker 생성 (displayPageNum을 10으로 설정)
    }

    // 회원정보 등록
	public boolean getAdmUserIns(AdmMemberVO admMemberVO) {
		String encodePw = passwordEncoder.encode(admMemberVO.getMb_password());
		int emailing;
		if(admMemberVO.getMb_emailing_test() != null) {
			emailing = 1;
		}else {
			emailing = 0;
		}
		admMemberVO.setMb_emailing(emailing);
		admMemberVO.setMb_password(encodePw);
		adminuserDAO.UserInsert(admMemberVO);
		return true;
	}

	public int getAdmMemberCheck(String mb_id) {
		return adminuserDAO.MemberCheck(mb_id);
	}

	
}

