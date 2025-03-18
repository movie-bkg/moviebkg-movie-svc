package com.sid.moviebkg.movie.command.dto;

import com.sid.moviebkg.common.command.dto.OperationType;
import com.sid.moviebkg.common.command.dto.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto<T> {
    private List<T> requestList;
    private String commandKey;
    private String groupKey;
    private String initiatedBy;
    private OperationType operationType;
    private ServiceType serviceType;
}
