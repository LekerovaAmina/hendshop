import 'package:flutter/material.dart';

/// Страница избранного
class FavoritesScreen extends StatelessWidget {
  const FavoritesScreen({super.key});

  static final _favorites = List.generate(8, (_) => {
    'title': 'Ягодный торт 4кг',
    'shop': 'Сладкоешка',
    'description': 'Ягодный мус, 3 слоя: йогурт, какао, кофе. Шоколадная заливка потеков и декор из свежих ягод',
    'date': '20.09.25',
    'price': '4500',
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter, end: Alignment.bottomCenter,
          colors: [Color(0xFFFFFFFF), Color(0xFFEFE1FF)],
        ),
      ),
      child: SafeArea(
        child: Column(
          children: [
            // Шапка
            Padding(
              padding: const EdgeInsets.fromLTRB(10, 8, 10, 0),
              child: Container(
                height: 40,
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(5),
                  border: Border.all(color: const Color(0xFF8A38F5)),
                ),
                child: const Padding(
                  padding: EdgeInsets.symmetric(horizontal: 16),
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: Text('Инфа',
                        style: TextStyle(fontFamily: 'Inter', fontSize: 14)),
                  ),
                ),
              ),
            ),

            // Список избранного
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(10, 10, 10, 0),
                child: Column(
                  children: _favorites
                      .map((f) => _FavoriteCard(item: f))
                      .toList(),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

/// Карточка избранного (Frame 39/40/41...)
class _FavoriteCard extends StatelessWidget {
  final Map<String, dynamic> item;
  const _FavoriteCard({required this.item});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 140,
      margin: const EdgeInsets.only(bottom: 8),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        children: [
          // Фото
          Container(
            width: 132, height: 132,
            margin: const EdgeInsets.all(4),
            decoration: BoxDecoration(
              color: const Color(0xFFEFE1FF),
              borderRadius: BorderRadius.circular(8),
            ),
            child: const Icon(Icons.image_outlined,
                size: 50, color: Color(0xFFB3BBFF)),
          ),

          // Инфо
          Expanded(
            child: Padding(
              padding: const EdgeInsets.symmetric(vertical: 8),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(item['title'] ?? '',
                          style: const TextStyle(
                              fontFamily: 'Inter', fontSize: 14)),
                      const SizedBox(height: 4),
                      Text(item['shop'] ?? '',
                          style: const TextStyle(
                              fontFamily: 'Inter', fontSize: 14)),
                    ],
                  ),

                  // Описание
                  Text(
                    item['description'] ?? '',
                    style: const TextStyle(
                        fontFamily: 'Inter', fontSize: 14,
                        color: Color(0xFF969696)),
                    maxLines: 3,
                    overflow: TextOverflow.ellipsis,
                  ),

                  // Дата и цена
                  Row(
                    children: [
                      Text(item['date'] ?? '',
                          style: const TextStyle(
                              fontFamily: 'Inter', fontSize: 14,
                              color: Color(0xFF383838))),
                      const Spacer(),
                      Text(item['price'] ?? '',
                          style: const TextStyle(
                              fontFamily: 'Inter', fontSize: 14)),
                      const SizedBox(width: 2),
                      const Text('₸',
                          style: TextStyle(
                              fontFamily: 'Inter', fontSize: 12,
                              fontWeight: FontWeight.w500,
                              color: Color(0xFF8A58FF))),
                      const SizedBox(width: 8),
                    ],
                  ),
                ],
              ),
            ),
          ),

          // Иконка сердца (избранное)
          Padding(
            padding: const EdgeInsets.only(right: 4, top: 4),
            child: Align(
              alignment: Alignment.topRight,
              child: GestureDetector(
                onTap: () {},
                child: const Icon(Icons.favorite,
                    color: Color(0xFFDBDFFF), size: 22),
              ),
            ),
          ),
        ],
      ),
    );
  }
}