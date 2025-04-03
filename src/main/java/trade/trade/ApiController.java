package trade.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class ApiController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용
    private final XmlMapper xmlMapper = new XmlMapper(); // XML 파싱용

    @Value("${api.key}")
    private String apiKey;  // application.properties에서 API Key를 가져옵니다.

    // 사용자로부터 주소를 받아 API 요청
    @GetMapping("/getAddress")
    public String getAddress(Model model) {
        // 입력값을 이용한 URL 빌드
        String searchSe = "road";  // 도로명 주소
        String srchwrd = "주월동 408-1";  // 사용자 입력값으로 검색어 설정
        int countPerPage = 10;  // 페이지당 10개 항목
        int currentPage = 1;  // 첫 번째 페이지

        // URL 빌드
        String url = UriComponentsBuilder.fromHttpUrl("http://openapi.epost.go.kr:80/postal/retrieveNewAdressAreaCdService/retrieveNewAdressAreaCdService/getNewAddressListAreaCd")
                .queryParam("ServiceKey", apiKey)  // 인증키 추가
                .queryParam("searchSe", searchSe)  // 고정된 검색 구분
                .queryParam("srchwrd", srchwrd)    // 사용자 입력값을 검색어로 설정
                .queryParam("countPerPage", countPerPage)  // 페이지당 출력 개수
                .queryParam("currentPage", currentPage)    // 페이지 번호
                .toUriString();

        // API 호출 및 응답을 XML 형식으로 받기
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        System.out.println(response);
        // 응답이 정상적으로 오면 XML을 JSON으로 변환하여 출력
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                // XML -> JSON 변환
                String xmlResponse = response.getBody();
                // XML을 Java 객체로 변환
                Object xmlObject = xmlMapper.readValue(xmlResponse, Object.class);
                // Java 객체를 JSON 문자열로 변환
                String jsonResponse = objectMapper.writeValueAsString(xmlObject);

                // 모델에 JSON 응답 추가
                model.addAttribute("addressData", jsonResponse);
            } catch (Exception e) {
                model.addAttribute("error", "XML 처리 오류: " + e.getMessage());
            }
        } else {
            model.addAttribute("error", "API 호출 실패: " + response.getStatusCode());
        }

        System.out.println(response);

        // 결과를 HTML 템플릿에서 렌더링하도록 반환
        return "address";  // address.html 파일을 렌더링
    }
}
