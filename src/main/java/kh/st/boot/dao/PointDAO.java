package kh.st.boot.dao;

public interface PointDAO {

    boolean spandPoint(String userName, int point);

    boolean usePoint(String userName, int point, String reason);
    
}
