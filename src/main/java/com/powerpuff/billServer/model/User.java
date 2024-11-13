package com.powerpuff.billServer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User's unique identifier", example = "")
    private Integer id; // 主键，自增

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "The timestamp when the user was created", example = "2023-01-01T12:00:00")
    private LocalDateTime createdAt; // 用户创建时间，默认值为当前时间

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "The timestamp when the user was last updated", example = "2023-01-01T12:00:00")
    private LocalDateTime updatedAt; // 记录最后更新时间

    @Enumerated(EnumType.ORDINAL) // 使用数字来表示枚举类型
    @Column(name = "using_type", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0") // 0: active
    @Schema(description = "User's status", example = "0") // 0: active, 1: inactive, 2: deleted
    private UsingType usingType; // 用户状态

    @Column(name = "username", length = 50, nullable = false)
    @Schema(description = "The user's username", example = "john_doe")
    private String username; // 用户名

    @Column(name = "email", length = 100, nullable = false, unique = true)
    @Schema(description = "The user's email address", example = "john.doe@example.com")
    private String email; // 用户邮箱，唯一标识符

    @Column(name = "is_email_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Whether the user's email has been verified", example = "false")
    private Boolean isEmailVerified; // 是否完成邮箱验证

    @Column(name = "avatar_url", length = 255)
    @Schema(description = "URL of the user's avatar", example = "http://example.com/avatar.jpg")
    private String avatarUrl; // 用户头像URL

    @Column(name = "password_hash", length = 255)
    @Schema(description = "Hashed password of the user", example = "222") //$2a$10$EixZK6B5k23...") // 示例哈希值
    private String passwordHash; // 用户密码哈希值

    @Column(name = "subscription_plan", length = 20, nullable = false)
    @Schema(description = "The user's subscription plan type", example = "premium")
    private String subscriptionPlan; // 订阅计划类型

    @Column(name = "subscription_plan_expiry")
    @Schema(description = "Expiration date of the user's subscription plan", example = "2024-01-01T12:00:00")
    private LocalDateTime subscriptionPlanExpiry; // 订阅计划到期时间

    @Column(name = "is_notification_enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Whether notifications are enabled for the user", example = "true")
    private Boolean isNotificationEnabled; // 是否启用通知功能

    @Column(name = "preferred_language", length = 20)
    @Schema(description = "The user's preferred language", example = "English")
    private String preferredLanguage; // 用户偏好的语言

    @Column(name = "timezone", length = 50)
    @Schema(description = "The user's timezone", example = "PDT")
    private String timezone; // 用户时区

    @Column(name = "last_login_at")
    @Schema(description = "The timestamp of the user's last login", example = "2024-01-01T12:00:00")
    private LocalDateTime lastLoginAt; // 最后登录时间

    @Enumerated(EnumType.ORDINAL) // 使用数字来表示角色
    @Column(name = "role", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0") // 0: user
    @Schema(description = "Role of the user", example = "0") // 0: user, 1: admin
    private Role role; // 用户角色

    @Column(name = "password_reset_token", length = 255)
    @Schema(description = "Token for password reset", example = "reset-token")
    private String passwordResetToken; // 密码重置令牌

    @Column(name = "password_reset_token_expiry")
    @Schema(description = "Expiration date of the password reset token", example = "2024-01-01T12:00:00")
    private LocalDateTime passwordResetTokenExpiry; // 密码重置令牌到期时间

    @Enumerated(EnumType.ORDINAL) // 使用数字来表示身份提供商
    @Column(name = "auth_provider", columnDefinition = "TINYINT(0)") // 0: google
    @Schema(description = "User's authentication provider", example = "0") // 0: google, 1: apple, 2: facebook, 3: email
    private AuthProvider authProvider; // 用户身份认证提供商

    @Column(name = "auth_provider_id", length = 255)
    @Schema(description = "Unique identifier for the user from the authentication provider", example = "provider-id")
    private String authProviderId; // 第三方登录的唯一标识符

    @Column(name = "default_currency", length = 10)
    @Schema(description = "Default currency type for the user", example = "USD")
    private String defaultCurrency; // 默认货币类型

    // 内部静态枚举类定义
    public static enum Role {
        USER(0),    // 普通用户
        ADMIN(1);   // 管理员

        private final int value;

        Role(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Role fromValue(int value) {
            for (Role role : Role.values()) {
                if (role.getValue() == value) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Invalid Role value: " + value);
        }
    }

    public enum AuthProvider {
        GOOGLE(0),    // Google身份认证
        APPLE(1),     // Apple身份认证
        FACEBOOK(2),  // Facebook身份认证
        EMAIL(3);     // 电子邮件身份认证

        private final int value;

        AuthProvider(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static AuthProvider fromValue(int value) {
            for (AuthProvider provider : AuthProvider.values()) {
                if (provider.getValue() == value) {
                    return provider;
                }
            }
            throw new IllegalArgumentException("Invalid AuthProvider value: " + value);
        }
    }



    // Getters and Setters
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public LocalDateTime getSubscriptionPlanExpiry() {
        return subscriptionPlanExpiry;
    }

    public void setSubscriptionPlanExpiry(LocalDateTime subscriptionPlanExpiry) {
        this.subscriptionPlanExpiry = subscriptionPlanExpiry;
    }

    public Boolean getNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(Boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetTokenExpiry() {
        return passwordResetTokenExpiry;
    }

    public void setPasswordResetTokenExpiry(LocalDateTime passwordResetTokenExpiry) {
        this.passwordResetTokenExpiry = passwordResetTokenExpiry;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public String getAuthProviderId() {
        return authProviderId;
    }

    public void setAuthProviderId(String authProviderId) {
        this.authProviderId = authProviderId;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}
