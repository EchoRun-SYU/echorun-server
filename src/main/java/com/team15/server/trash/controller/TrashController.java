package com.team15.server.trash.controller;

import com.team15.server.trash.dto.TrashAnalyzeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/trash")
@Tag(name = "Trash API", description = "AI 쓰레기 분석 및 플로깅 인증 관련 API입니다.")
public class TrashController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "AI 쓰레기 이미지 분석 API", description = "제미나이 API를 활용하여 실제 이미지를 분석하고 결과를 반환합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TrashAnalyzeResponse.class))
    )
    public ResponseEntity<?> analyzeTrash(@RequestPart("image") MultipartFile image) {

        if (image == null || image.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지 파일이 비어있습니다.");
        }

        try {
            // 이미지를 Base64로 인코딩 (제미나이 전송용)
            byte[] fileContent = image.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileContent);

            // 제미나이 API 엔드포인트 URL 설정
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey;
            // 제미나이에게 내릴 프롬프트
            String prompt = "너는 플로깅 인증 검증 AI이자 쓰레기 분리수거 전문가야. " +
                    "사진 속 쓰레기를 분석해서 실제 플로깅 상황인지 판단해. " +
                    "다음 조건 중 하나라도 해당하면 suspicious를 true로 판단해: " +
                    "쓰레기가 이미 쓰레기통 안에 있음, " +
                    "실내 환경으로 보임, " +
                    "동일한 물체를 반복 촬영한 느낌, " +
                    "사람이 일부러 배치한 듯 매우 부자연스러움, " +
                    "쓰레기가 너무 깨끗하거나 새것처럼 보임. " +
                    "반드시 아래 JSON 형식으로만 답변하고 다른 말은 절대 하지마.\n" +
                    "{\n" +
                    "  \"trashType\": \"PLASTIC 또는 GLASS 또는 PAPER 또는 CAN 또는 OTHER 중 하나\",\n" +
                    "  \"confidence\": 0.0에서 1.0 사이의 신뢰도 실수,\n" +
                    "  \"suspicious\": true 또는 false,\n" +
                    "  \"suspiciousReason\": \"의심 사유 한 줄\",\n" +
                    "  \"message\": \"해당 쓰레기의 올바른 분리수거 지침 1줄 요약\"\n" +
                    "}";

            // 제미나이 규격에 맞는 JSON 바디 조립
            JSONObject requestBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject partsObj = new JSONObject();
            JSONArray parts = new JSONArray();

            // 텍스트 프롬프트 추가
            JSONObject textPart = new JSONObject().put("text", prompt);
            parts.put(textPart);

            // 이미지 데이터 추가 (인라인 데이터 형태)
            JSONObject imagePart = new JSONObject()
                    .put("inlineData", new JSONObject()
                            .put("mimeType", image.getContentType())
                            .put("data", base64Image));
            parts.put(imagePart);

            partsObj.put("parts", parts);
            contents.put(partsObj);
            requestBody.put("contents", contents);

            // HTTP 요청 날리기
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);

            // 제미나이가 준 응답에서 원하는 JSON 빼오기
            JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
            String rawText = jsonResponse.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            // 정제 처리
            String cleanJson = rawText.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .replaceAll("\r", "")
                    .replaceAll("\n", "")
                    .trim();
            JSONObject resultJson = new JSONObject(cleanJson);

            // 결과 매핑해서 최종 DTO 리턴
            TrashAnalyzeResponse response = new TrashAnalyzeResponse(
                    resultJson.getString("trashType"),
                    50, // 포인트는 우리 서버 기준 고정 50점 지급
                    resultJson.getDouble("confidence"),
                    resultJson.getString("message")
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("AI 분석 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}