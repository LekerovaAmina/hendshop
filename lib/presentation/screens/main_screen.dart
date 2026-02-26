import 'package:flutter/material.dart';
import '../widgets/navigation/bottom_nav_bar.dart';
import 'home/home_screen.dart';
import 'catalog/catalog_screen.dart';
import 'orders/orders_screen.dart';
import 'favorites/favorites_screen.dart';
import 'profile/profile_screen.dart';

/// ─────────────────────────────────────────────────────────
/// MainScreen — оболочка с нижней навигацией
///
/// Переключает между 5 экранами, сохраняя их состояние
/// через IndexedStack (экраны не пересоздаются при переключении)
/// ─────────────────────────────────────────────────────────
class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  int _currentIndex = 0; // Стартовая вкладка — Главная

  // Все 5 экранов (создаются один раз)
  final List<Widget> _screens = const [
    HomeScreen(),
    CatalogScreen(),
    OrdersScreen(),
    FavoritesScreen(),
    ProfileScreen(),
  ];

  void _onTabTapped(int index) {
    setState(() {
      _currentIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: IndexedStack(
        index: _currentIndex,
        children: _screens,
      ),
      // Навигация прикреплена снизу
      bottomNavigationBar: HandShopBottomNavBar(
        currentIndex: _currentIndex,
        onTap: _onTabTapped,
      ),
    );
  }
}