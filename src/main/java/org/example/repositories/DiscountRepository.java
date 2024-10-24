package org.example.repositories;

import org.example.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    //поиск всех активных скидок для продукта? переименовать?
    List<Discount> findByProductIdAndStartDateBeforeAndEndDateAfter(Long productId, Date startDate, Date endDate);
}
