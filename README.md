# Coupon API

The **Coupon API** provides functionality for managing coupons, validating them, applying discounts, and handling coupon consumption. This API is part of a larger e-commerce system where coupons can be created, activated, deactivated, and applied to orders.

## Overview

The **Coupon API** includes endpoints for:
- Creating and managing coupons.
- Validating and applying coupons to orders.
- Handling coupon consumption for tracking and analytics.
- Calculating discounts based on the coupon details.

## Endpoints

### Create Coupon

- **URL**: `/coupon`
- **Method**: `POST`
- **Description**: Creates a new coupon.
- **Request Body**: 
  ```json
  {
    "remainingUsages": "integer",
    "expiryDate": "yyyy-mm-dd",
    "value": "decimal",
    "active": "boolean",
    "type": "coupon_type"
  }
  ```
- **Response**:
  ```json
  {
    "code": "string",
    "remainingUsages": "integer",
    "expiryDate": "yyyy-mm-dd",
    "value": "decimal",
    "active": "boolean",
    "type": "coupon_type"
  }
  ```
- **Response Code**: `201 Created`

---

### Get All Coupons

- **URL**: `/coupon`
- **Method**: `GET`
- **Description**: Retrieves all coupons or filters coupons by their active status.
- **Query Parameter**: `isActive` (Boolean, optional)
- **Response**:
  ```json
  [
    {
      "code": "string",
      "remainingUsages": "integer",
      "expiryDate": "yyyy-mm-dd",
      "value": "decimal",
      "active": "boolean",
      "type": "coupon_type"
    }
  ]
  ```
- **Response Code**: `200 OK`

---

### Get Coupon by Code

- **URL**: `/coupon/{code}`
- **Method**: `GET`
- **Description**: Retrieves the details of a specific coupon by its code.
- **Path Variable**: `code` (String)
- **Response**:
  ```json
  {
    "code": "string",
    "remainingUsages": "integer",
    "expiryDate": "yyyy-mm-dd",
    "value": "decimal",
    "active": "boolean",
    "type": "coupon_type"
  }
  ```
- **Response Code**: `200 OK`

---

### Activate Coupon

- **URL**: `/coupon/activation/{code}`
- **Method**: `PUT`
- **Description**: Activates a coupon by its code.
- **Path Variable**: `code` (String)
- **Response**:
  ```json
  {
    "code": "string",
    "remainingUsages": "integer",
    "expiryDate": "yyyy-mm-dd",
    "value": "decimal",
    "active": "boolean",
    "type": "coupon_type"
  }
  ```
- **Response Code**: `200 OK`

---

### Deactivate Coupon

- **URL**: `/coupon/deactivation/{code}`
- **Method**: `PUT`
- **Description**: Deactivates a coupon by its code.
- **Path Variable**: `code` (String)
- **Response**:
  ```json
  {
    "code": "string",
    "remainingUsages": "integer",
    "expiryDate": "yyyy-mm-dd",
    "value": "decimal",
    "active": "boolean",
    "type": "coupon_type"
  }
  ```
- **Response Code**: `200 OK`

---

### Validate Coupon

- **URL**: `/coupon/validation`
- **Method**: `GET`
- **Description**: Validates a coupon code for a customer.
- **Query Parameters**: 
  - `code` (String, required)
  - `customerEmail` (String, required)
- **Response Code**: `200 OK`

---

### Calculate Discount

- **URL**: `/coupon/discount`
- **Method**: `GET`
- **Description**: Calculates the discount for an order based on the coupon.
- **Query Parameters**:
  - `code` (String, required)
  - `customerEmail` (String, required)
  - `orderPrice` (Decimal, required)
- **Response**:
  ```json
  {
    "discountAmount": "decimal"
  }
  ```
- **Response Code**: `200 OK`

---

### Consume Coupon

- **URL**: `/coupon/consumption`
- **Method**: `POST`
- **Description**: Consumes a coupon for a specific order.
- **Request Body**:
  ```json
  {
    "code": "string",
    "customerEmail": "string",
    "orderPrice": "decimal",
    "orderId": "long"
  }
  ```
- **Response**:
  ```json
  {
    "id": "long",
    "consumptionDate": "yyyy-mm-dd'T'HH:mm:ss.SSSZ",
    "orderId": "long",
    "actualDiscount": "decimal",
    "customerEmail": "string",
    "couponId": "long",
    "couponCode": "string",
    "couponValue": "decimal"
  }
  ```
- **Response Code**: `201 Created`

---

## Data Models

### Coupon DTO

- **code**: Coupon code (String)
- **remainingUsages**: Number of remaining usages for the coupon (Integer)
- **expiryDate**: Expiry date of the coupon (LocalDate)
- **value**: The discount value of the coupon (BigDecimal)
- **active**: Indicates whether the coupon is active (Boolean)
- **type**: Type of the coupon (CouponType)

---

### Consumption DTO

- **id**: Unique identifier for the consumption (Long)
- **consumptionDate**: Date and time the coupon was consumed (Instant)
- **orderId**: The order ID associated with the consumption (Long)
- **actualDiscount**: The actual discount applied (BigDecimal)
- **customerEmail**: Email of the customer who used the coupon (String)
- **couponId**: ID of the coupon used (Long)
- **couponCode**: Code of the coupon used (String)
- **couponValue**: Value of the coupon (BigDecimal)

---

## Configuration

- Configure the database connection in the `application.properties` file:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
  spring.datasource.username=your_db_username
  spring.datasource.password=your_db_password
  ```
