package kh.st.boot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kh.st.boot.dao.AdminDAO;
import kh.st.boot.dao.MemberDAO;
import kh.st.boot.dao.SearchDAO;
import kh.st.boot.dao.SltAdmLevelPageDAO;
import kh.st.boot.dao.StockDAO;
import kh.st.boot.model.dto.ComRankDTO;
import kh.st.boot.model.dto.DashListDTO;
import kh.st.boot.model.dto.HotStockDTO;
import kh.st.boot.model.dto.SearchDTO;
import kh.st.boot.model.vo.AdminLevelPageVO;
import kh.st.boot.model.vo.AdminVO;
import kh.st.boot.model.vo.BoardVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.NewsVO;
import kh.st.boot.model.vo.StockJisuVO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfigService {
	
	private AdminDAO adminDao;
	private StockDAO stockDao;
	private SearchDAO searchDao;
	private SltAdmLevelPageDAO levelAdminDao;
	private MemberDAO memberDao;
	
	
	public AdminVO getConfig() {
		return adminDao.selectAdmin();
	}

	public List<HotStockDTO> getHotStockList(int limit) {
		List<HotStockDTO> list = stockDao.getHotStockList(limit);
		for(HotStockDTO tmp : list) {
			String amount = priceTextChange(Double.parseDouble(tmp.getMrk()));
			tmp.setPrice_text(amount);
		}
		return list;
	}
	
	public String priceTextChange(double amount) {
		String price_text = null;
		if (amount >= 1_000_000_000_000L) { // 1조 이상
		    double trillion = amount / 1_000_000_000_000L; // 조 단위로 변환
		    price_text = String.format("%.1f조원", trillion); // 소수점 첫째 자리까지 표시
		} else if (amount >= 1_000_000_000) { // 1억 이상
		    double hundredMillion = amount / 1_000_000_000; // 억 단위로 변환
		    price_text = String.format("%.1f억원", hundredMillion); // 소수점 첫째 자리까지 표시
		} else if (amount >= 1_000) { // 1천 이상
		    double thousand = amount / 1_000; // 천 단위로 변환
		    price_text = String.format("%.1f천원", thousand); // 소수점 첫째 자리까지 표시
		} else { // 그 이하
		    price_text = String.format("%.0f원", amount); // 원 단위로 표시
		}
		return price_text;
	}

	public List<DashListDTO> getDashList(Map<String, Object> param) {
		List<DashListDTO> lists = new ArrayList<DashListDTO>();
		
		if(param.get("tag").equals("mystock")) {
			lists = stockDao.getMyStock((String)param.get("mb_id"));
		} else if(param.get("tag").equals("mywish")){
			lists = stockDao.getMyWish((String)param.get("mb_id"));
		}
		
		return lists;
	}
	
	public List<StockJisuVO> jisuConfig(String type) {
		return stockDao.jisuConfig(type);
	}

	public List<SearchDTO> totalSearch(String type, String stx) {
		List<SearchDTO> list = new ArrayList<SearchDTO>();
		SimpleDateFormat formatter = new SimpleDateFormat("MM월 dd일");
		if(type.equals("stock")) {
			List<DashListDTO> stocks = searchDao.stockSearch(stx);
			if(stocks.isEmpty() == false) {
				for(DashListDTO tmps : stocks) { 
					SearchDTO newSearch = new SearchDTO();
					newSearch.setStockCnt(stocks.size());
					newSearch.setNewsCnt(0);
					newSearch.setCode(tmps.getCode());
					newSearch.setFlt(tmps.getFlt());
					newSearch.setPrice(tmps.getPrice());
					newSearch.setTitle(tmps.getStockName());
					newSearch.setType("stock");
					list.add(newSearch);
				}
			} else {
				SearchDTO newSearch = new SearchDTO();
				newSearch.setNewsCnt(0);
				newSearch.setStockCnt(0);
				list.add(newSearch);
			}
		} else if(type.equals("news")) {
			List<NewsVO> news = searchDao.newsSearch(stx);
			
			if(news.isEmpty() == false) {
				for(NewsVO tmps : news) { 
					SearchDTO newSearch = new SearchDTO();
					newSearch.setNewsCnt(news.size());
					newSearch.setStockCnt(0);
					newSearch.setCode(Integer.toString(tmps.getNe_no()));
					newSearch.setTitle(tmps.getNe_title());
					newSearch.setDate(formatter.format(tmps.getNe_datetime()));
					newSearch.setNewspaper(tmps.getNp_name());
					newSearch.setType("news");
					list.add(newSearch);
				}
			} else {
				SearchDTO newSearch = new SearchDTO();
				newSearch.setNewsCnt(0);
				newSearch.setStockCnt(0);
				list.add(newSearch);
			}
		} else {
			List<DashListDTO> stocks = searchDao.stockSearch(stx);
			List<NewsVO> news = searchDao.newsSearch(stx);
			
			if(stocks.isEmpty() == false) {
				for(DashListDTO tmps : stocks) { 
					SearchDTO newSearch = new SearchDTO();
					newSearch.setStockCnt(stocks.size());
					newSearch.setCode(tmps.getCode());
					newSearch.setFlt(tmps.getFlt());
					newSearch.setPrice(tmps.getPrice());
					newSearch.setTitle(tmps.getStockName());
					newSearch.setType("stock");
					list.add(newSearch);
				}
			} 
			if(news.isEmpty() == false) {
				for(NewsVO tmps : news) { 
					SearchDTO newSearch = new SearchDTO();
					newSearch.setNewsCnt(news.size());
					newSearch.setCode(Integer.toString(tmps.getNe_no()));
					newSearch.setTitle(tmps.getNe_title());
					newSearch.setDate(formatter.format(tmps.getNe_datetime()));
					newSearch.setNewspaper(tmps.getNp_name());
					newSearch.setType("news");
					list.add(newSearch);
				}
			}
			SearchDTO newSearch = new SearchDTO();
			int newsCnt = 0, stockCnt = 0;
			if(news.isEmpty() == false) {
				newsCnt = news.size();
			}
			if (stocks.isEmpty() == false) {
				stockCnt = stocks.size();
			}
			newSearch.setNewsCnt(stockCnt);
			newSearch.setStockCnt(newsCnt);
			list.add(newSearch);
		}
		
		return list;
	}

	public List<ComRankDTO> getCommunityRank() {
		List<ComRankDTO> list = searchDao.getCommunityRank();
		
		for(ComRankDTO tmp : list) {
			tmp.setFlt(searchDao.getFlt(tmp.getCode()));
		}
		
		return list;
	}

	public List<BoardVO> getCommunityList(String code) {
		List<BoardVO> list = searchDao.getCommunityList(code);
		for(BoardVO tmp : list) {
			String txt = getLvTxt(tmp.getMb_level());
			tmp.setLv_txt(txt);
		}
		return list;
	}

	public MemberVO getConfigLv(MemberVO member) {
		int orgMbLv = member.getMb_level(); //기존 레벨 먼저 가져올게요
		int followCnt = member.getMb_follow(); //회원을 팔로우하고 있는 회원 수를 가져올게요
		List<AdminLevelPageVO> lvConfig = levelAdminDao.getAllssltAdminLevelPage(); //일단 모든 레벨을 가져옵니다.
		int newMbLv = member.getMb_level();
		
		member.setLv_txt(lvConfig.get(orgMbLv - 1).getLv_name());
		
		if(orgMbLv > 6) {
			return member;
		}
		
		for(AdminLevelPageVO tmp : lvConfig) {
			if(tmp.getLv_auto_use().equals("N")) {
				if(followCnt <= tmp.getLv_up_limit()) {
					newMbLv = tmp.getLv_num();
					break;
				}
			}
		}
		
		if(orgMbLv != newMbLv) {
				member.setMb_level(newMbLv);
				member.setLv_txt(lvConfig.get(newMbLv - 1).getLv_name());
			if(memberDao.updateLevel(member) == false) {
				member.setMb_level(orgMbLv);
				member.setLv_txt(lvConfig.get(orgMbLv - 1).getLv_name());
			}
		}  
		return member;
	}
	
	public String getLvTxt(int level) {
		String txt = "게스트";
		
		List<AdminLevelPageVO> lvConfig = levelAdminDao.getAllssltAdminLevelPage(); //일단 모든 레벨을 가져옵니다.
		
		txt = lvConfig.get(level - 1).getLv_name();
		
		return txt;
	}
	
}
