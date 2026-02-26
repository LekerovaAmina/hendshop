import 'package:flutter/material.dart';

/// Страница профиля пользователя
class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

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
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 25),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const SizedBox(height: 20),

                // Аватар (Group 2 — Rectangle 4 круглый)
                Center(
                  child: Stack(
                    children: [
                      Container(
                        width: 117, height: 117,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          border: Border.all(
                              color: const Color(0xFF8A38F5), width: 1),
                          color: const Color(0xFFEFE1FF),
                        ),
                        child: const Icon(Icons.person,
                            size: 80, color: Color(0xFFB3BBFF)),
                      ),
                      Positioned(
                        bottom: 0, right: 0,
                        child: Container(
                          width: 30, height: 30,
                          decoration: const BoxDecoration(
                            color: Color(0xFF8A58FF),
                            shape: BoxShape.circle,
                          ),
                          child: const Icon(Icons.edit,
                              size: 16, color: Colors.white),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 20),

                // Ссылка "изменить фото профиля"
                Center(
                  child: GestureDetector(
                    onTap: () {},
                    child: const Text(
                      'изменить фото профиля',
                      style: TextStyle(
                          fontFamily: 'Inter', fontSize: 14,
                          color: Colors.black),
                    ),
                  ),
                ),
                const SizedBox(height: 45),

                // Данные пользователя
                _ProfileField(label: 'Имя пользователя', value: 'Амина Лекерова'),
                const SizedBox(height: 18),
                _ProfileField(label: 'Номер', value: '+7 (777) 777-77-77'),
                const SizedBox(height: 18),
                _ProfileField(label: 'Почта', value: 'amina@example.com'),
                const SizedBox(height: 18),

                // Кнопка "Изменить данные"
                Align(
                  alignment: Alignment.centerRight,
                  child: GestureDetector(
                    onTap: () {},
                    child: const Text(
                      'Изменить данный',
                      style: TextStyle(
                          fontFamily: 'Inter', fontSize: 14,
                          color: Colors.black),
                    ),
                  ),
                ),
                const SizedBox(height: 35),

                // Подписка
                const Text('Подписка',
                    style: TextStyle(fontFamily: 'Inter', fontSize: 14)),
                const SizedBox(height: 8),
                const Text('Инфо о подписке',
                    style: TextStyle(fontFamily: 'Inter', fontSize: 14,
                        color: Color(0xFF6B7280))),

                const SizedBox(height: 200),

                // Ссылка на сайт
                Row(
                  children: [
                    const Text('Подробнее на сайте: ',
                        style: TextStyle(fontFamily: 'Inter', fontSize: 14)),
                    GestureDetector(
                      onTap: () {},
                      child: const Text('сайт',
                          style: TextStyle(
                              fontFamily: 'Inter', fontSize: 14,
                              color: Color(0xFF8A58FF),
                              decoration: TextDecoration.underline)),
                    ),
                  ],
                ),
                const SizedBox(height: 20),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

/// Поле профиля с меткой
class _ProfileField extends StatelessWidget {
  final String label;
  final String value;
  const _ProfileField({required this.label, required this.value});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(label,
            style: const TextStyle(
                fontFamily: 'Inter', fontSize: 14, color: Colors.black)),
        const SizedBox(height: 4),
        Text(value,
            style: const TextStyle(
                fontFamily: 'Inter', fontSize: 14,
                color: Color(0xFF6B7280))),
      ],
    );
  }
}