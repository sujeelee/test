package kh.st.boot.dao;

import java.util.List;

import kh.st.boot.model.dto.EventDTO;
import kh.st.boot.model.dto.EventPrizeCounterDTO;
import kh.st.boot.model.vo.EventPrizeVO;
import kh.st.boot.model.vo.EventVO;
import kh.st.boot.model.vo.FileVO;
import kh.st.boot.model.vo.PrizeVO;

public interface EventDAO {

    List<EventDTO> getEventList(String eventStatus);

    EventVO getEvent(String eventStatus, int ev_no);

    boolean setEvent(EventVO event);

    EventVO getEventToImportAFile();

    void setEventFile(FileVO fileVo);

    boolean updateEventDateAndStatus();

    List<EventDTO> getEventPostList(String eventStatus);

    EventVO getEventPost(int ev_no);

    boolean deleteEventPost(int ev_no);

    FileVO getFileByEventNumber(int ev_no);

    boolean updateEvent(EventVO event);

    boolean deleteEventThumbnailFile(int ev_no);

    boolean setNewCalenderEvent(String mb_id, String checkList);

    boolean setCalenderEvent_add50point(String mb_id);

    void setPointByCalenderEvent(String mb_id);

    String getCalenderEventValue(String name);

    List<EventVO> getEventListByEventForm(String form);

    boolean setPrizeToBeUsedFromTheEvent(PrizeVO prize);

    List<PrizeVO> getPrizeListByEv_no(int ev_no);

    PrizeVO getPrizeLastOne();

    EventPrizeVO getEventPrizeTicket(EventPrizeVO ep);

    boolean setEventPrizeTicket(EventPrizeVO ep);

    boolean updateEventPrizeTicket_AddOne(EventPrizeVO ep);

    List<EventPrizeVO> getEventPrizeTicketList(int ep_no, String mb_id);

    List<EventPrizeCounterDTO> getEventPrizeTicketCounter(int ev_no);

    List<EventDTO> getEventAllList();

    boolean deletePrize(int pr_no);

    PrizeVO getPrizeByPr_no(int pr_no);

    boolean updatePrize(PrizeVO prize);

    boolean deletePrizeThumbnailFile(int pr_no);

    EventVO findEventByNumber(int ev_no);

    boolean changeBannerShow(int ev_no, int i);

    List<EventVO> getEventListForMainBanner();


    
}
