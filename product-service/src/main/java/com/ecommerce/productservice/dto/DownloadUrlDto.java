package com.ecommerce.productservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.jcip.annotations.Immutable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DownloadUrlDto {

    String downloadUrl;
}
