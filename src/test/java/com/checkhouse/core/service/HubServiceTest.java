package com.checkhouse.core.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HubServiceTest {
    @BeforeAll
    public static void onlyOnce() {}

    @BeforeEach
    void setup() {}

    @Test
    void SUCCESS_addHub() {
        // 허브 정보가 주어짐

        // 허브 저장

        // 저장된 허브 정보 확인
    }

    @Test
    void SUCCESS_updateHub() {
        // 허브 ID, 수정된 허브 정보

        // 수정된 허브 저장

        // 저장된 허브 정보 일치 확인
    }

    @Test
    void SUCCESS_deleteHub() {
        // 허브 ID

        // 허브 조회

        // 허브 삭제
    }

    /**
     * 입력된 주소에 해당하는 허브를 배정
     */
    @Test
    void SUCCESS_allocateHub() {
        // 주소

        // 허브 할당

        // when
    }
    @DisplayName("허브 리스트 조회 성공")
    @Test
    void SUCCESS_getHubList() {}

    /**
     * 허브 저장이 실패하는 경우는 다음과 같음
     * 1. 이미 존재하는 허브
     * 2. 잘못된 주소
     * 3. 잘못된 존 매핑
     *
     */
    @DisplayName("이미 존재하는 경우 허브 저장 실패")
    @Test
    void FAIL_addHub_already_exist() {
        // 허브 정보가 주어짐

        // 허브 저장

        // 저장된 허브 정보 확인
    }
    @DisplayName("잘못된 주소의 경우 허브 저장 실패")
    @Test
    void FAIL_addHub_invalid_address() {
        // 허브 정보가 주어짐

        // 허브 저장

        // 저장된 허브 정보 확인
    }

    @DisplayName("잘못된 존 매핑의 경우 허브 저장 실패")
    @Test
    void FAIL_addHub_invalid_zone() {
        // 허브 정보가 주어짐

        // 허브 저장

        // 저장된 허브 정보 확인
    }

    /**
     * 허브 정보 수정이 실패하는 경우는 다음과 같음
     * 1. 존재하지 않는 허브
     * 2. 잘못된 주소 수정
     * 3. 존재하지 않는 존 ID
     */
    @DisplayName("존재하지 않는 허브의 경우 허브 정보 수정 실패")
    @Test
    void FAIL_updateHub_not_found_hub() {

    }

    @DisplayName("잘못된 주소의 경우 허브 정보 수정 실패")
    @Test
    void FAIL_updateHub_invalid_address() {

    }

    @DisplayName("수정한 담당 존이 존재하지 않는 경우 정보 수정 실패")
    @Test
    void FAIL_updateHub_invalid_zone() {

    }

    /**
     * 허브 삭제에 실패하는 경우는 다음과 같음
     * 1. 존재하지 않는 허브
     */
    @DisplayName("존재하지 않는 허브의 경우 허브 삭제 실패")
    @Test
    void FAIL_deleteHub_not_found_hub() {}
    /**
     * 허브 할당에 실패하는 경우는 다음과 같음
     * 1. 잘못된 주소
     * 2. 매핑할 허브가 없는 경우
     */
    @DisplayName("잘못된 주소의 경우 허브 할당 실패")
    @Test
    void FAIL_allocHub_invalid_address() {}

    @DisplayName("매핑할 허브가 없는 주소의 경우 실패")
    @Test
    void FAIL_allocHub_not_found_zone() {}

}
