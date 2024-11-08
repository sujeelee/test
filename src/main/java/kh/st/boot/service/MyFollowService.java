package kh.st.boot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kh.st.boot.dao.SearchDAO;
import kh.st.boot.model.dto.FollowInfoDTO;
import kh.st.boot.model.vo.FollowVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyFollowService {
	
	private SearchDAO searchDao;
	
	public List<FollowVO> getFollowList(String mb_id, Criteria cri) {
		return searchDao.getFollowList(mb_id, cri);
	}

	public PageMaker getPageMaker(String type, Criteria cri, String mb_id) {
		int count = 0;
		count = searchDao.getCount(type, cri, mb_id);
		return new PageMaker(10, cri, count);
	}

	public boolean unfollow(String fo_no, String mb_id) {
		boolean res = searchDao.unfollow(fo_no, mb_id);
		if(res == true) {
			searchDao.memberFollow(fo_no, -1); //fo_no는 팔로우한 아이디
		}
		return res;
	}

	public List<FollowVO> getFollowViews(String fo_id, Criteria cri) {
		return searchDao.getFollowViews(fo_id, cri);
	}

	public FollowInfoDTO getFollowInfo(String fo_id, String mb_id) {
		FollowInfoDTO info = searchDao.getFollowInfo(fo_id);
		int fo_no = searchDao.getFollowNo(fo_id, mb_id);
		info.setFo_no(fo_no);
		return info;
	}

	public boolean follow(String fo_no, String mb_id) {
		boolean res = searchDao.follow(fo_no, mb_id);
		if(res == true) {
			searchDao.memberFollow(fo_no, 1); //fo_no는 팔로우한 아이디
		}
		return res;
	}
	
}
