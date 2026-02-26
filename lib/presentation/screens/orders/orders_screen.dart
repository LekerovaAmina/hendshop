import 'package:flutter/material.dart';

/// Страница заказов
class OrdersScreen extends StatelessWidget {
  const OrdersScreen({super.key});

  // Тестовые данные
  static final _activeOrders = [
    {'title': 'Ягодный торт 4кг', 'shop': 'Сладкоешка',
      'status': 'Приняли заказ', 'date': '20.09.25', 'price': '4500'},
  ];
  static final _arrivedOrders = [
    {'title': 'Ягодный торт 4кг', 'shop': 'Сладкоешка',
      'status': 'Доставлен', 'date': '20.09.25', 'price': '4500'},
    {'title': 'Ягодный торт 4кг', 'shop': 'Сладкоешка',
      'status': 'Доставлен', 'date': '20.09.25', 'price': '4500'},
  ];

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
            // Шапка с поиском
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

            // Список заказов
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(13, 8, 13, 0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Активные заказы
                    const Text('Заказы',
                        style: TextStyle(fontFamily: 'Inter', fontSize: 14)),
                    const SizedBox(height: 8),
                    ..._activeOrders.map((o) => _OrderCard(order: o, isActive: true)),

                    const SizedBox(height: 8),

                    // Пришедшие заказы
                    const Text('Пришедшие заказы',
                        style: TextStyle(fontFamily: 'Inter', fontSize: 14)),
                    const SizedBox(height: 8),
                    ..._arrivedOrders.map((o) => _OrderCard(order: o, isActive: false)),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

/// Карточка заказа (Frame 39)
class _OrderCard extends StatelessWidget {
  final Map<String, dynamic> order;
  final bool isActive;
  const _OrderCard({required this.order, required this.isActive});

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
          // Изображение товара (Rectangle 4)
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

          // Информация
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
                      Text(order['title'] ?? '',
                          style: const TextStyle(
                              fontFamily: 'Inter', fontSize: 14)),
                      const SizedBox(height: 4),
                      Text(order['shop'] ?? '',
                          style: const TextStyle(
                              fontFamily: 'Inter', fontSize: 14)),
                    ],
                  ),

                  // Статус
                  Text(
                    order['status'] ?? '',
                    style: const TextStyle(
                        fontFamily: 'Inter', fontSize: 14,
                        color: Color(0xFF8A58FF)),
                  ),

                  // Дата и цена
                  Row(
                    children: [
                      Text(order['date'] ?? '',
                          style: const TextStyle(
                              fontFamily: 'Inter', fontSize: 14,
                              color: Color(0xFF383838))),
                      const Spacer(),
                      Text(order['price'] ?? '',
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

          // Иконка избранного (Vector — DBDFFF цвет)
          Padding(
            padding: const EdgeInsets.only(right: 4, top: 4),
            child: Align(
              alignment: Alignment.topRight,
              child: Icon(
                  isActive ? Icons.favorite_border : Icons.favorite,
                  color: const Color(0xFFDBDFFF), size: 22),
            ),
          ),
        ],
      ),
    );
  }
}