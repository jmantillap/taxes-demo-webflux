package work.javiermantilla.tax.domain.model.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constans {
    public static final String A_CONVERT_JSONOBJECT_OCCURRED = "Error converting attribute to the desired format";
    public static final String REQUEST_BODY_ERROR = "Error in the request body";
    public static final String UNEXPECTED_ERROR = "Server error";
    public static final String RESOURCE_NOT_FOUND_MSG = "Trying  to access a nonexistent table or index";
    public static final String UNEXPECTED_EXCEPTION_MSG = "Unexpected exception in dynamo services";
    public static final String SDK_EXCEPTION_MSG = "amazon SDK exception related to dynamo service and client";
    public static final String VALIDATE_FORMAT_NAME = "Available format name";
    public static final String INVALIDATE_FORMAT_NAME = "Nombre de formato no disponible";
    public static final String INVALIDATE_FORMAT_NAME_MSG = "El nombre del formato ya existe";
    public static final String ERROR_UPDATE = "Error al actualizar";
    public static final String INVALID_FORMAT = "Formato invalido, verifique los datos ingresados";
    public static final String INVALID_FORMAT_STRUCTURE = "Estructura de formato invalida, verifique " +
            "los datos ingresados";
}
