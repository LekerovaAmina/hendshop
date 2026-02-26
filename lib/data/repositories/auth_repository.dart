import '../../core/network/api_client.dart';
import '../../core/network/api_endpoints.dart';
import '../../core/storage/local_storage.dart';
import '../models/user_model.dart';

/// Репозиторий для работы с авторизацией
class AuthRepository {
  final ApiClient _apiClient = ApiClient();
  final LocalStorage _localStorage = LocalStorage();

  /// Вход в систему
  Future<UserModel> login(String email, String password) async {
    try {
      final request = LoginRequest(email: email, password: password);
      final response = await _apiClient.post(
        ApiEndpoints.login,
        request.toJson(),
      );

      final loginResponse = LoginResponse.fromJson(response);

      // Сохраняем токен и данные пользователя
      await _localStorage.saveToken(loginResponse.token);
      await _localStorage.saveUserId(loginResponse.user.id);
      await _localStorage.saveUserRole(loginResponse.user.role);
      await _localStorage.saveUserEmail(loginResponse.user.email);

      return loginResponse.user;
    } catch (e) {
      throw Exception('Ошибка входа: $e');
    }
  }

  /// Регистрация обычного пользователя
  Future<UserModel> register(String email, String password, String username) async {
    try {
      final request = RegisterRequest(
        email: email,
        password: password,
        username: username,
      );

      final response = await _apiClient.post(
        ApiEndpoints.register,
        request.toJson(),
      );

      final loginResponse = LoginResponse.fromJson(response);

      // Сохраняем токен и данные пользователя
      await _localStorage.saveToken(loginResponse.token);
      await _localStorage.saveUserId(loginResponse.user.id);
      await _localStorage.saveUserRole(loginResponse.user.role);
      await _localStorage.saveUserEmail(loginResponse.user.email);

      return loginResponse.user;
    } catch (e) {
      throw Exception('Ошибка регистрации: $e');
    }
  }

  /// Регистрация фрилансера
  Future<UserModel> registerFreelancer(
      String email,
      String password,
      String username,
      String shopName,
      String shopDescription,
      ) async {
    try {
      final request = {
        'email': email,
        'password': password,
        'username': username,
        'shop_name': shopName,
        'shop_description': shopDescription,
      };

      final response = await _apiClient.post(
        ApiEndpoints.registerFreelancer,
        request,
      );

      final loginResponse = LoginResponse.fromJson(response);

      // Сохраняем токен и данные пользователя
      await _localStorage.saveToken(loginResponse.token);
      await _localStorage.saveUserId(loginResponse.user.id);
      await _localStorage.saveUserRole(loginResponse.user.role);
      await _localStorage.saveUserEmail(loginResponse.user.email);

      return loginResponse.user;
    } catch (e) {
      throw Exception('Ошибка регистрации фрилансера: $e');
    }
  }

  /// Выход из системы
  Future<void> logout() async {
    await _localStorage.clearAll();
  }

  /// Проверка, авторизован ли пользователь
  Future<bool> isLoggedIn() async {
    return await _localStorage.isLoggedIn();
  }

  /// Получить текущего пользователя из локального хранилища
  Future<Map<String, dynamic>?> getCurrentUser() async {
    final userId = await _localStorage.getUserId();
    final role = await _localStorage.getUserRole();
    final email = await _localStorage.getUserEmail();

    if (userId == null || role == null || email == null) {
      return null;
    }

    return {
      'id': userId,
      'role': role,
      'email': email,
    };
  }
}