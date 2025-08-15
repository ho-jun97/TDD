package com.example.tdd.membership.controller;

import com.example.tdd.dto.MembershipDetailResponse;
import com.example.tdd.dto.MembershipRequest;
import com.example.tdd.dto.MembershipAddResponse;
import com.example.tdd.entity.MembershipType;
import com.example.tdd.exception.GlobalExceptionHandler;
import com.example.tdd.exception.MembershipErrorResult;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.membership.service.MembershipService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.example.tdd.constants.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController target;

    private MockMvc mockMvc;

    private Gson gson;

    @Mock
    private MembershipService membershipService;

    @BeforeEach
    void init() {
        gson = new Gson();
        mockMvc =  MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @ParameterizedTest
    @MethodSource("invalidedMembershipAddParameter") // 파라미터를 작성한 메소드 이름
    public void membershipRegisterFailByParameter(final Integer point, final MembershipType membershipType) throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(point, membershipType)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }
    public static Stream<Arguments> invalidedMembershipAddParameter() {
        return Stream.of(
                Arguments.of(null, MembershipType.NAVER),
                Arguments.of(-1, MembershipType.NAVER),
                Arguments.of(10000,null)
        );
    }



    @Test
    @DisplayName("멤버십등록실패 사용자 식별값이 헤더에 없음")
    public void idNotExistInHeader() throws Exception {
        // given
        final String url = "/api/v1/memberships";


        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십등록실패 포인트 null")
    public void pointIsNull() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십등록실패 포인트 음수")
    public void pointIsMinus() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("멤버십등록실패 멤버십 종류 null")
    public void MembershipTypeIsNull() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십등록실패 MemberShipService에서 Error Throw")
    public void ErrorThrowByMembershipService() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("12345", MembershipType.NAVER, 10000);
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십 등록 성공")
    public void membershipRegisterSuccess() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        final MembershipAddResponse membershipAddResponse = MembershipAddResponse.builder()
                .id(-1L)
                .membershipType(MembershipType.NAVER)
                .build();

        doReturn(membershipAddResponse).when(membershipService).addMembership("12345", MembershipType.NAVER, 10000);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());

        final MembershipAddResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MembershipAddResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
    }

    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }

    @Test
    @DisplayName("mockMvc Null 아님")
    public void mockMvcNotNull() throws Exception {

        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("멤버십목록조회 실패 사용자 식별값이 헤더에 없음")
    void getMemberFailByNoHeaderValue() throws Exception {
        // given
        final String url = "/api/v1/memberships";;

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십상세조회 실패_사용자식별값이 헤더에 없음")
    void getDetailMembershipFailNotHeaderValue() throws Exception{
        // given
        final String url = "/api/v1/memberships";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십상세조회 실패_멤버십이 존재하지않음")
    void getDetailMembershipFailNotFound() throws Exception{
        // given
        final String url = "/api/v1/memberships/-1";
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
                .when(membershipService).getMembership(-1L, "12345");

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("멤버십 상세조회 성공")
    void getDetailMembershipSuccess() throws Exception{
        // given
        final String url = "/api/v1/memberships/-1";
        doReturn(MembershipDetailResponse.builder().build())
                .when(membershipService).getMembership(-1L, "12345");

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "12345")
//                        .param("membershipType", MembershipType.NAVER.name())
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("멤버십삭제실패_사용자식별값이헤더에없음")
    void membershipDeleteFailByNoHeaderValue() throws Exception{
        // given
        final String url = "/api/v1/memberships/-1";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십삭제 성공")
    void membershipDeleteSuccess() throws Exception{
        // given
        final String url = "/api/v1/memberships/-1";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(USER_ID_HEADER, "12345")
        );

        resultActions.andExpect(status().isNoContent());
    }
}
