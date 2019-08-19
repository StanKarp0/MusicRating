package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.PerformerUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PerformerService {

    Performer save(PerformerForm performerForm);

    Performer update(PerformerUpdateForm performerForm);

    Optional<Performer> findById(long performerId);

    Page<Performer> findByQuery(String query, Pageable pageable);

    Optional<Performer> delete(long performerId);

    Page<Performer> findAll(Pageable pageable);
}
