/// API endpoints для HandShop Backend
class ApiEndpoints {
  // Base URL - замените на ваш реальный URL сервера
  static const String baseUrl = 'http://localhost:8080';

  // Auth endpoints
  static const String login = '/api/auth/login';
  static const String register = '/api/auth/register';
  static const String registerFreelancer = '/api/auth/register-freelancer';

  // Products endpoints
  static const String products = '/api/products';
  static String productDetails(int id) => '/api/products/$id';
  static String productReviews(int id) => '/api/products/$id/reviews';
  static String addReview(int id) => '/api/products/$id/review';
  static String reportProduct(int id) => '/api/products/$id/report';

  // Favorites endpoints
  static const String favorites = '/api/favorites';
  static String addToFavorites(int productId) => '/api/favorites/$productId';
  static String removeFromFavorites(int productId) => '/api/favorites/$productId';

  // Orders endpoints
  static const String createOrder = '/api/orders';
  static const String myOrders = '/api/orders/my';
  static String markOrderDelivered(int orderId) => '/api/orders/$orderId/delivered';

  // Freelancer endpoints
  static const String freelancerShelves = '/api/freelancer/shelves';
  static const String freelancerProducts = '/api/freelancer/products';
  static String submitProduct(int id) => '/api/freelancer/products/$id/submit';
  static String archiveProduct(int id) => '/api/freelancer/products/$id/archive';
  static String updateOrderStatus(int id) => '/api/freelancer/orders/$id/status';

  // Admin endpoints
  static const String adminCategories = '/api/admin/global-categories';
  static const String adminModeration = '/api/admin/products/moderation';
  static String approveProduct(int id) => '/api/admin/products/$id/approve';
  static String rejectProduct(int id) => '/api/admin/products/$id/reject';
  static const String adminReports = '/api/admin/reports';
}