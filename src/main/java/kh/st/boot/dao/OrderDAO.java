package kh.st.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kh.st.boot.model.dto.MyStocksDTO;
import kh.st.boot.model.vo.OrderVO;
import kh.st.boot.model.vo.PointVO;
import kh.st.boot.model.vo.ReservationVO;

public interface OrderDAO {
	List<ReservationVO> getReservation(@Param("code")String st_code, @Param("mb_id")String mb_id);

	boolean insertReservation(@Param("re")ReservationVO reservation);

	boolean insertOrder(@Param("od")OrderVO order);

	boolean setPointBuy(@Param("po")PointVO point);
	
	boolean updateMemberPoint(@Param("po")PointVO point);
	
	boolean stockQty(@Param("qty")int od_qty, @Param("code")String st_code);
	
	MyStocksDTO totalMyStock(@Param("code")String st_code, @Param("mb_id")String mb_id);

	boolean deleteReservation(@Param("code")String st_code, @Param("re_no")String re_no);

	void updateReservation(@Param("re")ReservationVO tmp);
}
