package com.checkhouse.core.controller;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.checkhouse.core.service.InspectionService;

@WebMvcTest(InspectionController.class)
@AutoConfigureMockMvc
public class InspectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InspectionService inspectionService;

    @BeforeEach
    void setup() {

    }
    //검수 등록 성공
    @Test
    @DisplayName("검수 등록 성공")
    void addInspectionSuccess() throws Exception {

    }
    //검수 상태 업데이트 성공
    @Test
    @DisplayName("검수 상태 업데이트 성공")
    void updateInspectionSuccess() throws Exception {

    }
    //검수 삭제 성공
    @Test
    @DisplayName("검수 삭제 성공")
    void deleteInspectionSuccess() throws Exception {

    }
    //검수 설명 수정 성공
    @Test
    @DisplayName("검수 설명 수정 성공")
    void updateInspectionDescriptionSuccess() throws Exception {

    }
    //검수 리스트 조회 성공
    @Test
    @DisplayName("검수 리스트 조회 성공")
    void getInspectionListSuccess() throws Exception {

    }
    // todo: 검수 사진 등록 성공
    @Test
    @DisplayName("검수 사진 등록 성공")
    void addInspectionPhotoSuccess() throws Exception {

    }
}
