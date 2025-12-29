package com.human.dalligo.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import com.human.dalligo.dao.LshTripDAO;
import com.human.dalligo.service.LshDistanceService;
import com.human.dalligo.service.LshEventService;
import com.human.dalligo.service.LshTripService;
import com.human.dalligo.vo.JSUserVO;
import com.human.dalligo.vo.LshApplyListVO;
import com.human.dalligo.vo.LshEventVO;
import com.human.dalligo.vo.LshTripVO;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LshTripController {
	
	private final LshTripDAO tripDAO;
    private final LshTripService tripService;
    private final LshEventService eventService;
    private final LshDistanceService distanceService;
    private final RestTemplate restTemplate = new RestTemplate();

    /** 이벤트의 Trip 상세 조회 
     * @param startCity */
    @GetMapping("/events/{id}/trips")
    public String tripDetailForEvent(
            @PathVariable("id") int eventId,
            @SessionAttribute("loginUser") JSUserVO loginUser,
            Model model, Object startCity) {
    	
    	// 🔐 비회원 접근 차단 (Whitelabel 방지)
        if (loginUser == null) {
            return "redirect:/events";
        }

        String userAddr = loginUser.getAddress();
        //System.out.printf("userAddr = "+userAddr);
        model.addAttribute("loginUser", loginUser);

        LshEventVO event = eventService.getEvent(eventId);
        LshTripVO trip = tripService.getTripByEvent(eventId);

        // Trip 없으면 자동 생성
        if (trip == null) {
            trip = tripService.createTripFromEvent(event, loginUser.getUserId());
        } else {
            // distance 갱신
            String startCity1 = tripService.extractCity(loginUser.getAddress());
            String endCity = tripService.extractCity(event.getLocation());

            Integer distanceKm = distanceService.getDistance(startCity1, endCity);
            BigDecimal distance = distanceKm != null ? BigDecimal.valueOf(distanceKm) : BigDecimal.ZERO;
            trip.setDistance(distance);

            int cost = tripService.calculateCost(distance);
            trip.setCost(cost);

            tripDAO.updateTrip(trip); // DB 반영
        }

        model.addAttribute("event", event);
        model.addAttribute("startDate",
                Date.from(event.getStartDate().atZone(ZoneId.systemDefault()).toInstant()));
        model.addAttribute("endDate",
                Date.from(event.getEndDate().atZone(ZoneId.systemDefault()).toInstant()));
        model.addAttribute("trip", trip);
        model.addAttribute("minPeople", 25);

        // 출발/도착 도시 계산
        model.addAttribute("startCity", tripService.extractCity(userAddr));
        model.addAttribute("endCity", tripService.extractCity(event.getLocation()));
        
		// ✅ cities 테이블의 주소 조회 (이미 있는 서비스 가정)
        String cityName1=tripService.extractCity(userAddr);
        String cityName2=tripService.extractCity(event.getLocation());
        //System.out.println("cityName = "+cityName);
        model.addAttribute("startCityAddr", tripService.getCityAddress(cityName1));
        model.addAttribute("endCityAddr", tripService.getCityAddress(cityName2));
        // cities 테이블에서 place 조회
        model.addAttribute("startCityPlace", tripService.getCityPlace(cityName1));
        model.addAttribute("endCityPlace", tripService.getCityPlace(cityName2));


        return "trip/detail";
    }


    // 거리 업데이트 : trip객체에 distance를 같이 넘겨야 함
    @GetMapping("/trips/{id}")
    public String showTripDetail(@PathVariable("id") int tripId, Model model) {

        LshTripVO trip = tripService.getTripById(tripId);
        model.addAttribute("trip", trip);

        String startCity = tripService.extractCity(trip.getStartCity());
        String endCity   = tripService.extractCity(trip.getEndCity());

        // 거리 계산 및 cost 계산
        BigDecimal distance;
        int cost;

        if (startCity.equals(endCity)) {
            distance = BigDecimal.ZERO;
            cost = 0;
        } else {
            Integer distKm = distanceService.getDistance(startCity, endCity);
            distance = (distKm == null || distKm <= 0) ? BigDecimal.ZERO : BigDecimal.valueOf(distKm);
            cost = tripService.calculateCost(distance);
            if ("제주".equals(startCity) || "제주".equals(endCity)) cost += 110_000;
        }

        trip.setDistance(distance);
        trip.setCost(cost);
        tripDAO.updateTrip(trip);

        model.addAttribute("distance", distance);
        model.addAttribute("cost", cost);

        return "trip/detail"; // <- 반드시 필요
    }



    /** 이벤트 신청 */
    @PostMapping("/apply")
    @ResponseBody
    public Map<String, Object> apply(
            @SessionAttribute("loginUser") JSUserVO loginUser,
            @RequestBody Map<String, Object> req) {

        Map<String, Object> res = new HashMap<>();

        if (loginUser == null) {
            res.put("ok", false);
            res.put("message", "로그인이 필요합니다.");
            res.put("currentPeople", 0);
            return res;
        }

        String userId = loginUser.getUserId();

        // event_id 또는 eventId 둘 다 허용
        Object e1 = req.get("event_id");
        Object e2 = req.get("eventId");
        Integer eventId = null;
        if (e1 instanceof Number) eventId = ((Number)e1).intValue();
        else if (e2 instanceof Number) eventId = ((Number)e2).intValue();
        else if (e1 instanceof String) eventId = Integer.valueOf((String)e1);
        else if (e2 instanceof String) eventId = Integer.valueOf((String)e2);

        if (eventId == null) {
            res.put("ok", false);
            res.put("message", "event_id 가 전달되지 않았습니다.");
            res.put("currentPeople", 0);
            return res;
        }

        int applied = tripService.applyToEvent(eventId, userId);

       // ✅ 핵심 수정 부분 : applyToTrip 서비스 메소드로부터 신청일자를 판단하여 리턴 -> 컨트롤러는 해석용으로 필요함
        if (applied == 1) {
            res.put("ok", true);
            res.put("message", "신청이 완료되었습니다.");
        } else if (applied == -1) {
            res.put("ok", false);
            res.put("message", "이미 신청하셨습니다.");
        } else if (applied == -2) {
            res.put("ok", false);
            res.put("message", "신청 불가합니다. 이미 지난 대회입니다.");
        } else {
            res.put("ok", false);
            res.put("message", "신청 처리 중 오류가 발생했습니다.");
        }

        res.put("currentPeople", tripService.getCurrentPeople(eventId));
        return res;
    }

    @PostMapping("/trip/cancel")
    @ResponseBody
    public Map<String, Object> cancelTrip(
            @SessionAttribute("loginUser") JSUserVO loginUser,
            @RequestBody Map<String, Object> payload) {

        Map<String, Object> result = new HashMap<>();

        if (loginUser == null) {
            result.put("ok", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        Object ev = payload.get("eventId");
        Integer eventId = null;
        if (ev instanceof Number) eventId = ((Number)ev).intValue();
        else if (ev instanceof String) eventId = Integer.valueOf((String) ev);

        if (eventId == null) {
            result.put("ok", false);
            result.put("message", "eventId 누락");
            return result;
        }

        String userId = loginUser.getUserId();
        boolean ok = tripService.cancelApplication(eventId, userId);

        result.put("ok", ok);
        result.put("message", ok ? "신청이 취소되었습니다." : "취소 실패 또는 이미 취소됨");
        result.put("currentPeople", tripService.getCurrentPeople(eventId)); // 최신 카운트 반환
        return result;
    }


    /** 신청 목록 조회 */
    @GetMapping("/trip/list")
    public String applyList(Model model) {
        List<LshApplyListVO> applyLists = tripService.getAllApplicationsWithEventInfo();
        model.addAttribute("applyLists", applyLists);
        return "trip/list";
    }

    /** (선택) 이벤트 상세 → Trip 상세 이동 */
    @GetMapping("/events/{id}/trip-detail")
    public String eventTripDetail(
            @PathVariable("id") int id,
            @SessionAttribute("loginUser") JSUserVO loginUser,
            Model model) {

        LshEventVO event = eventService.getEvent(id);
        LshTripVO trip = tripService.getTripByUserAndEvent(loginUser.getUserId(), id);

        model.addAttribute("event", event);
        model.addAttribute("tripDetail", trip);

        return "trip/detail";
    }
    
    
    @Value("${kakao.rest.api-key}")
    private String kakaoRestApiKey;

    @GetMapping("/kakao/route")
    @ResponseBody
    public Map<String, Object> getRoute(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {

        // 1️⃣ 출발 / 도착 좌표
        Map<String, Double> startCoord = getCoordinate(start);
        Map<String, Double> endCoord   = getCoordinate(end);

        // 2️⃣ 출발지 == 도착지 → 지도보기(마커만)
        if (startCoord.get("lat").equals(endCoord.get("lat")) &&
            startCoord.get("lng").equals(endCoord.get("lng"))) {

            Map<String, Object> result = new HashMap<>();
            result.put("sameLocation", true);
            result.put("center", Map.of(
                "lat", startCoord.get("lat"),
                "lng", startCoord.get("lng")
            ));
            result.put("points", List.of());   // 경로 없음
            result.put("distance", 0);

            return result;
        }

        // 3️⃣ 출발지 ≠ 도착지 → 기존 Directions API 로직 그대로
        String url = String.format(
            "https://apis-navi.kakaomobility.com/v1/directions" +
            "?origin=%f,%f&destination=%f,%f&priority=RECOMMEND",
            startCoord.get("lng"), startCoord.get("lat"),
            endCoord.get("lng"), endCoord.get("lat")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();

        Map<String, Object> route =
            (Map<String, Object>) ((List<?>) body.get("routes")).get(0);

        Map<String, Object> summary =
            (Map<String, Object>) route.get("summary");

        int distance = (int) summary.get("distance");

        List<Map<String, Object>> roads =
            (List<Map<String, Object>>) ((Map<?, ?>)
                ((List<?>) route.get("sections")).get(0)).get("roads");

        List<Map<String, Double>> points = new ArrayList<>();

        for (Map<String, Object> road : roads) {
            List<Double> vertexes = (List<Double>) road.get("vertexes");
            for (int i = 0; i < vertexes.size(); i += 2) {
                points.add(Map.of(
                    "lng", vertexes.get(i),
                    "lat", vertexes.get(i + 1)
                ));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("sameLocation", false);
        result.put("distance", distance);
        result.put("points", points);
        result.put("center", Map.of(
            "lat", startCoord.get("lat"),
            "lng", startCoord.get("lng")
        ));

        return result;
    }
    
    private Map<String, Double> getCoordinate(String query) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        List<String> candidates = buildSearchQueries(query);

        for (String q : candidates) {
            try {
                String encoded = q;

                /* 1️⃣ Address API */
                String addressUrl =
                    "https://dapi.kakao.com/v2/local/search/address.json"
                        + "?query=" + encoded
                        + "&page=1"
                        + "&size=1";

                ResponseEntity<Map> response =
                    restTemplate.exchange(addressUrl, HttpMethod.GET, entity, Map.class);

                Map body = response.getBody();
                System.out.println("ADDRESS TRY [" + q + "] => " + body);

                if (body != null) {
                    List docs = (List) body.get("documents");
                    if (docs != null && !docs.isEmpty()) {

                        Map first = (Map) docs.get(0);
                        Map road = (Map) first.get("road_address");
                        Map addr = (Map) first.get("address");

                        Map target = null;

                        if (road != null && road.get("x") != null && road.get("y") != null) {
                            target = road;
                        } else if (addr != null && addr.get("x") != null && addr.get("y") != null) {
                            target = addr;
                        }

                        if (target != null) {
                            return Map.of(
                                "lat", Double.parseDouble(target.get("y").toString()),
                                "lng", Double.parseDouble(target.get("x").toString())
                            );
                        }
                    }
                }

                /* 2️⃣ Keyword API */
                String keywordUrl =
                    "https://dapi.kakao.com/v2/local/search/keyword.json"
                        + "?query=" + encoded
                        + "&page=1"
                        + "&size=1";

                response =
                    restTemplate.exchange(keywordUrl, HttpMethod.GET, entity, Map.class);

                body = response.getBody();
                System.out.println("KEYWORD TRY [" + q + "] => " + body);

                if (body != null) {
                    List docs = (List) body.get("documents");
                    if (docs != null && !docs.isEmpty()) {
                        Map first = (Map) docs.get(0);
                        return Map.of(
                            "lat", Double.parseDouble(first.get("y").toString()),
                            "lng", Double.parseDouble(first.get("x").toString())
                        );
                    }
                }

            } catch (Exception e) {
                System.out.println("FAIL QUERY => " + q);
                e.printStackTrace();
            }
        }

        throw new RuntimeException("좌표 변환 실패: " + query);
    }




    private List<String> buildSearchQueries(String origin) {
        List<String> list = new ArrayList<>();

        if (origin == null || origin.isBlank()) {
            return list;
        }

        origin = origin.trim();

        // 1. 원본
        list.add(origin);

        // 2. "수원시 " 제거
        list.add(origin.replaceAll("^.*?시\\s*", ""));

        // 3. "권선구 " 제거
        list.add(origin.replaceAll("^.*?구\\s*", ""));

        // 4. 번지 제거
        list.add(origin.replaceAll("\\s\\d+.*$", ""));

        // 5. 도로명만
        list.add(origin.replaceAll("^.*?(대로|로)", "$1"));

        // 중복 제거
        return list.stream().distinct().toList();
    }

//    @PostConstruct
//    public void check() {
//        System.out.println("KAKAO KEY = " + kakaoRestApiKey);
//    }


}