package com.ijzepeda.armet.util;

import okhttp3.MediaType;

public class Constants {

    public static String VER_API = "/v2";
    public static String KEY_API = "";
    public static String URL_API = "https://www.mocky.io";
    public static String URL_LOGIN = "/5b26f8cb3000002c00ee27dd";
    public static String URL_REGISTER = "/5b26f8cb3000002c00ee27dd";

    //Error Codes
    public static final int CODE_OK = 200;
    public static final int CODE_BADREQUEST = 400;
    public static final int CODE_NOTFOUND = 404;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_UNAUHTHORIZED = 401;
    public static final int CODE_SERVERERROR = 500;
    //Error Messages
    public static final String MESSAGE_BADREQUEST = "The server cannot process the request. Try it again later";
    public static final String MESSAGE_NOTFOUND = "The requested resource could not be found but may be available in the future. Subsequent requests by the client are permissible.";
    public static final String MESSAGE_FORBIDDEN = "The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource, or may need an account of some sort.";
    public static final String MESSAGE_UNAUHTHORIZED = "The user does not have the necessary credentials.";
    public static final String MESSAGE_SERVERERROR = "The server encountered an unexpected condition which prevented it from fulfilling the request.";

    public static final String ERROR_REQUEST = "An error has occurred";

    /**
     * Mediatype and sendData exist at the moment to test connection, nothing else
     */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String JSON_POSTMAN_STRING = "application/json";
    public static final MediaType JSON_POSTMAN = MediaType.parse(JSON_POSTMAN_STRING);
    public static final String XFORM = "application/x-www-form-urlencoded";
    public static final MediaType XFORM_MEDIATYPE = MediaType.parse(XFORM);

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String UI_DATE_FORMAT = "MMMM d, yyyy";
    //HTTP CODES
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_BAD_REQUEST = 400;
    public static final int RESPONSE_NOT_AVAILABLE = 404;
    public static final int RESPONSE_FORBIDDEN = 301;
    public static final int RESPONSE_BAD_CREDENTIALS = 401;
    public static final int RESPONSE_SERVER_ERROR = 500;


    public static final String PRODUCT_ID ="productId";
    public static final String SERVICE_ID ="serviceId";


    public static final String EXTRA_EDIT_SERVICE ="editService";
    public static final String EXTRA_EDITING_SERVICE ="editingService";
    public static final String EXTRA_EDIT_PRODUCT ="editProduct";
    public static final String EXTRA_EDITING_PRODUCT ="editingProduct";

    public static final String EXTRA_CLIENT_ID ="clientId";
    public static final String EXTRA_CLIENT_NAME ="clientName";

    public static final String EXTRA_TASK_ID ="taskId";
    public static final String EXTRA_ANOTHER_ID ="";


    public static final int RESULT_TASK=102;





}
