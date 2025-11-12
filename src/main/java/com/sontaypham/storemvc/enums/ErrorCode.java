package com.sontaypham.storemvc.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
  // USER
  USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
  USERNAME_ALREADY_EXISTS(1002, "Username already exists", HttpStatus.CONFLICT),
  EMAIL_ALREADY_EXISTS(1003, "Email already exists", HttpStatus.CONFLICT),
  INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
  INVALID_CREDENTIALS(1005, "Invalid username or password", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED(1006, "Unauthorized", HttpStatus.UNAUTHORIZED),
  FORBIDDEN(1007, "Forbidden", HttpStatus.FORBIDDEN),
  UNAUTHENTICATED(1008, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  INVALID_USERNAME(1009, "Invalid username", HttpStatus.BAD_REQUEST),
  INVALID_EMAIL(1010, "Invalid email format, must end with @gmail.com", HttpStatus.BAD_REQUEST),
  PASSWORD_NOT_MATCHES(1011, "Password does not match", HttpStatus.BAD_REQUEST),
  PASSWORD_TYPE_INVALID(
      1012,
      "Password type invalid, must be at least {min} characters and contain at least 1 uppercase",
      HttpStatus.BAD_REQUEST),
  CONFIRM_PASSWORD_NOT_MATCHES(1013, "Confirm password does not match", HttpStatus.BAD_REQUEST),
  INVALID_TELEPHONE_NUMBER(1014, "Invalid telephone number", HttpStatus.BAD_REQUEST),
  // PRODUCT
  PRODUCT_NOT_FOUND(2001, "Product not found", HttpStatus.NOT_FOUND),
  PRODUCT_ALREADY_EXISTS(2002, "Product already exists", HttpStatus.CONFLICT),
  INVALID_PRODUCT_NAME(2003, "Invalid product name", HttpStatus.BAD_REQUEST),
  INVALID_PRODUCT_PRICE(2004, "Invalid product price", HttpStatus.BAD_REQUEST),
  PRODUCT_OUT_OF_STOCK(2005, "Product out of stock", HttpStatus.BAD_REQUEST),
  PRODUCT_CREATION_FAILED(2006, "Failed to create product", HttpStatus.INTERNAL_SERVER_ERROR),
  PRODUCT_UPDATE_FAILED(2007, "Failed to update product", HttpStatus.INTERNAL_SERVER_ERROR),
  PRODUCT_DELETE_FAILED(2008, "Failed to delete product", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PRODUCT_STATUS(2009, "Invalid product status", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND_IN_CART( 2010, "Product not found in cart", HttpStatus.NOT_FOUND),

  // CATEGORY
  CATEGORY_NOT_FOUND(3001, "Category not found", HttpStatus.NOT_FOUND),
  CATEGORY_ALREADY_EXISTS(3002, "Category already exists", HttpStatus.CONFLICT),
    // SUPPLIER
  SUPPLIER_NOT_FOUND(3003, "Supplier not found", HttpStatus.NOT_FOUND),
    SUPPLIER_ALREADY_EXISTS(3004, "Supplier already exists", HttpStatus.CONFLICT),
  // ROLE & PERMISSION
  ROLE_NOT_FOUND(6001, "Role not found", HttpStatus.NOT_FOUND),
  ROLE_ALREADY_EXISTS(6002, "Role already exists", HttpStatus.CONFLICT),
  PERMISSION_NOT_FOUND(6003, "Permission not found", HttpStatus.NOT_FOUND),
  PERMISSION_ALREADY_EXISTS(6004, "Permission already exists", HttpStatus.CONFLICT),
  ROLE_ASSIGNMENT_FAILED(6005, "Failed to assign role to user", HttpStatus.INTERNAL_SERVER_ERROR),
  PERMISSION_ASSIGN_FAILED(
      6006, "Failed to assign permission to role", HttpStatus.INTERNAL_SERVER_ERROR),
  ROLE_DELETE_FORBIDDEN(6007, "Cannot delete this role", HttpStatus.FORBIDDEN),
  PERMISSION_DELETE_FORBIDDEN(6008, "Cannot delete this permission", HttpStatus.FORBIDDEN),
  // UNCATEGORIZED
  UNCATEGORIZED(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);
  int code;
  String message;
  HttpStatus httpStatus;
}
