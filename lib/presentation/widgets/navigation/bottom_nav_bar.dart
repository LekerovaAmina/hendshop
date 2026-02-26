import 'package:flutter/material.dart';

/// ─────────────────────────────────────────────────────────
/// HandShop Bottom Navigation Bar
///
/// Использует PNG иконки из папки assets/icons/:
///   - Home.png
///   - Catalog.png
///   - Orders.png
///   - Favorites.png
///   - Profile.png
///
/// Активная иконка "всплывает" вверх на белом кружке
/// ─────────────────────────────────────────────────────────
class HandShopBottomNavBar extends StatelessWidget {
  final int currentIndex;
  final Function(int) onTap;

  const HandShopBottomNavBar({
    super.key,
    required this.currentIndex,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 78,
      child: Stack(
        clipBehavior: Clip.none,
        children: [
          // Белая полоска снизу (Rectangle 2)
          Positioned(
            bottom: 0,
            left: 0,
            right: 0,
            child: Container(height: 60, color: Colors.white),
          ),

          // Иконки
          Positioned.fill(
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                _NavItem(
                  index: 0,
                  currentIndex: currentIndex,
                  onTap: onTap,
                  iconPath: 'assets/icons/Home.png',
                  activeColor: const Color(0xFFE7AEFF),
                ),
                _NavItem(
                  index: 1,
                  currentIndex: currentIndex,
                  onTap: onTap,
                  iconPath: 'assets/icons/Catalog.png',
                  activeColor: const Color(0xFFC690FF),
                ),
                _NavItem(
                  index: 2,
                  currentIndex: currentIndex,
                  onTap: onTap,
                  iconPath: 'assets/icons/Orders.png',
                  activeColor: const Color(0xFFAE90FF),
                ),
                _NavItem(
                  index: 3,
                  currentIndex: currentIndex,
                  onTap: onTap,
                  iconPath: 'assets/icons/Favorites.png',
                  activeColor: const Color(0xFF977FFE),
                ),
                _NavItem(
                  index: 4,
                  currentIndex: currentIndex,
                  onTap: onTap,
                  iconPath: 'assets/icons/Profile.png',
                  activeColor: const Color(0xFF7672FF),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// ─────────────────────────────────────────────────────────
// Один элемент навигации с анимацией
// ─────────────────────────────────────────────────────────
class _NavItem extends StatelessWidget {
  final int index;
  final int currentIndex;
  final Function(int) onTap;
  final String iconPath;
  final Color activeColor;

  const _NavItem({
    required this.index,
    required this.currentIndex,
    required this.onTap,
    required this.iconPath,
    required this.activeColor,
  });

  bool get _active => currentIndex == index;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => onTap(index),
      behavior: HitTestBehavior.opaque,
      child: SizedBox(
        width: 60,
        height: 78,
        child: Stack(
          clipBehavior: Clip.none,
          alignment: Alignment.bottomCenter,
          children: [
            // Белый кружок (Ellipse) — всплывает когда активен
            AnimatedPositioned(
              duration: const Duration(milliseconds: 280),
              curve: Curves.easeOutBack,
              bottom: _active ? 18 : -60,
              left: 0,
              right: 0,
              child: AnimatedOpacity(
                duration: const Duration(milliseconds: 200),
                opacity: _active ? 1.0 : 0.0,
                child: Center(
                  child: Container(
                    width: 60,
                    height: 60,
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      shape: BoxShape.circle,
                    ),
                  ),
                ),
              ),
            ),

            // PNG иконка из assets
            AnimatedPositioned(
              duration: const Duration(milliseconds: 280),
              curve: Curves.easeOutBack,
              bottom: _active ? 32 : 17,
              left: 0,
              right: 0,
              child: Center(
                child: AnimatedOpacity(
                  duration: const Duration(milliseconds: 150),
                  opacity: _active ? 1.0 : 0.5, // неактивные полупрозрачные
                  child: Image.asset(
                    iconPath,
                    width: 28,
                    height: 28,
                    // Перекрашиваем иконку в нужный цвет
                    color: activeColor,
                    // Обработка ошибок если файла нет
                    errorBuilder: (context, error, stackTrace) {
                      return Icon(
                        _getIconFallback(index),
                        size: 28,
                        color: activeColor,
                      );
                    },
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  /// Запасная иконка если PNG файла нет
  IconData _getIconFallback(int index) {
    switch (index) {
      case 0:
        return Icons.home;
      case 1:
        return Icons.grid_view;
      case 2:
        return Icons.shopping_bag_outlined;
      case 3:
        return Icons.favorite_border;
      case 4:
        return Icons.person_outline;
      default:
        return Icons.circle;
    }
  }
}