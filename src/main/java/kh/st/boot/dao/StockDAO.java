package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.dto.DashListDTO;
import kh.st.boot.model.dto.HotStockDTO;
import kh.st.boot.model.vo.ReservationVO;
import kh.st.boot.model.vo.StockJisuVO;
import kh.st.boot.model.vo.StockPriceVO;
import kh.st.boot.model.vo.StockVO;
import kh.st.boot.model.vo.WishVO;
import kh.st.boot.pagination.StockCriteria;

public interface StockDAO {

	StockVO getStockCompany(@Param("st_code")String st_code);
	
	void companyType(@Param("st_code")String st_code, @Param("st_type")String st_type, @Param("st_status")String st_status); 

	void insertStockCompany(@Param("st")StockVO newStock); 
	 
	List<StockVO> getCompanyList(@Param("type")String type, @Param("cri")StockCriteria cri); 
	
	List<StockVO> getCompanyListMrk(@Param("type")String type, @Param("cri")StockCriteria cri);

	StockPriceVO getStockPrice(@Param("si_date")String si_date, @Param("st_code")String st_code); 

	boolean insertStockPrice(@Param("si")StockPriceVO price); 

	int getCount(@Param("cri")StockCriteria cri); 
	
	int getCountMrk(@Param("cri")StockCriteria cri);
	
	List<StockPriceVO> getStockInfoList(@Param("st_code")String st_code); 

	int getCountStockPrice(@Param("st_code")String st_code); 
	
	StockPriceVO getStockPriceOne(@Param("st_code")String st_code);
	
	boolean wishStockInsert(@Param("code")String st_code, @Param("mb_id")String mb_id);
	
	boolean wishStockDelete(@Param("code")String st_code, @Param("mb_id")String mb_id);
	
	WishVO wishCheck(@Param("code")String st_code, @Param("mb_id")String mb_id);

	StockPriceVO getStockPriceLater(@Param("code")String st_code);

	List<StockPriceVO> getStockInfoListDate(@Param("code")String st_code, @Param("to")String to_date, @Param("from")String from_date);

	List<HotStockDTO> getHotStockList(int limit);

	List<DashListDTO> getMyStock(@Param("mb_id")String mb_id);

	List<DashListDTO> getMyWish(@Param("mb_id")String mb_id);

	StockJisuVO getOldJisu(@Param("date")String date, @Param("type")String type);

	boolean insertStockJisu(@Param("ji")StockJisuVO jisu);

	List<StockJisuVO> jisuConfig(@Param("type")String type);

	List<ReservationVO> getReservation(@Param("code")String st_code);

}
