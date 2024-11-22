package org.example.controllers;

import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.ImageDto;
import org.example.exceptions.ImageNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    private ImageDto imageDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();

        imageDto = new ImageDto();
        imageDto.setId(1L);
        imageDto.setProductId(1L);
        imageDto.setUrl("http://example.com/image.jpg");

        // Устанавливаем SecurityContext с аутентифицированным пользователем с ролью ADMIN
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("admin@example.com", "",
                        Collections.singletonList(() -> "ROLE_ADMIN"));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testUploadImage_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "test image content".getBytes());

        when(imageService.uploadImage(eq(1L), any())).thenReturn(imageDto);

        mockMvc.perform(multipart("/images/upload/{productId}/", 1)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(imageDto.getId().intValue())))
                .andExpect(jsonPath("$.productId", is(imageDto.getProductId().intValue())))
                .andExpect(jsonPath("$.url", is(imageDto.getUrl())));
    }

    @Test
    void testGetImagesByProductId() throws Exception {
        when(imageService.getImagesByProductId(1L)).thenReturn(Arrays.asList(imageDto));

        mockMvc.perform(get("/images/product/{productId}/", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(imageDto.getId().intValue())))
                .andExpect(jsonPath("$[0].productId", is(imageDto.getProductId().intValue())))
                .andExpect(jsonPath("$[0].url", is(imageDto.getUrl())));
    }
    @Test
    void testDeleteImage_Success() throws Exception {
        doNothing().when(imageService).deleteImage(1L);

        mockMvc.perform(delete("/images/{imageId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteImage_NotFound() throws Exception {
        doThrow(new ImageNotFoundException(ErrorMessage.IMAGE_NOT_FOUND)).when(imageService).deleteImage(1L);

        mockMvc.perform(delete("/images/{imageId}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.IMAGE_NOT_FOUND));
    }
}
