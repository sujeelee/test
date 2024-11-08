package kh.st.boot.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kh.st.boot.dao.EventDAO;
import kh.st.boot.model.dto.EventDTO;
import kh.st.boot.model.dto.EventPrizeCounterDTO;
import kh.st.boot.model.vo.EventPrizeVO;
import kh.st.boot.model.vo.EventVO;
import kh.st.boot.model.vo.FileVO;
import kh.st.boot.model.vo.PrizeVO;
import kh.st.boot.utils.UploadFileUtils;

@Service
public class EventServiceImp implements EventService {

    @Autowired
    private EventDAO eventDao;
    
    @Value("${file.upload-dir}")
	String uploadPath;

    @Override
    public List<EventDTO> getEventList(String eventStatus) {
        if (eventStatus.equals("resUser")) {
            return eventDao.getEventPostList(eventStatus);
        }
        return eventDao.getEventList(eventStatus);
    }

    @Override
    public EventVO getEvent(String eventStatus, int ev_no) {
        if (eventStatus.equals("resUser")) {
            return eventDao.getEventPost(ev_no);
        }
        return eventDao.getEvent(eventStatus, ev_no);
    }

    @Override
    public boolean setEvent(EventVO event, MultipartFile file, MultipartFile file_banner) {
        if (event == null) {
            return false;
        }

        if (event.getEv_title() == null || event.getEv_title().trim().length() == 0) {
            return false;
        }

        boolean res = eventDao.setEvent(event);

        if (res) {
            EventVO tmpEv = eventDao.getEventToImportAFile();//방금 set한 event를 가져온다.
            uploadFile(file, tmpEv.getEv_no(), "event");
            uploadFile(file_banner, tmpEv.getEv_no(), "eventBanner");
        }
        return res;
    }

    //파일
	private void uploadFile(MultipartFile file, int ev_no, String fi_type) {
		if(file == null || file.getOriginalFilename().trim().length() == 0) {
			return;
		}
		// 첨부 파일을 서버에 업로드
		String fi_org_name = file.getOriginalFilename();
		try {
			String fi_path = UploadFileUtils.uploadFile(uploadPath, fi_org_name, file.getBytes());
			// 업로드한 정보를 DB에 추가
			FileVO fileVo = new FileVO(fi_path, fi_org_name, ev_no, fi_type);
			eventDao.setEventFile(fileVo);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Override
    public boolean updateEventDateAndStatus() {

        return eventDao.updateEventDateAndStatus();
    }

    @Override
    public boolean deleteEventPost(int ev_no) {
        return eventDao.deleteEventPost(ev_no);
    }

    @Override
    public FileVO getFile(int ev_no) {
        return eventDao.getFileByEventNumber(ev_no);
    }

    @Override
    public boolean updateEvent_withFile(EventVO event, MultipartFile file) {
        
        if (event == null) {
            return false;
        }

        if (event.getEv_title() == null || event.getEv_title().trim().length() == 0) {
            return false;
        }

        boolean up = eventDao.updateEvent(event);

        if (up && !file.isEmpty()) {
            up = eventDao.deleteEventThumbnailFile(event.getEv_no());
            UploadFileUtils.delteFile(uploadPath, event.getFi_path());
            uploadFile(file, event.getEv_no(), "event");
        }

        return up;
    }

    @Override
    public boolean CalenderEvent(String mb_id, int[] checkList) {
        if (mb_id == null) {
            return false;
        }

        String arrayAsString = Arrays.toString(checkList).replaceAll("[\\[\\] ]", "");


        if (eventDao.setCalenderEvent_add50point(mb_id)) {
            eventDao.setPointByCalenderEvent(mb_id);
            return eventDao.setNewCalenderEvent(mb_id, arrayAsString);
        }
        
        return false;
    }

    @Override
    public String getCalenderEventValue(String name) {
        if (name == null || name.trim().length() == 0) {
            return null;
        }
        return eventDao.getCalenderEventValue(name);
    }

    @Override
    public boolean setPrizeToBeUsedFromTheEvent(PrizeVO prize, MultipartFile file) {
        if (prize == null || prize.getPr_name().trim().length() == 0) {
            return false;
        }

        boolean res = eventDao.setPrizeToBeUsedFromTheEvent(prize);

        if (res) {
            PrizeVO tmpPrize = eventDao.getPrizeLastOne();
            uploadFile(file, tmpPrize.getPr_no(), "prize");
        }
        return res;
    }

    @Override
    public List<EventVO> getEventListByEventForm(String form) {
        if (form == null || form.trim().length() == 0) {
            return null;
        }

        return eventDao.getEventListByEventForm(form);
    }

    @Override
    public List<PrizeVO> getPrizeListByEv_no(int ev_no) {

        return eventDao.getPrizeListByEv_no(ev_no);
    }

    @Override
    public Boolean setEventPrizeTicket(EventPrizeVO ep) {
        boolean res = false;
        
        if (ep.getEp_mb_id() == null || ep == null || ep.getEp_mb_id().trim().length() == 0) {
            return false;
        }
        EventPrizeVO oldEp = getEventPrizeTicket(ep);
        
        if (oldEp == null) {
            res = eventDao.setEventPrizeTicket(ep);
        } else {
            res = eventDao.updateEventPrizeTicket_AddOne(ep);
        }

        return res;
    }

    @Override
    public EventPrizeVO getEventPrizeTicket(EventPrizeVO ep) {
        if (ep == null || ep.getEp_mb_id() == null || ep.getEp_mb_id().trim().length() == 0) {
            return null;
        }
        return eventDao.getEventPrizeTicket(ep);
    }

    @Override
    public List<EventPrizeVO> getEventPrizeTicketList(int ep_no, String mb_id) {
        return eventDao.getEventPrizeTicketList(ep_no, mb_id);
    }

    @Override
    public List<EventPrizeCounterDTO> getEventPrizeTicketCounter(int ev_no) {
        return eventDao.getEventPrizeTicketCounter(ev_no);
    }

    @Override
    public List<EventDTO> getEventAllList() {

        return eventDao.getEventAllList();
    }

    @Override
    public boolean deletePrize(int pr_no) {
        PrizeVO pr = eventDao.getPrizeByPr_no(pr_no);
        System.out.println(pr);
        UploadFileUtils.delteFile(uploadPath, pr.getFi_path());
        return eventDao.deletePrize(pr_no);
    }

    @Override
    public PrizeVO getPrizeByPr_no(int pr_no) {
        return eventDao.getPrizeByPr_no(pr_no);
    }

    @Override
    public boolean updateEventPrize_withFile(PrizeVO prize, MultipartFile file) {
        

        if (prize == null) {
            return false;
        }

        if (prize.getPr_name() == null || prize.getPr_name().trim().length() == 0) {
            return false;   
        }

        boolean res = eventDao.updatePrize(prize);

        if (res && !file.isEmpty()) {
            res = eventDao.deletePrizeThumbnailFile(prize.getPr_no());
            if (res) {
                UploadFileUtils.delteFile(uploadPath, prize.getFi_path());
                uploadFile(file, prize.getPr_no(), "prize");
            }
        }

        return res;
    }

    @Override
    public boolean changeBannerShow(int ev_no) {
        EventVO event = eventDao.findEventByNumber(ev_no);
        boolean res = false;
        if (event.getEv_bannerShow() == 1) {
            res = eventDao.changeBannerShow(ev_no, 0);
        } else if (event.getEv_bannerShow() == 0) {
            res = eventDao.changeBannerShow(ev_no, 1);
        }
        return res;
    }

    @Override
    public List<EventVO> getEventListForMainBanner() {
        
        return eventDao.getEventListForMainBanner();
    }



    

    
}
