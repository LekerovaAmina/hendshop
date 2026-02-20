import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'presentation/screens/auth/login_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  // Только портретная ориентация
  SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown,
  ]);
  runApp(const HandShopApp());
}

class HandShopApp extends StatelessWidget {
  const HandShopApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'HandShop',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        // Используем системный шрифт (без Inter)
        primaryColor: const Color(0xFF7C3AED),
        scaffoldBackgroundColor: Colors.transparent,
        // Убираем тень у AppBar
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.transparent,
          elevation: 0,
          scrolledUnderElevation: 0,
        ),
      ),
      // Первый экран — Login
      home: const LoginScreen(),
    );
  }
}