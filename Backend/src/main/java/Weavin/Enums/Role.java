package Weavin.Enums;

public enum Role {
    UNAUTHENTICATED,
    UNVERIFIED,
    USER,
    ADMIN;

    public static Role getRoleFromString(String role) {
        switch (role) {
            case "UNAUTHENTICATED":
                return UNAUTHENTICATED;
            case "UNVERIFIED":
                return UNVERIFIED;
            case "USER":
                return USER;
            case "ADMIN":
                return ADMIN;
            default:
                return null;
        }
    }
}
