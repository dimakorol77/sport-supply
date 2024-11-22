package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.enums.Role;
import org.example.exceptions.CartItemNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.CartItemService;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartItemController cartItemController;

    private ObjectMapper objectMapper;

    private CartItemDto cartItemDto;
    private CartItemResponseDto cartItemResponseDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(cartItemController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        cartItemDto = new CartItemDto();
        cartItemDto.setProductId(1L);
        cartItemDto.setQuantity(2);

        cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setProductId(1L);
        cartItemResponseDto.setQuantity(2);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        // Устанавливаем SecurityContext с аутентифицированным пользователем
        setAuthentication("test@example.com");
    }

    @Test
    void testAddItemToCart_Success() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(cartItemService.addItemToCart(eq(1L), eq(user.getId()), any(CartItemDto.class))).thenReturn(cartItemResponseDto);

        String cartItemJson = objectMapper.writeValueAsString(cartItemDto);

        mockMvc.perform(post("/api/cart-items/{cartId}/items", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartItemJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is(cartItemResponseDto.getProductId().intValue())))
                .andExpect(jsonPath("$.quantity", is(cartItemResponseDto.getQuantity())));
    }

    @Test
    void testUpdateCartItemQuantity_Success() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(cartItemService.updateCartItemQuantity(eq(1L), eq(user.getId()), eq(3))).thenReturn(cartItemResponseDto);

        mockMvc.perform(put("/api/cart-items/items/{cartItemId}", 1)
                        .param("quantity", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(cartItemResponseDto.getProductId().intValue())))
                .andExpect(jsonPath("$.quantity", is(cartItemResponseDto.getQuantity())));
    }

    @Test
    void testRemoveCartItem_Success() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        doNothing().when(cartItemService).removeCartItem(eq(1L), eq(user.getId()));

        mockMvc.perform(delete("/api/cart-items/items/{cartItemId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemoveCartItem_AccessDenied() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        doThrow(new AccessDeniedException(ErrorMessage.ACCESS_DENIED))
                .when(cartItemService).removeCartItem(eq(1L), eq(user.getId()));

        mockMvc.perform(delete("/api/cart-items/items/{cartItemId}", 1))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ErrorMessage.ACCESS_DENIED));
    }

    // Метод для установки аутентификации
    private void setAuthentication(String email) {
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User(email, "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
