import 'package:flutter/material.dart';

/// Главная страница — каталог товаров
/// По дизайну: поиск сверху, теги категорий, сетка карточек товаров
class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final _searchController = TextEditingController();

  // Категории из дизайна
  final List<String> _categories = ['торты', 'свечи', 'игрушки', 'фигурки'];
  int _selectedCategory = 0;

  // Тестовые товары
  final List<Map<String, dynamic>> _products = List.generate(10, (i) => {
    'title': 'Ягодный торт 4кг',
    'shop': 'Сладкоешка',
    'price': '4500',
    'hasConstructor': i % 3 == 0, // каждый 3-й товар имеет конструктор
  });

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      // Градиентный фон из дизайна
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [Color(0xFFFFFFFF), Color(0xFFEFE1FF)],
        ),
      ),
      child: SafeArea(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
              child: Column(
                children: [
                  // Строка поиска (Rectangle 1)
                  _SearchBar(controller: _searchController),
                  const SizedBox(height: 8),
                  // Категории (Rectangle 5-8)
                  _CategoryChips(
                    categories: _categories,
                    selectedIndex: _selectedCategory,
                    onSelected: (i) => setState(() => _selectedCategory = i),
                  ),
                ],
              ),
            ),

            // Сетка товаров
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 10),
                child: GridView.builder(
                  gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 2,
                    crossAxisSpacing: 10,
                    mainAxisSpacing: 11,
                    childAspectRatio: 180 / 264,
                  ),
                  itemCount: _products.length,
                  itemBuilder: (context, index) {
                    return ProductCard(product: _products[index]);
                  },
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

/// Поисковая строка
class _SearchBar extends StatelessWidget {
  final TextEditingController controller;
  const _SearchBar({required this.controller});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 40,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(5),
        border: Border.all(color: const Color(0xFF8A58FF), width: 1),
      ),
      child: TextField(
        controller: controller,
        style: const TextStyle(
            fontFamily: 'Inter', fontSize: 14, color: Color(0xFF2D0193)),
        decoration: const InputDecoration(
          hintText: 'Поиск',
          hintStyle: TextStyle(
              fontFamily: 'Inter', fontSize: 14, color: Color(0xFF2D0193)),
          border: InputBorder.none,
          contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 10),
          prefixIcon: Icon(Icons.search, color: Color(0xFF8A58FF), size: 20),
        ),
      ),
    );
  }
}

/// Чипы категорий
class _CategoryChips extends StatelessWidget {
  final List<String> categories;
  final int selectedIndex;
  final Function(int) onSelected;

  const _CategoryChips({
    required this.categories,
    required this.selectedIndex,
    required this.onSelected,
  });

  // Цвета чипов из дизайна
  static const _colors = [
    Color(0xFFD4C3FC),
    Color(0xFFC5C3FC),
    Color(0xFFB3BBFF),
    Color(0xFFAAADFF),
  ];

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 42,
      child: Row(
        children: List.generate(categories.length, (i) {
          return Expanded(
            child: Padding(
              padding: EdgeInsets.only(left: i == 0 ? 0 : 4),
              child: GestureDetector(
                onTap: () => onSelected(i),
                child: AnimatedContainer(
                  duration: const Duration(milliseconds: 200),
                  height: 42,
                  decoration: BoxDecoration(
                    color: _colors[i % _colors.length],
                    borderRadius: BorderRadius.circular(5),
                    border: selectedIndex == i
                        ? Border.all(color: const Color(0xFF7C3AED), width: 1.5)
                        : null,
                  ),
                  alignment: Alignment.center,
                  child: Text(
                    categories[i],
                    style: const TextStyle(
                      fontFamily: 'Inter',
                      fontSize: 14,
                      color: Colors.black,
                    ),
                  ),
                ),
              ),
            ),
          );
        }),
      ),
    );
  }
}

/// Карточка товара (Frame 4/5/6)
class ProductCard extends StatelessWidget {
  final Map<String, dynamic> product;
  const ProductCard({super.key, required this.product});

  @override
  Widget build(BuildContext context) {
    final hasConstructor = product['hasConstructor'] == true;

    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Изображение товара
          Stack(
            children: [
              ClipRRect(
                borderRadius: const BorderRadius.vertical(top: Radius.circular(8)),
                child: Container(
                  height: 172,
                  width: double.infinity,
                  color: const Color(0xFFEFE1FF), // Заглушка
                  child: const Icon(Icons.image_outlined,
                      size: 60, color: Color(0xFFB3BBFF)),
                ),
              ),

              // Иконка избранного (Vector в правом верхнем углу)
              Positioned(
                top: 8, right: 8,
                child: GestureDetector(
                  onTap: () {},
                  child: const Icon(Icons.favorite_border,
                      color: Color(0xFFA1A1A1), size: 22),
                ),
              ),

              // Тег "Конструктор" (Frame 34)
              if (hasConstructor)
                Positioned(
                  left: 4, bottom: 0,
                  child: Container(
                    padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 4),
                    decoration: const BoxDecoration(
                      color: Color(0xFF8A58FF),
                      borderRadius: BorderRadius.only(
                        topRight: Radius.circular(8),
                        bottomLeft: Radius.circular(0),
                      ),
                    ),
                    child: const Text(
                      'Конструктор',
                      style: TextStyle(
                        fontFamily: 'Inter',
                        fontSize: 14,
                        fontWeight: FontWeight.w500,
                        color: Colors.white,
                      ),
                    ),
                  ),
                ),
            ],
          ),

          // Информация о товаре (Frame 2)
          Padding(
            padding: const EdgeInsets.fromLTRB(4, 5, 4, 0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Название
                Text(
                  product['title'] ?? '',
                  style: const TextStyle(
                      fontFamily: 'Inter', fontSize: 14, color: Colors.black),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                const SizedBox(height: 8),
                // Магазин
                Text(
                  product['shop'] ?? '',
                  style: const TextStyle(
                      fontFamily: 'Inter', fontSize: 14, color: Colors.black),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                const SizedBox(height: 8),
                // Цена (Frame 33)
                Row(
                  children: [
                    Text(
                      product['price'] ?? '',
                      style: const TextStyle(
                          fontFamily: 'Inter', fontSize: 14, color: Colors.black),
                    ),
                    const SizedBox(width: 2),
                    const Text(
                      '₸',
                      style: TextStyle(
                        fontFamily: 'Inter',
                        fontSize: 12,
                        fontWeight: FontWeight.w500,
                        color: Color(0xFF8A58FF),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}