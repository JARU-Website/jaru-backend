package com.web.jaru.post_poll.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public final class PollRequest {

    private PollRequest() {}

    public record Create(
            boolean allowMultiple,
            @NotNull @Size(min = 2, max = 10) List<@NotBlank @Size(max = 100) String> options
            ) { }
}
