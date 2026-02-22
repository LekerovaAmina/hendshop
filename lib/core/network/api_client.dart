import 'dart:convert';
import 'package:http/http.dart' as http;
import 'api_endpoints.dart';
import '../storage/local_storage.dart';

/// HTTP клиент для работы с HandShop API
class ApiClient {
  final LocalStorage _localStorage = LocalStorage();

  /// GET запрос
  Future<dynamic> get(String endpoint) async {
    try {
      final url = Uri.parse('${ApiEndpoints.baseUrl}$endpoint');
      final token = await _localStorage.getToken();

      final response = await http.get(
        url,
        headers: _getHeaders(token),
      );

      return _handleResponse(response);
    } catch (e) {
      throw Exception('Ошибка сети: $e');
    }
  }

  /// POST запрос
  Future<dynamic> post(String endpoint, Map<String, dynamic> body) async {
    try {
      final url = Uri.parse('${ApiEndpoints.baseUrl}$endpoint');
      final token = await _localStorage.getToken();

      final response = await http.post(
        url,
        headers: _getHeaders(token),
        body: jsonEncode(body),
      );

      return _handleResponse(response);
    } catch (e) {
      throw Exception('Ошибка сети: $e');
    }
  }

  /// PUT запрос
  Future<dynamic> put(String endpoint, Map<String, dynamic> body) async {
    try {
      final url = Uri.parse('${ApiEndpoints.baseUrl}$endpoint');
      final token = await _localStorage.getToken();

      final response = await http.put(
        url,
        headers: _getHeaders(token),
        body: jsonEncode(body),
      );

      return _handleResponse(response);
    } catch (e) {
      throw Exception('Ошибка сети: $e');
    }
  }

  /// DELETE запрос
  Future<dynamic> delete(String endpoint) async {
    try {
      final url = Uri.parse('${ApiEndpoints.baseUrl}$endpoint');
      final token = await _localStorage.getToken();

      final response = await http.delete(
        url,
        headers: _getHeaders(token),
      );

      return _handleResponse(response);
    } catch (e) {
      throw Exception('Ошибка сети: $e');
    }
  }

  /// PATCH запрос
  Future<dynamic> patch(String endpoint, Map<String, dynamic> body) async {
    try {
      final url = Uri.parse('${ApiEndpoints.baseUrl}$endpoint');
      final token = await _localStorage.getToken();

      final response = await http.patch(
        url,
        headers: _getHeaders(token),
        body: jsonEncode(body),
      );

      return _handleResponse(response);
    } catch (e) {
      throw Exception('Ошибка сети: $e');
    }
  }

  /// Формирование заголовков запроса
  Map<String, String> _getHeaders(String? token) {
    final headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    // Добавляем JWT токен если есть
    if (token != null && token.isNotEmpty) {
      headers['Authorization'] = 'Bearer $token';
    }

    return headers;
  }

  /// Обработка ответа от сервера
  dynamic _handleResponse(http.Response response) {
    if (response.statusCode >= 200 && response.statusCode < 300) {
      // Успешный ответ
      if (response.body.isEmpty) {
        return null;
      }
      return jsonDecode(utf8.decode(response.bodyBytes));
    } else if (response.statusCode == 401) {
      // Неавторизован
      throw Exception('Требуется авторизация');
    } else if (response.statusCode == 403) {
      // Доступ запрещен
      throw Exception('Доступ запрещен');
    } else if (response.statusCode == 404) {
      // Не найдено
      throw Exception('Ресурс не найден');
    } else if (response.statusCode == 500) {
      // Ошибка сервера
      throw Exception('Ошибка сервера');
    } else {
      // Другие ошибки
      throw Exception('Ошибка: ${response.statusCode}');
    }
  }
}