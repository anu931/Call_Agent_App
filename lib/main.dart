import 'package:flutter/material.dart';
import 'screens/home_screen.dart';
import 'screens/call_log_screen.dart';

void main() => runApp(MaterialApp(
  debugShowCheckedModeBanner: false,
  theme: ThemeData.dark(),
  home: const AppShell(),
));

class AppShell extends StatefulWidget {
  const AppShell({super.key});
  @override
  State<AppShell> createState() => _AppShellState();
}

class _AppShellState extends State<AppShell> {
  int _page = 0;
  final _pages = const [HomeScreen(), CallLogScreen()];

  @override
  Widget build(BuildContext context) => Scaffold(
    body: _pages[_page],
    bottomNavigationBar: BottomNavigationBar(
      currentIndex: _page,
      onTap: (i) => setState(() => _page = i),
      items: const [
        BottomNavigationBarItem(icon: Icon(Icons.home), label: 'Home'),
        BottomNavigationBarItem(icon: Icon(Icons.list_alt), label: 'Call Log'),
      ],
    ),
  );
}