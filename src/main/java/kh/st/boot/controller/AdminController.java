package kh.st.boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import groovy.transform.AutoImplement;
import kh.st.boot.model.dto.AdMemberCheckDTO;
import kh.st.boot.model.vo.AdmApprovalVO;
import kh.st.boot.model.vo.AdmDaycheckVO;
import kh.st.boot.model.vo.AdmMemberVO;
import kh.st.boot.model.vo.AdmPointVO;
import kh.st.boot.model.vo.AdminLevelPageVO;
import kh.st.boot.model.vo.AdminStock_addVO;
import kh.st.boot.model.vo.AdminVO;
import kh.st.boot.model.vo.MemberVO;
import kh.st.boot.model.vo.NewsPaperVO;
import kh.st.boot.model.vo.admOrderPageVO;
import kh.st.boot.pagination.AdmApprovalCriteria;
import kh.st.boot.pagination.AdmDayCheckCriteria;
import kh.st.boot.pagination.AdmNewsCriteria;
import kh.st.boot.pagination.AdmPointCriteria;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.OrderCriteria;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.pagination.UserCriteria;
import kh.st.boot.service.AdmPointService;
import kh.st.boot.service.AdminApprovalService;
import kh.st.boot.service.AdminOrderService;
import kh.st.boot.service.AdminService;
import kh.st.boot.service.AdminStock_addService;
import kh.st.boot.service.AdminUserService;
import kh.st.boot.service.MemberService;
import kh.st.boot.service.PointSltIdPageService;
import kh.st.boot.service.SltAdmLevelPageService;
import kh.st.boot.service.newspaperService;

@Controller
@AutoImplement
@RequestMapping("/admin") // 기본 경로 설정
public class AdminController {

	// 의존성 추가?
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private AdminUserService admUserService;

	@Autowired
	private newspaperService newspaperService;

	@Autowired
	private SltAdmLevelPageService sltAdmLevelPageService;

	@Autowired
	private AdminOrderService adminOrderService;

	@Autowired
	private PointSltIdPageService pointSltIdPageService;

	@Autowired
	private AdminApprovalService adminApprovalService;

	@Autowired
	private AdmPointService admPointService;

	@Autowired
	private AdminStock_addService adminStock_addService;

	// hh
	// 관리자 기본 페이지
	// 관리자 설정 페이지 값 전송 코드
	@GetMapping("/adminHome")
	public String admin(Model model) {
		// db에 저장된 값 DAO와 Service를 통해 받아온 값 리스트에 저장
		AdminVO adminH = adminService.getAdminH();
		// 값을 보내주기
		model.addAttribute("adminH", adminH);
		return "/admin/adminHome";
	}

	// - hh
	// 관리자 설정 페이지 값 변경
	@PostMapping("/adminHome/update")
	public String admUpdate(AdminVO adminVO, Model model) {
		System.out.println("페이지 값 받아오기" + adminVO);
		boolean res = adminService.admUpdate(adminVO);
		if (res == false) {
			model.addAttribute("msg", "실패");
			model.addAttribute("url", "/admin/adminHome");
			return "util/msg";
		}

		return "redirect:/admin/adminHome";
	}

	// -------------------------------------------------------------------------------
	// -----------------------------    회원 정보 수정 -------------------------------
	// -------------------------------------------------------------------------------

	// - hh
	@GetMapping("/admMember/adminUser")
	public String admUser(Model model, Criteria cri) {
		cri.setPerPageNum(12);
		List<AdmMemberVO> user = admUserService.getAdminMem(cri);
		PageMaker pm_use = admUserService.getPageMaker(cri);
		model.addAttribute("user", user);
		model.addAttribute("pm_use", pm_use);

		return "/admin/admMember/adminUser";
	}

	// - hh
	// 회원 정보 상세페이지 조회
	@PostMapping("/admMember/admUserSelect")
	public String admUserSelUpd(Model model, int mb_no) {
		AdmMemberVO admUseSel = admUserService.getAdmUseSel(mb_no);

		model.addAttribute("admUseSel", admUseSel);

		return "/admin/admMember/admUserSelect";
	}

	// - hh
	// 회원 정보 상세페이지 업데이트
	@PostMapping("/admMember/admUserSelect/Update")
	public String admUserUpdate(Model model, AdmMemberVO admMemberVO) {
		boolean res = admUserService.getAdmUserUpd(admMemberVO);
		if (res == false) {
			model.addAttribute("msg", "실패");
			model.addAttribute("url", "/admin/adminHome");
			return "util/msg";
		}
		return "redirect:/admin/admMember/adminUser";
	}

	// - hh
	// 회원 정보 삭제
	// 이건 잘 모르겠음
	@PostMapping("/admMember/adminUser/delete")
	public String admUserDelete(int mb_no) {
		boolean admimUserDel = admUserService.getAdmUseDel(mb_no);

		return "redirect:/admin/admMember/adminUser";
	}

	// - hh
	// 회원등록 페이지 불러오기
	@GetMapping("/admMember/admUserInsert")
	public String admUserInsertPage() {
		return "/admin/admMember/admUserInsert";
	}
	
	// -hh
	// 회원 등록 
	@PostMapping("/admMember/admUserInsert/AdminUserInsert")
	public String admUserInsert(Model model, AdmMemberVO admMemberVO) { 
		boolean res = admUserService.getAdmUserIns(admMemberVO);
		if (res == false) {
			model.addAttribute("msg", "실패");
			model.addAttribute("url", "/admin/adminHome");
			return "util/msg";
		}
		return "redirect:/admin/admMember/adminUser";
	}

	// -hh
	@GetMapping("/admMember/adminUser/userSearch")
	public String admuseSearch(@RequestParam("use_sh") String use_sh, @RequestParam("search") String search,
			@RequestParam(value = "page", defaultValue = "1") int page, Model model, UserCriteria cri) { // UserCriteria

		// 한 페이지당 게시물 숫자
		cri.setPerPageNum(12);
		// cri로 넘겨줄 페이지
		cri.setPage(page);
		// 검색 매퍼에서 사용
		cri.setSearch(search);

		List<AdmMemberVO> searchUser = admUserService.getSearchUser(use_sh, cri);

		PageMaker pm_use = admUserService.getPageMakerSearch(cri, use_sh);
		model.addAttribute("user", searchUser);
		model.addAttribute("pm_use", pm_use);
		model.addAttribute("use_sh", use_sh);
		model.addAttribute("search", search);

		return "/admin/admMember/adminUser";
	}

	// -hh
	@PostMapping("/admMember/admUserSelect/searchid")
	@ResponseBody
	public int UserIdCheck(@RequestBody AdMemberCheckDTO request) {
		String mb_id = request.getMb_id();
		int userIdCheck = admUserService.getAdmMemberCheck(mb_id);
		
		return userIdCheck;
		
		
	}
	
	// -------------------------------------------------------------------------------
	// -------------------------- 뉴스 관리 컨트롤러 -------------------------------
	// -------------------------------------------------------------------------------
	
	// - hh gh
	// newspaper 뉴스
	@GetMapping("/admNews/news")
	public String newsPage(Model model, Criteria cri) {
		cri.setPerPageNum(7);
		List<NewsPaperVO> newspapers = newspaperService.getAllNewspapers(cri);
		PageMaker pm_news = newspaperService.getPageMaker(cri);
		model.addAttribute("newspapers", newspapers);
		model.addAttribute("pm_news", pm_news);
		return "/admin/admNews/news"; // admin/news.html로 이동
	}

	// - gh
	// 뉴스수정
	@PostMapping("/admNews/newspapers/edit")
	public String updateNewspaper(@RequestParam(required = false) String np_name,
			@RequestParam(required = false) byte np_use, Model model) {

		boolean res = newspaperService.updateNewspaper(np_name, np_use);
		if (res == false) {
			model.addAttribute("msg", "기존 신문사 이름과 동일합니다.");
			model.addAttribute("url", "/admin/admNews/news");
			return "util/msg";
		}

		return "redirect:/admin/admNews/news";
	}

	// - gh
	// 뉴스등록
	@PostMapping("/admNews/newspapers/register")
	public String registerNewspaper(@RequestParam(required = false) String np_name,
			@RequestParam(required = false) String np_use, Model model) {

		// np_use를 byte로 변환 (1 또는 0)
		byte useByte = (np_use != null && np_use.equals("1")) ? (byte) 1 : (byte) 0;

		boolean res = newspaperService.addNewspaper(np_name, useByte);

		if (res == false) {
			model.addAttribute("msg", "이미 존재하는 신문사 입니다.");
			model.addAttribute("url", "/admin/admNews/news");
			return "util/msg";
		}

		return "redirect:/admin/admNews/news";
	}

	// - gh
	// 사용여부 변경
	@PostMapping("/admNews/newspapers/usechange")
	public String NewsPaperUseChange(@RequestParam(required = false) String np_no,
			@RequestParam(required = false) String np_use, Model model) {
		// np_use를 byte로 변환 (1 또는 0)
		byte useByte = (np_use != null && np_use.equals("1")) ? (byte) 0 : (byte) 1;

		boolean res = newspaperService.getUseChange(np_no, useByte);

		if (res == false) {
			model.addAttribute("msg", "이미 존재하는 신문사 입니다.");
			model.addAttribute("url", "/admin/admNews/news");
			return "util/msg";
		}

		return "redirect:/admin/admNews/news";
	}

	// -gh
	// 뉴스삭제
	@PostMapping("/admNews/newspapers/delete")
	public String deleteNewspaper(@RequestParam("np_no") int np_no, @RequestParam("np_name") String np_name,
			@RequestParam("np_use") byte np_use) {
		NewsPaperVO newNewspaper = new NewsPaperVO();
		newNewspaper.setNp_no(np_no); //
		newNewspaper.setNp_name(np_name);
		newNewspaper.setNp_use(np_use);

		newspaperService.deleteNewspaper(newNewspaper);
		return "redirect:/admin/admNews/news";
	}

	// - gh hh
	@GetMapping("/admNews/news/search")
	public String searchNewspapers(Model model, @RequestParam("np_name") String np_name, AdmNewsCriteria cri,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		cri.setPerPageNum(7);
		cri.setPage(page);
		cri.setNp_name(np_name);

		List<AdmMemberVO> newspapers = newspaperService.getSearchNews(cri);
		PageMaker pm_news = newspaperService.getPageMakerSearch(cri);

		model.addAttribute("newspapers", newspapers);
		model.addAttribute("pm_news", pm_news);
		model.addAttribute("np_name", np_name);

		return "/admin/admNews/news";

	}

	// -------------------------------------------------------------------------------
	// -------------------------- LV 관리 컨트롤러 -------------------------------
	// -------------------------------------------------------------------------------

	// gh
	// 접속시 불러오기
	@GetMapping("/admLevel/admLevelPage")
	public String sltAdmLevelPage(Model model) {
		List<AdminLevelPageVO> ssltAdminLevelPage = sltAdmLevelPageService.getAllssltAdminLevelPage();
		model.addAttribute("list", ssltAdminLevelPage);
		return "/admin/admLevel/admLevelPage";
	}

	// hh
	// 회원 정보 상세페이지 조회
	@PostMapping("/admLevel/admLevSel")
	public String admlevSel(Model model, int lv_num) {
		AdminLevelPageVO admlevSel = sltAdmLevelPageService.getAdmlevSel(lv_num);
		model.addAttribute("admlevSel", admlevSel);

		return "/admin//admLevel/admLevSel";
	}

	// hh
	// 수정하기
	@PostMapping("/admLevel/admLevSel/update")
	public String udtAdmLv(AdminLevelPageVO admLevVO, Model model) {
		boolean res = sltAdmLevelPageService.admLevUpdate(admLevVO);
		return "redirect:/admin/admLevel/admLevelPage";
	}

	// -------------------------------------------------------------------------------
	// ------------------- 출석체크 포인트 적립내역 검색 컨트롤러 -------------------
	// -------------------------------------------------------------------------------

	// gh
	// 접속시 불러오기
	@GetMapping("/admDaycheck/daycheckAdm")
	public String sltAdmPointPage(Model model, Criteria cri) {
		cri.setPerPageNum(12);
		List<AdmDaycheckVO> sltPoint = pointSltIdPageService.sltAllDay(cri);
		PageMaker pm_daycheck = pointSltIdPageService.getPageMaker(cri);
		model.addAttribute("list", sltPoint);
		model.addAttribute("pm_daycheck", pm_daycheck);
		return "/admin/admDaycheck/daycheckAdm";
	}

	// gh
	@PostMapping("/admDaycheck/daycheckAdm/update")
	public String sltIdPointPage(@RequestParam String mb_id, Model model) {
		List<AdmDaycheckVO> sltPointOne = pointSltIdPageService.sltOneDay(mb_id);
		model.addAttribute("list", sltPointOne);
		return "/admin/admDaycheck/daycheckAdm";
	}

	// hh
	@GetMapping("/admDaycheck/daycheckAdm/Search")
	public String daycheckSearch(@RequestParam String mb_id, Model model,
			@RequestParam(value = "page", defaultValue = "1") int page, AdmDayCheckCriteria cri) {
		cri.setPerPageNum(12);
		cri.setPage(page);
		cri.setMb_id(mb_id);
		List<AdmDaycheckVO> daycheck = pointSltIdPageService.daychSearch(cri);
		PageMaker pm_daycheck = pointSltIdPageService.getPageMakerSearch(cri);

		model.addAttribute("list", daycheck);
		model.addAttribute("pm_daycheck", pm_daycheck);
		model.addAttribute("mb_id", mb_id);

		return "/admin/admDaycheck/daycheckAdm";
	}

	// -------------------------------------------------------------------------------
	// -------------------------- 주문내역 조회 컨트롤러 ------------------------------------
	// -------------------------------------------------------------------------------

	// gh hh
	// 접속시 불러오기
	@GetMapping("/admOrder/orderAdm")
	public String sltOrder(Model model, Criteria cri) {
		cri.setPerPageNum(15);
		List<admOrderPageVO> sltAdminOrder = adminOrderService.getAllsltAdminOrder(cri);
		PageMaker pm_ord = adminOrderService.getPageMaker(cri);
		model.addAttribute("pm_ord", pm_ord);
		model.addAttribute("list", sltAdminOrder);
		model.addAttribute("pm_ord", pm_ord);
		return "/admin/admOrder/orderAdm";
	}

	// hh
	// 주문내역 검색
	@PostMapping("/admOrder/orderAdm/search")
	public String searchIdName(@RequestParam String od_name, @RequestParam String mb_id, @RequestParam String od_id,
			Model model) {

		List<admOrderPageVO> searchOrder = adminOrderService.searchNameId(od_name, mb_id, od_id);
		model.addAttribute("list", searchOrder);
		return "/admin/admOrder/orderAdm";

	}
	
	// gh
	// 주문번호로 삭제
	@PostMapping("/admOrder/orderAdm/delet")
	public String orderDelet(@RequestParam String od_id, Model model) {
		List<admOrderPageVO> deletOrder = adminOrderService.deletOrderNum(od_id);
		return "redirect:/admin/admOrder/orderAdm";

	}

	// -------------------------------------------------------------------------------
	// --------------------------주식/뉴스 회원승인 ----------------------------------
	
	// gh
	@GetMapping("/admOrder/orderAdm/AdmOrderSearch")
	public String orderSearch(@RequestParam("od_sh") String od_sh, @RequestParam("od_search") String od_search,
			@RequestParam(value = "page", defaultValue = "1") int page, Model model, OrderCriteria cri) {
//		페이지에 보여주는 리스트 수
		cri.setPerPageNum(8);
//		현재 페이지 넘기기		
		cri.setPage(page);
//		검색어 넘기기
		cri.setOd_search(od_search);
		List<admOrderPageVO> orderSearch = adminOrderService.getOrderSearch(od_sh, cri);
		PageMaker pm_ord = adminOrderService.getPageMakerSearch(cri, od_sh);

		pm_ord.setCri(cri);
		model.addAttribute("list", orderSearch);
		model.addAttribute("pm_ord", pm_ord);
		model.addAttribute("od_sh", od_sh);
		model.addAttribute("od_search", od_search);

		return "/admin/admOrder/orderAdm";
	}

	// ------------------------- 글쓰기 권한 수정 페이지 ----------------------------
	// ------------------------- admApprovalPage ----------------------------
	// -------------------------------------------------------------------------------

	// gh
	// 접속시
	@GetMapping("/admApproval/admApprovalPage")
	public String nullSltApproval(Model model, Criteria cri) {
		cri.setPerPageNum(8);
		List<AdmApprovalVO> nullSlt = adminApprovalService.allSelect(cri);
		PageMaker pm_Approval = adminApprovalService.getPageMaker(cri);
		model.addAttribute("pm_Approval", pm_Approval);
		model.addAttribute("list", nullSlt);
		return "/admin/admApproval/admApprovalPage";
	}

	// gh
	// 승인/거절 했을때
	@PostMapping("/admApproval/admApprovalPage/slt")
	public String ySltApproval(@RequestParam String mp_yn, @RequestParam String mp_company,
			@RequestParam String mp_type, @RequestParam int mb_no) {
		adminApprovalService.ynUPDATE(mp_yn, mp_company, mp_type, mb_no);
		return "redirect:/admin/admApproval/admApprovalPage";
	}

	// hh
	// 검색기능
	@GetMapping("/admApproval/admApprovalPage/search")
	public String searchApproval(String mp_company, Model model,
			@RequestParam(value = "page", defaultValue = "1") int page, AdmApprovalCriteria cri) {
		cri.setPerPageNum(8);
//		현재 페이지 넘기기		
		cri.setPage(page);
//		검색어 넘기기
		cri.setMp_company(mp_company);

		List<AdmApprovalVO> approvalSearch = adminApprovalService.search(cri);
		PageMaker pm_Approval = adminApprovalService.getPageMakersearch(cri);

		model.addAttribute("pm_Approval", pm_Approval);
		model.addAttribute("list", approvalSearch);
		model.addAttribute("mp_company", mp_company);
		return "/admin/admApproval/admApprovalPage";
	}

	// -------------------------------------------------------------------------------
	// --------------------------사용자포인트 관리 컨트롤러 --------------------------
	// -------------------------------------------------------------------------------

	// gh hh
	// 페이지 이동시 리스트 당겨옴
	@GetMapping("/admPoint/admPointPage")
	public String pointSelect(Model model, Criteria cri) {
		cri.setPerPageNum(7);
		List<AdmPointVO> Slt = admPointService.allselect(cri);
		PageMaker pm_point = admPointService.getPageMaker(cri);
		model.addAttribute("list", Slt);
		model.addAttribute("pm_point", pm_point);
		return "/admin/admPoint/admPointPage";
	}

	// hh
	// 아이디로 사용자 검색
	@PostMapping("admPoint/admPointPage/Id")
	public String idSelect(@RequestParam String mb_id, Model model, Criteria cri) {
		List<AdmPointVO> idSlt = admPointService.idSelect(mb_id);
		PageMaker pm_point = admPointService.getPageMaker(cri);
		model.addAttribute("list", idSlt);
		model.addAttribute("pm_point", pm_point);
		return "/admin/admPoint/admPointPage";
	}

	// gh
	// 포인트 적립, 차감
	@PostMapping("/admPoint/admPointPage/point")
	public String plusMinus(Model model, @RequestParam String mb_id, @RequestParam int po_num,@RequestParam String pointType, @RequestParam String po_content) {
		MemberVO user = memberService.findById(mb_id);
		if(user != null) {
			admPointService.plusminus(user.getMb_id(), po_num, pointType, po_content);
			model.addAttribute("msg", "정상 처리되었습니다.");
			model.addAttribute("url", "/admin/admPoint/admPointPage");
			return "util/msg";
		}
		else {
			model.addAttribute("msg", "없는 회원입니다.");
			model.addAttribute("url", "/admin/admPoint/admPointPage");
			return "util/msg";
		}
	}

	// gh
	// 내역 삭제
	@PostMapping("admPoint/admPointPage/delete")
	public String plusMinus(@RequestParam int po_no) {

		admPointService.delete(po_no);
		return "redirect:/admin/admPoint/admPointPage";
	}

	// hh
	@GetMapping("admPoint/admPointPage/pointSearch")
	public String pointSearch(Model model, String mb_id, AdmPointCriteria cri,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		cri.setPerPageNum(7);
		cri.setPage(page);
		cri.setMb_id(mb_id);

		List<AdmPointVO> Slt = admPointService.getPointUserSearch(cri);
		PageMaker pm_point = admPointService.getPageMaker(cri);

		model.addAttribute("list", Slt);
		model.addAttribute("pm_point", pm_point);
		model.addAttribute("mb_id", mb_id);

		return "/admin/admPoint/admPointPage";

	}



	// -------------------------------------------------------------------------------
	// -------------------------- 주식 발행/소각 요청 승인 ----------------------------
	// ------------------------------------------------------------------------------

	// gh
	// 페이지 이동시 리스트 당겨옴
	@GetMapping("/admStock/admStock_add")
	public String stock_add(Model model) {
		List<AdminStock_addVO> selecte = adminStock_addService.nullSelect();
		model.addAttribute("list", selecte);
		return "/admin/admStock/admStock_add";
	}

	// gh
	// 검색하기
	@PostMapping("/admStock/admStock_add/search")
	public String Stock_addSearch(Model model, String mb_id) {
		System.out.println(mb_id);
		List<AdminStock_addVO> search = adminStock_addService.search(mb_id);
		System.out.println(mb_id);
		model.addAttribute("list", search);
		return "/admin/admStock/admStock_add";
	}

	// gh
	// 상세페이지에서 승인/거절시 update 하고 리스트 페이지로 이동
	@PostMapping("/admStock/admStock_add/choose")
	public String chooseStock_add(int sa_no, String sa_yn, String sa_feedback, int sa_qty, String mb_id) {
//		sa_yn 체크시 : on // 아니면 "null" 	
		adminStock_addService.update(sa_no, sa_yn, sa_feedback, sa_qty,mb_id);
		return "redirect:/admin/admStock/admStock_add";
	}
	
}   
