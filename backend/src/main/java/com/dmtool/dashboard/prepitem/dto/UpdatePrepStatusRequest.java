package com.dmtool.dashboard.prepitem.dto;

import com.dmtool.dashboard.prepitem.model.PrepStatus;

public record UpdatePrepStatusRequest(
        PrepStatus status
) {}