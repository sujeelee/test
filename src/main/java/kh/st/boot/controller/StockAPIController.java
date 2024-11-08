package kh.st.boot.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.st.boot.model.vo.StockJisuVO;
import kh.st.boot.model.vo.StockPriceVO;
import kh.st.boot.model.vo.StockVO;
import kh.st.boot.pagination.Criteria;
import kh.st.boot.pagination.PageMaker;
import kh.st.boot.pagination.StockCriteria;
import kh.st.boot.service.StockService;


@Controller
@RequestMapping("/admin/stock")
public class StockAPIController {
	@Autowired
	StockService stockService;
	private String encodeKey = "Ys5KwqClpBafJnzS5dWVgJrYaBHnZC8udtvx5%2FNkEepksQAMeaMA5n1N92DJQe39K7dfLtpnJcm9uS8s9i9WTw%3D%3D";
	
	@GetMapping("")
	public String home(@RequestParam Map<String, String> params, StockCriteria cri, Model model) {
		List<StockVO> list = stockService.getCompanyList("", cri);
		// 페이지메이커 생성 (페이징 처리를 위한 객체)
		PageMaker pm = stockService.getPageMaker(cri);
		
		for(StockVO st : list) {
			int cnt = stockService.getCompanyStockCount(st.getSt_code());
			st.setSt_price_cnt(cnt);
		}
		
		// 오늘 날짜 가져오기
        LocalDate nowday = LocalDate.now();
        // 날짜 형식 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 어제 날짜를 문자열로 변환
        String date = nowday.format(formatter);
		model.addAttribute("date", date);
	    // 가져온 데이터를 모델에 추가
	    model.addAttribute("list", list);
	    model.addAttribute("pm", pm); // 페이지 정보 추가
	    
		return "stock/stockList";
	}
	
	private List<Map<String, Object>> getUrlAPI(String apiUrl, String type){
		StringBuilder result = new StringBuilder();
		
	    try {
	        // URL 객체 사용
	        @SuppressWarnings("deprecation")
			URL url = new URL(apiUrl); // apiUrl은 private 멤버 변수로 선언된 상태
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	        urlConnection.setRequestMethod("GET");
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
	        String returnLine;
	        while ((returnLine = bufferedReader.readLine()) != null) {
	            result.append(returnLine).append("\n");
	        }
	        urlConnection.disconnect();
	        // JSON 문자열을 Java 객체로 변환
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode rootNode = objectMapper.readTree(result.toString());
	        
	        // "response" -> "body" -> "items" -> "item" 배열 추출
	        JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
	        // JsonNode를 List로 변환
	        List<Map<String, Object>> jsonArray = objectMapper.convertValue(itemsNode, new TypeReference<List<Map<String, Object>>>(){});
	        
	        if(type == "total") {
		        JsonNode totalCountTmp = rootNode.path("response").path("body").path("totalCount");
		        int totalCount = totalCountTmp.asInt();  // totalCount 값을 정수로 추출
		       
		        Map<String, Object> totalCountMap = new HashMap<>();
		        totalCountMap.put("totalCount", totalCount);
		        //totalCount 값을 jsonArray 리스트에 추가
		        jsonArray.add(totalCountMap);  // 리스트의 마지막에 totalCount 정보만 추가
	        }
	        return jsonArray;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	@GetMapping("/getListStock")
	public String RestAPI(Criteria cri, Model model, @RequestParam Map<String, String> params) {
		// 현재 날짜 가져오기
        LocalDate today = LocalDate.now();
        
        // "yyyyMM" 형식으로 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String formattedDate = today.format(formatter);
        
		String apiUrl = "https://apis.data.go.kr/1160100/service/GetKrxListedInfoService/getItemInfo"
				+ "?serviceKey="
				+ encodeKey
				+ "&likeBasDt="+ formattedDate
				+"&resultType=json&numOfRows=10&pageNo=";
		
		int count = 0;
		
		apiUrl += cri.getPage();
		if(params.get("sfl") != null && params.get("stx") != null) {
			String stx = params.get("stx").toString();
			try {
				stx = URLEncoder.encode(params.get("stx").toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(params.get("sfl").equals("cmpname")) {
				apiUrl += "&likeItmsNm=" + stx;
			} else if(params.get("sfl").equals("crno")) {
				apiUrl += "&crno=" + stx;
			}
		}
		List<Map<String, Object>> jsonArray = getUrlAPI(apiUrl, "total");
		
		if(jsonArray.isEmpty()) return "stock/getStock"; // 뷰 이름 반환
		
		// 페이지메이커 생성 (페이징 처리를 위한 객체)
		count = (int) jsonArray.getLast().get("totalCount");
		PageMaker pm = new PageMaker(10, cri, count);
		jsonArray.removeLast();
		System.out.println(jsonArray);
		System.out.println(params);
		System.out.println(pm);
		model.addAttribute("list", jsonArray);
		model.addAttribute("param", params);
		model.addAttribute("pm", pm); // 페이지 정보 추가
		List<String> codeList = new ArrayList<>();
		for (Map<String, Object> item : jsonArray) {
			item.put("tmp", "N");
			for (Map.Entry<String, Object> entry : item.entrySet()) {
				if ("isinCd".equals(entry.getKey())) {
			        String st_code = (String) entry.getValue(); // 값을 String으로 변환
			        if(!codeList.contains(st_code)) {			        	
			        	codeList.add(st_code);
			        }
			        StockVO st = stockService.getCompanyOne(st_code);
			        if(st != null) {
			        	item.replace("tmp", "Y");
			        }
			    }
			}
		}
		return "stock/getStock";
	}
	
	@PostMapping("/insertStock")
	@ResponseBody
	public Map<String, String> insertStock(@RequestBody Map<String, Object> stockInfo,HttpServletRequest req, HttpServletResponse res) {
		Map<String, String> result = new HashMap<String, String>();
		StockVO chkStock = stockService.getCompanyOne((String) stockInfo.get("isinCd"));
		if(chkStock != null) {
			result.put("res", "fail");
			result.put("msg", "이미 등록된 주식회사입니다.");
		} else {
			StockVO newStock = new StockVO();
			newStock.setSt_code((String) stockInfo.get("isinCd"));
			newStock.setSt_name((String) stockInfo.get("itmsNm"));
			newStock.setSt_type((String) stockInfo.get("mrktCtg"));
			
			String apiUrl = "https://apis.data.go.kr/1160100/service/GetStocIssuInfoService_V2/getItemBasiInfo_V2"
					+ "?serviceKey="
					+ encodeKey
					+ "&crno="+ (String) stockInfo.get("crno")
					+"&resultType=json&numOfRows=10&pageNo=1";
			List<Map<String, Object>> jsonArray = getUrlAPI(apiUrl, "");
			
			if(!jsonArray.isEmpty()) {
				Map<String, String> field = new HashMap<>();
		        field.put("issuStckCnt", "st_qty");
		        field.put("stckIssuCmpyNm", "st_issue");
		        field.put("lstgAbolDt", "st_status");
		        // jsonArray 값을 확인하기 위한 for문
		        for (Map<String, Object> item : jsonArray) {
		            for (Map.Entry<String, Object> entry : item.entrySet()) {
		            	String jsonKey = entry.getKey();
		                Object value = entry.getValue();
		                String voFieldName = field.get(jsonKey);
		                if (voFieldName != null) {
		                	Field fields;
							try {
								fields = StockVO.class.getDeclaredField(voFieldName);
								fields.setAccessible(true); // private 필드 접근
			                    fields.set(newStock, value); // 필드에 값 설정
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                }
		            }
		        }
			}
			stockService.insertStockCompany(newStock);
			getAllStockInfo(req, res, newStock.getSt_code());
			result.put("res", "success");
			result.put("msg", "주식회사 등록이 완료되었습니다.");
		}
		return result;
	}
	
	@GetMapping("/Stockprice")
	public String getStockPrice(Model model) {
		List<StockVO> list = stockService.getCompanyList("상장폐지", null);
		model.addAttribute("result", list);
		for(int i=0; i<list.size();i++) {
			String st_code = list.get(i).getSt_code();
			String apiUrl = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey="
					+ encodeKey
					+ "&isinCd=" + st_code
					+ "&resultType=json";
			
			List<Map<String, Object>> jsonArray = getUrlAPI(apiUrl, "");
			
			if(jsonArray.isEmpty()) return "stock/Stockprice"; // 뷰 이름 반환
			
			Map<String, String> field = new HashMap<>();
			field.put("basDt", "si_date");
			field.put("clpr", "si_price");
			field.put("isinCd", "st_code");
			field.put("vs", "si_vs");
			field.put("fltRt", "si_fltRt");
			field.put("mrktTotAmt", "si_mrkTotAmt");
			field.put("hipr", "si_hipr");
			field.put("lopr", "si_lopr");
			field.put("trqu", "si_trqu");
			
			StockVO stock = stockService.getCompanyOne(st_code);
			
			 // jsonArray 값을 확인하기 위한 for문
	        for (Map<String, Object> item : jsonArray) {
	        	 StockPriceVO stockPrice = new StockPriceVO(); // 새로운 StockVO 객체 생성
	            for (Map.Entry<String, Object> entry : item.entrySet()) {
	            	String jsonKey = entry.getKey();
	                Object value = entry.getValue();
	                String voFieldName = field.get(jsonKey);
	                if (voFieldName != null) {
	                	Field fields;
						try {
							fields = StockPriceVO.class.getDeclaredField(voFieldName);
							fields.setAccessible(true); // private 필드 접근
							if(voFieldName == "si_price") {
								value = Integer.parseInt((String) value); 
							}
		                    fields.set(stockPrice, value); // 필드에 값 설정
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                }
	            }
	            stockService.insertPrice(stockPrice);
	        }
		}
		
		return "stock/Stockprice"; // 뷰 이름 반환
	}
	
	@GetMapping("/stockInfo/{code}")
	public String getStockInfo(Model model, @PathVariable String code) {
		
		// 오늘 날짜 가져오기
        LocalDate yesterday = LocalDate.now();
        
        // 날짜 형식 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        
        // 어제 날짜를 문자열로 변환
        String date = yesterday.format(formatter);
        
		StockPriceVO today = stockService.getStockPrice(date, code);
		List<StockPriceVO> list = stockService.getStockInfoList(code);
		StockVO stock = stockService.getCompanyOne(code);
		
		model.addAttribute("today", today);
		model.addAttribute("date", date);
		model.addAttribute("list", list);
		model.addAttribute("stock", stock);
		
		return "stock/stockInfo";
	}
	
	@PostMapping("/getNewStockInfo")
	@ResponseBody
	public Map<String, String> getNewStockInfo(HttpServletRequest req, HttpServletResponse res, @RequestParam("st_code") String st_code, @RequestParam("si_date") String si_date) {
			String apiUrl = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey="
					+ encodeKey
					+ "&isinCd=" + st_code
					+ "&endBasDt=" + si_date
					+ "&resultType=json";
			
			Map<String, String> result = new HashMap<>();
			
			List<Map<String, Object>> jsonArray = getUrlAPI(apiUrl, "");
			if(jsonArray == null) {
				result.put("res", "fail"); 
				return result;
			}
			Map<String, String> field = new HashMap<>();
			field.put("basDt", "si_date");
			field.put("clpr", "si_price");
			field.put("isinCd", "st_code");
			field.put("vs", "si_vs");
			field.put("fltRt", "si_fltRt");
			field.put("mrktTotAmt", "si_mrkTotAmt");
			field.put("hipr", "si_hipr");
			field.put("lopr", "si_lopr");
			field.put("trqu", "si_trqu");
			
			StockVO stock = stockService.getCompanyOne(st_code);
			boolean chk = false;
			 // jsonArray 값을 확인하기 위한 for문
	        for (Map<String, Object> item : jsonArray) {
	        	 StockPriceVO stockPrice = new StockPriceVO(); // 새로운 StockVO 객체 생성
	            for (Map.Entry<String, Object> entry : item.entrySet()) {
	            	String jsonKey = entry.getKey();
	                Object value = entry.getValue();
	                String voFieldName = field.get(jsonKey);
	                if (voFieldName != null) {
	                	Field fields;
						try {
							fields = StockPriceVO.class.getDeclaredField(voFieldName);
							fields.setAccessible(true); // private 필드 접근
							if(voFieldName == "si_price") {
								value = Integer.parseInt((String) value); 
							}
		                    fields.set(stockPrice, value); // 필드에 값 설정
						} catch (Exception e) {
							e.printStackTrace();
						}
	                }
	            }
	            if(chk == false) {
	            	chk = stockService.insertPrice(stockPrice);
	            } else {
	            	stockService.insertPrice(stockPrice);
	            }
	        }
	        
	        if(chk) {
	        	result.put("res", "success"); 
		        result.put("name", stock.getSt_name());
	        } else {
	        	result.put("res", "fail");
	        }
			return result;
	}
	
	@PostMapping("/getAllStockInfo")
	@ResponseBody
	public Map<String, String> getAllStockInfo(HttpServletRequest req, HttpServletResponse res, @RequestParam("st_code") String st_code) {
			String apiUrl = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey="
					+ encodeKey
					+ "&isinCd=" + st_code
					+ "&numOfRows=100"
					+ "&resultType=json";
			
			Map<String, String> result = new HashMap<>();
			
			List<Map<String, Object>> jsonArray = getUrlAPI(apiUrl, "");
			if(jsonArray == null) {
				result.put("res", "fail"); 
				return result;
			}
			Map<String, String> field = new HashMap<>();
			field.put("basDt", "si_date");
			field.put("clpr", "si_price");
			field.put("isinCd", "st_code");
			field.put("vs", "si_vs");
			field.put("fltRt", "si_fltRt");
			field.put("mrktTotAmt", "si_mrkTotAmt");
			field.put("hipr", "si_hipr");
			field.put("lopr", "si_lopr");
			field.put("trqu", "si_trqu");
			
			StockVO stock = stockService.getCompanyOne(st_code);
			boolean chk = false;
			 // jsonArray 값을 확인하기 위한 for문
	        for (Map<String, Object> item : jsonArray) {
	        	 StockPriceVO stockPrice = new StockPriceVO(); // 새로운 StockVO 객체 생성
	            for (Map.Entry<String, Object> entry : item.entrySet()) {
	            	String jsonKey = entry.getKey();
	                Object value = entry.getValue();
	                if(stock.getSt_type() == null && jsonKey.contains("mrktCtg")) {
	                	stockService.updateCompanyType(st_code, (String) value, "");
	                }
	                String voFieldName = field.get(jsonKey);
	                if (voFieldName != null) {
	                	Field fields;
						try {
							fields = StockPriceVO.class.getDeclaredField(voFieldName);
							fields.setAccessible(true); // private 필드 접근
							if(voFieldName == "si_price") {
								value = Integer.parseInt((String) value); 
							}
		                    fields.set(stockPrice, value); // 필드에 값 설정
						} catch (Exception e) {
							e.printStackTrace();
						}
	                }
	            }
	            chk = stockService.insertPrice(stockPrice);
	        }
	        
	        if(chk) {
	        	result.put("res", "success"); 
		        result.put("name", stock.getSt_name());
	        } else {
	        	result.put("res", "fail");
	        }
			return result;
	}
	
	@PostMapping("/getStockJisu")
	@ResponseBody
	public Map<String, String> getStockJisu(HttpServletRequest req, HttpServletResponse res, @RequestParam("date") String date) {
			String[] idxNm = {"KRX 300", "코스닥", "코스피"};
			
			Map<String, String> result = new HashMap<>();
			for(int i=0;i<idxNm.length;i++) { 
				String apiUrl = "https://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex?serviceKey="
						+ encodeKey
						+ "&endBasDt=" + date
						+ "&numOfRows=50"
						+ "&resultType=json";
		
				String encoded;
				try {
					encoded = URLEncoder.encode(idxNm[i], StandardCharsets.UTF_8.toString());
					apiUrl += "&idxNm=" + encoded;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				List<Map<String, Object>> jsonArray = getUrlAPI(apiUrl, "");
				if(jsonArray == null) {
					result.put("res", "fail"); 
					return result;
				}
				Map<String, String> field = new HashMap<>();
				
				field.put("idxNm", "ji_type");
				field.put("basDt", "ji_date");
				field.put("clpr", "ji_clpr");
				field.put("vs", "ji_vs");
				field.put("mkp", "ji_mkp");
				field.put("fltRt", "ji_fltRt");
				field.put("hipr", "ji_hipr");
				field.put("lopr", "ji_lopr");
				field.put("trqu", "ji_trqu");
				
				boolean chk = false;
				
				// jsonArray 값을 확인하기 위한 for문
		        for (Map<String, Object> item : jsonArray) {
		        	 StockJisuVO oldJisu = stockService.getOldJisu((String)item.get("basDt"), idxNm[i]);
		        	 
		        	 if(oldJisu != null) continue;
		        	
		        	 StockJisuVO jisu = new StockJisuVO(); //지수정보VO생성 
		            for (Map.Entry<String, Object> entry : item.entrySet()) {
		            	String jsonKey = entry.getKey();
		                Object value = entry.getValue();
		                String voFieldName = field.get(jsonKey);
		                if (voFieldName != null) {
		                	Field fields;
							try {
								fields = StockJisuVO.class.getDeclaredField(voFieldName);
								fields.setAccessible(true); // private 필드 접근
			                    fields.set(jisu, value); // 필드에 값 설정
							} catch (Exception e) {
								e.printStackTrace();
							}
		                }
		            }
		            
		            chk = stockService.insertStockJisu(jisu);
		            
		            if(chk) {
		            	result.put("res", "success"); 
		            } else {
		            	result.put("res", "fail");
		            	result.put("type" + i, idxNm[i]);
		            }
		        }
		        
			}
		return result;
	}
	
	@PostMapping("/updateAllStockInfo")
	@ResponseBody
	public Map<String, String> updateAllStockInfo(HttpServletRequest req, HttpServletResponse res, @RequestParam("date") String date) {
			String defaultUrl = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey="
					+ encodeKey
					+ "&endBasDt=" + date
					+ "&resultType=json";
			List<StockVO> allStocks = stockService.getCompanyList(null, null);
			Map<String, String> result = new HashMap<>();
			for(StockVO stOrg : allStocks) {
				String st_code = stOrg.getSt_code();
				String apiUrl = defaultUrl + "&isinCd=" + st_code;
				List<Map<String, Object>> jsonArray = getUrlAPI(apiUrl, "");
				if(jsonArray == null) {
					result.put("res", "fail"); 
				}
				Map<String, String> field = new HashMap<>();
				field.put("basDt", "si_date");
				field.put("clpr", "si_price");
				field.put("isinCd", "st_code");
				field.put("vs", "si_vs");
				field.put("fltRt", "si_fltRt");
				field.put("mrktTotAmt", "si_mrkTotAmt");
				field.put("hipr", "si_hipr");
				field.put("lopr", "si_lopr");
				field.put("trqu", "si_trqu");
				
				boolean chk = false;
				 // jsonArray 값을 확인하기 위한 for문
				int lastIndex = jsonArray.size() - 1; // 마지막 인덱스 계산
				int currentIndex = 0;
		        for (Map<String, Object> item : jsonArray) {
		        	 StockPriceVO stockPrice = new StockPriceVO(); // 새로운 StockVO 객체 생성
		        	 boolean isLastElement = (currentIndex == lastIndex); // 현재 요소가 마지막인지 여부
		            for (Map.Entry<String, Object> entry : item.entrySet()) {
		            	String jsonKey = entry.getKey();
		                Object value = entry.getValue();
		                String voFieldName = field.get(jsonKey);
		                if (voFieldName != null) {
		                	Field fields;
							try {
								fields = StockPriceVO.class.getDeclaredField(voFieldName);
								fields.setAccessible(true); // private 필드 접근
								if(voFieldName == "si_price") {
									value = Integer.parseInt((String) value); 
								}
			                    fields.set(stockPrice, value); // 필드에 값 설정
							} catch (Exception e) {
								e.printStackTrace();
							}
		                }
		            }
		            stockService.insertPrice(stockPrice);
		            if (isLastElement) {
		            	stockService.updateReservation(stockPrice);
		            }
		        }
			}
	        
			result.put("res", "success");
			return result;
	}
	
}