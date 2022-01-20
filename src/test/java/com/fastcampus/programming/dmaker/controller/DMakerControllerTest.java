package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.service.DMakerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.fastcampus.programming.dmaker.type.DeveloperLevel.JUNIOR;
import static com.fastcampus.programming.dmaker.type.DeveloperLevel.SENIOR;
import static com.fastcampus.programming.dmaker.type.DeveloperSkillType.BACK_END;
import static com.fastcampus.programming.dmaker.type.DeveloperSkillType.FRONT_END;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DMakerController.class)
class DMakerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dmakerService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    UTF_8
            );

    @Test
    void getAllDevelopers() throws Exception {
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(BACK_END)
                .developerLevel(JUNIOR)
                .memberId("memberId1")
                .build();

        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(FRONT_END)
                .developerLevel(SENIOR)
                .memberId("memberId2")
                .build();
        given(dmakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.[0].developerSkillType",
                                is(BACK_END.name()))
                ).andExpect(
                jsonPath("$.[0].developerLevel",
                        is(JUNIOR.name()))
        ).andExpect(
                jsonPath("$.[1].developerSkillType",
                        is(FRONT_END.name())
                        )
        ).andExpect(
                jsonPath("$.[1].developerLevel",
                        is(SENIOR.name())
                        )
        );
    }
}