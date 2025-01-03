package org.ject.momentia.common.domain.user.type;

public enum FieldType {
    PAINTING("회화"),
    CRAFTSCULPTURE("공예/조각"),
    DRAWING("드로잉"),
    PRINTMAKING("판화"),
    CALLIGRAPHY("서예"),
    ILLUSTRATION("일러스트"),
    DIGITALART("디지털아트"),
    PHOTOGRAPHY("사진"),
    OTHERS("기타");

    private final String koreanName;

    FieldType(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }

}
