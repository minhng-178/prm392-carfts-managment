package com.example.prm392_craft_management.models.payment;

public class PaymentResponseModel {
    private Payment payment;
    private Order order;

    public PaymentResponseModel(Order order, Payment payment) {
        this.order = order;
        this.payment = payment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public static class Payment {
        private int id;
        private int order_id;
        private int user_id;
        private double amount;
        private String ref;
        private String url;
        private String expire;
        private int status;

        public Payment(double amount, String expire, int id, int order_id, String ref, int status, String url, int user_id) {
            this.amount = amount;
            this.expire = expire;
            this.id = id;
            this.order_id = order_id;
            this.ref = ref;
            this.status = status;
            this.url = url;
            this.user_id = user_id;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }

    public static class Order {
        private int id;
        private int user_id;
        private String phone;
        private String address;
        private int distance;
        private double total_product_price;
        private double shipping_fee;
        private double total_price;
        private int status;

        public Order(String address, int distance, int id, String phone, double shipping_fee, int status, double total_price, double total_product_price, int user_id) {
            this.address = address;
            this.distance = distance;
            this.id = id;
            this.phone = phone;
            this.shipping_fee = shipping_fee;
            this.status = status;
            this.total_price = total_price;
            this.total_product_price = total_product_price;
            this.user_id = user_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public double getShipping_fee() {
            return shipping_fee;
        }

        public void setShipping_fee(double shipping_fee) {
            this.shipping_fee = shipping_fee;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getTotal_price() {
            return total_price;
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }

        public double getTotal_product_price() {
            return total_product_price;
        }

        public void setTotal_product_price(double total_product_price) {
            this.total_product_price = total_product_price;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
