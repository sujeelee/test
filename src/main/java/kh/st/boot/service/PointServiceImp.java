package kh.st.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.st.boot.dao.PointDAO;

@Service
public class PointServiceImp implements PointService {

    @Autowired
    private PointDAO pointDao;

    @Override
    public boolean usePoint(String userName, Integer point, String reason) {
        if (userName == null || userName.trim().length() == 0) {
            return false;
        }

        if (pointDao.spandPoint(userName, (int)point)) {
            return pointDao.usePoint(userName, -(int)point, reason);
        } else {
            return false;
        }       
    }
    
}
