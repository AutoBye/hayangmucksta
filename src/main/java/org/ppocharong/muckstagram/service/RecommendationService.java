package org.ppocharong.muckstagram.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class RecommendationService {

    private final RestTemplate restTemplate;
    private final PreferredIngredientService preferredIngredientService;

    // RestTemplate 및 PreferredIngredientService 주입
    public RecommendationService(RestTemplate restTemplate, PreferredIngredientService preferredIngredientService) {
        this.restTemplate = restTemplate;
        this.preferredIngredientService = preferredIngredientService;
    }

    // 사용자 선호 재료를 기반으로 추천 요청
    public Map<String, Object> recommendByIngredients(Long userId) {
        try {
            // 사용자 선호 재료 목록을 가져옴
            List<String> preferredIngredients = preferredIngredientService.getPreferredIngredientsByUserId(userId);

            System.out.println("사용자 선호 재료: " + preferredIngredients);
            // Flask 서버의 엔드포인트 URL
            String flaskUrl = "http://localhost:5000/recommend"; // Flask 서버 URL

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 바디 설정
            Map<String, Object> requestBody = Map.of("ingredients", preferredIngredients);

            // 요청 엔티티 생성
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Flask 서버로 POST 요청 전송 및 응답 받기
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    flaskUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            // 응답 상태 코드 확인
            if (response.getStatusCode() == HttpStatus.OK) {
                // 응답 결과 반환
                System.out.println("Flask 서버로부터 받은 응답: " + response.getBody());
                return response.getBody();
            } else {
                System.out.println("Flask 서버로부터 오류 응답: " + response.getStatusCode());
                throw new RuntimeException("Flask 서버 요청 실패: 상태 코드 " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            // Flask 서버에서 4xx 오류가 발생했을 때
            System.out.println("클라이언트 오류: " + e.getMessage());
            throw new RuntimeException("Flask 서버 요청 중 클라이언트 오류 발생: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            // Flask 서버에서 5xx 오류가 발생했을 때
            System.out.println("서버 오류: " + e.getMessage());
            throw new RuntimeException("Flask 서버 요청 중 서버 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            // 기타 오류 발생 시
            System.out.println("알 수 없는 오류: " + e.getMessage());
            throw new RuntimeException("Flask 서버 요청 중 오류 발생: " + e.getMessage());
        }
    }
}
