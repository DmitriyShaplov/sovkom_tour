package phonenumbers.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PhonesResponseDto {
    private List<String> phones = new ArrayList<>();
}
