package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDateTime;

@Data
public class PromotionDto {
    private Long id;

    @NotBlank(message = "Название акции не может быть пустым")
    @Size(max = 200, message = "Название акции не должно превышать 200 символов")
    private String name;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    @NotNull(message = "Дата начала обязательна")
    @PastOrPresent(message = "Дата начала не может быть в будущем")
    private LocalDateTime startDate;

    @NotNull(message = "Дата окончания обязательна")
    @Future(message = "Дата окончания должна быть в будущем")
    private LocalDateTime endDate;

    @AssertTrue(message = "Дата окончания должна быть после даты начала")
    private boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true; // Проверку @NotNull выполняют другие аннотации
        }
        return endDate.isAfter(startDate);
    }
}
