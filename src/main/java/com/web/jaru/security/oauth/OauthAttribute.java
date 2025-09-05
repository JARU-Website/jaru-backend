package com.web.jaru.security.oauth;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OauthAttribute {
    private Map<String,Object> attributes;
    private String attributeKey;
    private String email;
    private String picture;
    private String phoneNum;


    public static OauthAttribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(attributeKey,attributes);
            case "kakao":
                return ofKakao("email", attributes);
            case "naver":
                return ofNaver("id",attributes);
            default:
                throw new RuntimeException();

        }
    }

    private static OauthAttribute ofGoogle(String attributeKey, Map<String,Object> attributes) {
        return OauthAttribute.builder()
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributeKey(attributeKey)
                .attributes(attributes)
                .build();
    }

    private static OauthAttribute ofNaver(String attributeKey, Map<String,Object> attributes){
        Map<String,Object> response = (Map<String, Object>) attributes.get("response");

        return OauthAttribute.builder()
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .phoneNum((String) response.get("mobile"))  // 네이버에만 추가
                .attributeKey(attributeKey)
                .attributes(response)
                .build();
    }


    private static OauthAttribute ofKakao(String attributeKey, Map<String, Object> attributes) {
        // kakao_account가 없으면 null 처리
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = null;
        String picture = null;

        if (kakaoAccount != null) {
            //  email = (String) kakaoAccount.get("email");
            // kakaoAccount 내에 profile이 있는지 확인
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
            if (kakaoProfile != null) {
                email = (String) kakaoProfile.get("nickname");
                picture = (String) kakaoProfile.get("profile_image_url");
            }
        }

        return OauthAttribute.builder()
                .email(email)
                .picture(picture)
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }


    public Map<String,Object> convertToMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("picture", picture);

        if (phoneNum != null) {  // 네이버 OAuth에서만 추가
            map.put("phoneNum", phoneNum);
        }

        return map;
    }

}
