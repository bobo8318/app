package cn.openui.www.caodian.config;

/**
 * Created by My on 2016/10/30.
 */
public class Config {

    public static String CHARSET = "UTF-8";

    public static int SCOPE_ALL = 0;
    public static int SCOPE_ONLY_FRIENDS = 1;
    public static int SCOPE_ONLY_ME = 2;

    public static int STATUS_CHECK = 0;
    public static int STATUS_PUBLISH = 1;
    public static int STATUS_DELETE = 2;
    public static int STATUS_FORBID = 3;

    public final static String FACE_DECTECT_URL =  "https://api-cn.faceplusplus.com/facepp/v3/detect";

    public final static String FACE_KEY = "PHNK6av4h9zC1IPnSgIny18kxPS5HXn0";
    public final static String FACE_SECRET = "bCGOGWoBG5j5BzP9jJRnyJPm2Uef0PrO";
    public static final String BOUNDARYSTR = "wwwopenuicn";
    public static final String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";

}
