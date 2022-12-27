package hsge.hsgeback.constant;

public enum FcmPushId {
    MATCH_SWIPE("match"),
    NEW_MESSAGE("message"),
    WAVING("waving"),
    NOTICE("notice");

    private final String pushId;

    FcmPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getPushId() {
        return pushId;
    }
}
