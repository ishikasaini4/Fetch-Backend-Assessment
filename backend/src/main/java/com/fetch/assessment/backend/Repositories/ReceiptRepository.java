package com.fetch.assessment.backend.Repositories;

import com.fetch.assessment.backend.models.Receipt;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ReceiptRepository extends CrudRepository<Receipt, UUID> {

}
