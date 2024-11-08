package kh.st.boot.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kh.st.boot.model.dto.EventDTO;
import kh.st.boot.model.dto.EventPrizeCounterDTO;
import kh.st.boot.model.vo.EventPrizeVO;
import kh.st.boot.model.vo.EventVO;
import kh.st.boot.model.vo.FileVO;
import kh.st.boot.model.vo.PrizeVO;

public interface EventService {

    List<EventDTO> getEventList(String eventStatus);

    EventVO getEvent(String eventStatus, int ev_no);

    boolean setEvent(EventVO event, MultipartFile file,MultipartFile file_banner);

    boolean updateEventDateAndStatus();

    boolean deleteEventPost(int ev_no);

    FileVO getFile(int ev_no);

    boolean updateEvent_withFile(EventVO event, MultipartFile file);

    boolean CalenderEvent(String mb_id, int[] checkList);

    String getCalenderEventValue(String name);

    boolean setPrizeToBeUsedFromTheEvent(PrizeVO prize, MultipartFile file);

    List<EventVO> getEventListByEventForm(String form);

    List<PrizeVO> getPrizeListByEv_no(int ev_no);

    Boolean setEventPrizeTicket(EventPrizeVO ep);

    EventPrizeVO getEventPrizeTicket(EventPrizeVO ep);

    List<EventPrizeVO> getEventPrizeTicketList(int ep_no, String string);

    List<EventPrizeCounterDTO> getEventPrizeTicketCounter(int ev_no);

    List<EventDTO> getEventAllList();

    boolean deletePrize(int pr_no);

    PrizeVO getPrizeByPr_no(int pr_no);

    boolean updateEventPrize_withFile(PrizeVO prize, MultipartFile file);

    boolean changeBannerShow(int ev_no);

    List<EventVO> getEventListForMainBanner();

    

    
}
