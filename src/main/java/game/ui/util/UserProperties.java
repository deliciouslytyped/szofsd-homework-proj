package game.ui;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
//TODO why _exactly_ did the example choose these?
//TODO why does this fail with already defined construtor?
//@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class UserProperties {};
