package com.genedu.user.dto.customer;

import java.util.List;

public record UserListResponseDTO(int totalUser, List<UserAdminDTO> customers, int totalPage) {
}
