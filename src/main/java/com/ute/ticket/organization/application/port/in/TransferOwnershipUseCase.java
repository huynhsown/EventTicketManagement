package com.ute.ticket.organization.application.port.in;

import com.ute.ticket.organization.application.command.TransferOwnershipCommand;
import com.ute.ticket.organization.application.result.OrganizationResult;

public interface TransferOwnershipUseCase {
    OrganizationResult execute(TransferOwnershipCommand cmd);
}
