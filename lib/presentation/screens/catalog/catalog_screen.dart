import 'package:flutter/material.dart';

/// Каталог — сетка категорий с градиентными плитками
class CatalogScreen extends StatelessWidget {
  const CatalogScreen({super.key});

  // Категории (из дизайна — "категория" как placeholder)
  static const _categories = [
    'Торты', 'Украшения', 'Игрушки', 'Свечи',
    'Фигурки', 'Вязание', 'Букеты', 'Мыло',
    'Открытки', 'Картины',
  ];

  @override
  Widget build(BuildContext context) {
    return Container(
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
            // Поиск
            Padding(
              padding: const EdgeInsets.fromLTRB(10, 8, 10, 0),
              child: Container(
                height: 40,
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(5),
                  border: Border.all(color: const Color(0xFF8A38F5)),
                ),
                child: const Row(
                  children: [
                    Padding(
                      padding: EdgeInsets.symmetric(horizontal: 14),
                      child: Icon(Icons.search, color: Color(0xFFC690FF), size: 20),
                    ),
                    Text('Поиск',
                        style: TextStyle(
                            fontFamily: 'Inter', fontSize: 14, color: Colors.black)),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 8),

            // Сетка категорий (Frame 7 из дизайна)
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 10),
                child: _CategoryGrid(categories: _categories),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

/// Сетка категорий с разными высотами (как в дизайне Figma)
class _CategoryGrid extends StatelessWidget {
  final List<String> categories;
  const _CategoryGrid({required this.categories});

  // Цвет одинаковый для всех из дизайна
  static const _gradColors = [Color(0xFFEFE1FF), Color(0xFFC5C3FC)];

  @override
  Widget build(BuildContext context) {
    // Строки из дизайна: [2, 3, 2, 3] (чередование 2 и 3 колонки)
    final rows = [
      _RowConfig(count: 2, height: 65),
      _RowConfig(count: 3, height: 65),
      _RowConfig(count: 2, height: 125),
      _RowConfig(count: 2, height: 65),
      _RowConfig(count: 3, height: 65),
    ];

    int catIndex = 0;
    final widgets = <Widget>[];

    for (final row in rows) {
      final rowCats = <String>[];
      for (int i = 0; i < row.count && catIndex < categories.length; i++) {
        rowCats.add(categories[catIndex++]);
      }
      if (rowCats.isNotEmpty) {
        widgets.add(_CategoryRow(
          categories: rowCats,
          height: row.height,
        ));
        widgets.add(const SizedBox(height: 8));
      }
    }

    return SingleChildScrollView(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: widgets,
      ),
    );
  }
}

class _RowConfig {
  final int count;
  final double height;
  const _RowConfig({required this.count, required this.height});
}

class _CategoryRow extends StatelessWidget {
  final List<String> categories;
  final double height;
  const _CategoryRow({required this.categories, required this.height});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: height,
      child: Row(
        children: categories.asMap().entries.map((e) {
          return Expanded(
            child: Padding(
              padding: EdgeInsets.only(left: e.key > 0 ? 8 : 0),
              child: _CategoryTile(name: e.value, height: height),
            ),
          );
        }).toList(),
      ),
    );
  }
}

class _CategoryTile extends StatelessWidget {
  final String name;
  final double height;
  const _CategoryTile({required this.name, required this.height});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {},
      child: Container(
        height: height,
        decoration: BoxDecoration(
          gradient: const LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFFEFE1FF), Color(0xFFC5C3FC)],
          ),
          borderRadius: BorderRadius.circular(5),
        ),
        alignment: Alignment.center,
        child: Text(
          name,
          style: const TextStyle(
              fontFamily: 'Inter', fontSize: 14, color: Colors.black),
        ),
      ),
    );
  }
}