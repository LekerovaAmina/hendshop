/// Модель пользователя для работы с API
class UserModel {
  final int id;
  final String email;
  final String username;
  final String role; // USER, FREELANCER, ADMIN
  final String? avatarUrl;
  final DateTime createdAt;

  UserModel({
    required this.id,
    required this.email,
    required this.username,
    required this.role,
    this.avatarUrl,
    required this.createdAt,
  });

  /// Создание объекта из JSON (ответ от сервера)
  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      id: json['id'] as int,
      email: json['email'] as String,
      username: json['username'] as String,
      role: json['role'] as String,
      avatarUrl: json['avatar_url'] as String?,
      createdAt: DateTime.parse(json['created_at'] as String),
    );
  }

  /// Преобразование объекта в JSON (для отправки на сервер)
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'email': email,
      'username': username,
      'role': role,
      'avatar_url': avatarUrl,
      'created_at': createdAt.toIso8601String(),
    };
  }

  /// Копирование объекта с изменением некоторых полей
  UserModel copyWith({
    int? id,
    String? email,
    String? username,
    String? role,
    String? avatarUrl,
    DateTime? createdAt,
  }) {
    return UserModel(
      id: id ?? this.id,
      email: email ?? this.email,
      username: username ?? this.username,
      role: role ?? this.role,
      avatarUrl: avatarUrl ?? this.avatarUrl,
      createdAt: createdAt ?? this.createdAt,
    );
  }
}

/// Модель для ответа от сервера при логине
class LoginResponse {
  final String token;
  final UserModel user;

  LoginResponse({
    required this.token,
    required this.user,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    return LoginResponse(
      token: json['token'] as String,
      user: UserModel.fromJson(json['user'] as Map<String, dynamic>),
    );
  }
}

/// Модель для запроса регистрации
class RegisterRequest {
  final String email;
  final String password;
  final String username;

  RegisterRequest({
    required this.email,
    required this.password,
    required this.username,
  });

  Map<String, dynamic> toJson() {
    return {
      'email': email,
      'password': password,
      'username': username,
    };
  }
}

/// Модель для запроса логина
class LoginRequest {
  final String email;
  final String password;

  LoginRequest({
    required this.email,
    required this.password,
  });

  Map<String, dynamic> toJson() {
    return {
      'email': email,
      'password': password,
    };
  }
}