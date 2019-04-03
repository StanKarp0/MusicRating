package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerForm;
import org.springframework.hateoas.Resource;

import java.util.Optional;

public interface PerformerService {

    Performer save(PerformerForm performerForm);

}
