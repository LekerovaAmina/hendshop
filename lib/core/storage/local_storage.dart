import 'package:shared_preferences/shared_preferences.dart';

/// Класс для работы с локальным хранилищем (SharedPreferences)
class LocalStorage {
  static const String _tokenKey = 'jwt_token';
  static const String _userIdKey = 'user_id';
  static const String _userRoleKey = 'user_role';
  static const String _userEmailKey = 'user_email';

  /// Сохранить JWT токен
  Future<void> saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_tokenKey, token);
  }

  /// Получить JWT токен
  Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_tokenKey);
  }

  /// Удалить JWT токен (при выходе)
  Future<void> removeToken() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_tokenKey);
  }

  /// Сохранить ID пользователя
  Future<void> saveUserId(int userId) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt(_userIdKey, userId);
  }

  /// Получить ID пользователя
  Future<int?> getUserId() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getInt(_userIdKey);
  }

  /// Сохранить роль пользователя (USER, FREELANCER, ADMIN)
  Future<void> saveUserRole(String role) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_userRoleKey, role);
  }

  /// Получить роль пользователя
  Future<String?> getUserRole() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_userRoleKey);
  }

  /// Сохранить email пользователя
  Future<void> saveUserEmail(String email) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_userEmailKey, email);
  }

  /// Получить email пользователя
  Future<String?> getUserEmail() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_userEmailKey);
  }

  /// Проверить, авторизован ли пользователь
  Future<bool> isLoggedIn() async {
    final token = await getToken();
    return token != null && token.isNotEmpty;
  }

  /// Очистить все данные (при выходе)
  Future<void> clearAll() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_tokenKey);
    await prefs.remove(_userIdKey);
    await prefs.remove(_userRoleKey);
    await prefs.remove(_userEmailKey);
  }
}