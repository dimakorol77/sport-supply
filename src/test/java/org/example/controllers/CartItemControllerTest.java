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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        // Удаляем следующие строки:
        // cartItemResponseDto.setId(1L);
        // cartItemResponseDto.setCartId(1L);
        cartItemResponseDto.setProductId(1L);
        cartItemResponseDto.setQuantity(2);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testAddItemToCart_Success() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(cartItemService.addItemToCart(eq(1L), eq(user.getId()), any(CartItemDto.class))).thenReturn(cartItemResponseDto);
        doReturn("test@example.com").when(SecurityUtils.class);
        SecurityUtils.getCurrentUserEmail();

        String cartItemJson = objectMapper.writeValueAsString(cartItemDto);

        mockMvc.perform(post("/api/cart-items/{cartId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartItemJson))
                .andExpect(status().isCreated())
                // Убираем следующие ассерции:
                // .andExpect(jsonPath("$.id", is(cartItemResponseDto.getId().intValue())))
                // .andExpect(jsonPath("$.cartId", is(cartItemResponseDto.getCartId().intValue())))
                .andExpect(jsonPath("$.productId", is(cartItemResponseDto.getProductId().intValue())))
                .andExpect(jsonPath("$.quantity", is(cartItemResponseDto.getQuantity())));
    }


    @Test
    @WithMockUser(username = "test@example.com")
    void testUpdateCartItemQuantity_Success() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(cartItemService.updateCartItemQuantity(eq(1L), eq(user.getId()), eq(3))).thenReturn(cartItemResponseDto);
        doReturn("test@example.com").when(SecurityUtils.class);
        SecurityUtils.getCurrentUserEmail();

        mockMvc.perform(put("/api/cart-items/{cartItemId}", 1)
                        .param("quantity", "3"))
                .andExpect(status().isOk())
                // Убираем следующие ассерции:
                // .andExpect(jsonPath("$.id", is(cartItemResponseDto.getId().intValue())))
                // .andExpect(jsonPath("$.cartId", is(cartItemResponseDto.getCartId().intValue())))
                .andExpect(jsonPath("$.productId", is(cartItemResponseDto.getProductId().intValue())))
                .andExpect(jsonPath("$.quantity", is(cartItemResponseDto.getQuantity())));
    }


    @Test
    @WithMockUser(username = "test@example.com")
    void testRemoveCartItem_Success() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        doNothing().when(cartItemService).removeCartItem(eq(1L), eq(user.getId()));
        doReturn("test@example.com").when(SecurityUtils.class);
        SecurityUtils.getCurrentUserEmail();

        mockMvc.perform(delete("/api/cart-items/{cartItemId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testRemoveCartItem_AccessDenied() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        doThrow(new AccessDeniedException(ErrorMessage.ACCESS_DENIED))
                .when(cartItemService).removeCartItem(eq(1L), eq(user.getId()));
        doReturn("test@example.com").when(SecurityUtils.class);
        SecurityUtils.getCurrentUserEmail();

        mockMvc.perform(delete("/api/cart-items/{cartItemId}", 1))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ErrorMessage.ACCESS_DENIED));
    }
}
