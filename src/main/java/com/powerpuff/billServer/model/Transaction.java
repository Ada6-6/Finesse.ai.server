package com.powerpuff.billServer.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Transaction's unique identifier", example = "")
    private Integer id; // 主键，自增

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "The timestamp when the transaction was created", example = "2023-01-01T12:00:00")
    private LocalDateTime createdAt; // 用户创建时间，默认值为当前时间

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "The timestamp when the transaction was last updated", example = "2023-01-01T12:00:00")
    private LocalDateTime updatedAt; // 记录最后更新时间

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "using_type", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0") // 0: active
    @Schema(description = "Transaction's status", example = "0") // 0: active, 1: inactive, 2: deleted
    private UsingType usingType; // 交易状态

    @Column(name = "user_id", nullable = false)
    @Schema(description = "The unique identifier of the user associated with this transaction", example = "1")
    private Integer userId; // 对应用户的唯一标识符

    @Column(name = "transaction_at", nullable = false)
    @Schema(description = "The timestamp when the transaction occurred", example = "2023-01-01T12:00:00")
    private LocalDateTime transactionAt; // 交易时间

    @Column(name = "transaction_type", length = 50, nullable = false)
    @Schema(description = "The type of transaction", example = "income")//income / outcome
    private String transactionType; // 交易类型

    @Column(name = "counterparty_name", length = 255)
    @Schema(description = "Name of the counterparty or merchant", example = "ABC Store")
    private String counterpartyName; // 交易对方的姓名/商铺名称

    @Column(name = "category", length = 50)
    @Schema(description = "Category of the transaction", example = "Groceries")
    private String category; // 交易的分类

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Description of the transaction", example = "Bought groceries")
    private String description; // 交易描述

    @Column(name = "payment_method", length = 50)
    @Schema(description = "Payment method used for the transaction", example = "credit card")
    private String paymentMethod; // 支付方式

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Total amount of the transaction", example = "50.00")
    private BigDecimal totalAmount; // 总金额

    @Column(name = "receipt_image", length = 255)
    @Schema(description = "Path to the receipt image", example = "/path/to/receipt.jpg")
    private String receiptImage; // 存放收据图片的路径，可为空

    @Column(name = "account_info", length = 255)
    @Schema(description = "Account information for the payment method", example = "1234-5678-9101")
    private String accountInfo; // 存放支付账户信息（如银行卡号），可为空

    @Column(name = "transaction_reference_number", length = 100)
    @Schema(description = "Unique reference number for the transaction", example = "REF123456789")
    private String transactionReferenceNumber; // 交易中的唯一参考号

    @Column(name = "currency", length = 10)
    @Schema(description = "Currency type used for the transaction", example = "USD")
    private String currency; // 消费所使用的货币类型

    @Column(name = "location", length = 255)
    @Schema(description = "Geographic location of the transaction", example = "New York, USA")
    private String location; // 消费发生的地理位置

    @Column(name = "notes", columnDefinition = "TEXT")
    @Schema(description = "Additional notes or comments about the transaction", example = "Bought some snacks")
    private String notes; // 额外备注或说明

    @Column(name = "is_recurring", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Whether the transaction is recurring", example = "false")
    private Boolean isRecurring; // 标记该消费是否为定期支出

    @Column(name = "is_reimbursable", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Whether the transaction is reimbursable", example = "true")
    private Boolean isReimbursable; // 标记该消费是否可报销

    @Column(name = "tags", length = 255)
    @Schema(description = "User-defined tags for the transaction", example = "grocery, weekend")
    private String tags; // 用户自定义标签

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        // Set current time if the provided value is null
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        // Set current time if the provided value is null
        this.updatedAt = (updatedAt != null) ? updatedAt : LocalDateTime.now();
    }

    public UsingType getUsingType() {
        return usingType;
    }

    public void setUsingType(UsingType usingType) {
        this.usingType = usingType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getTransactionAt() {
        return transactionAt;
    }

    public void setTransactionAt(LocalDateTime transactionAt) {
        this.transactionAt = transactionAt;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReceiptImage() {
        return receiptImage;
    }

    public void setReceiptImage(String receiptImage) {
        this.receiptImage = receiptImage;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getRecurring() {
        return isRecurring;
    }

    public void setRecurring(Boolean recurring) {
        isRecurring = recurring;
    }

    public Boolean getReimbursable() {
        return isReimbursable;
    }

    public void setReimbursable(Boolean reimbursable) {
        isReimbursable = reimbursable;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setIsRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public void setIsReimbursable(boolean isReimbursable) {
        this.isReimbursable = isReimbursable;
    }


}
