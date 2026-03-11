import 'package:flutter/material.dart';
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

class CallLogScreen extends StatefulWidget {
  const CallLogScreen({super.key});

  @override
  State<CallLogScreen> createState() => _CallLogScreenState();
}

class _CallLogScreenState extends State<CallLogScreen> {

  List<Map<String, dynamic>> logs = [];

  Future<void> loadLogs() async {

    final dbPath = await getDatabasesPath();
    final path = join(dbPath, "call_logs.db");

    Database db = await openDatabase(path);

    final result = await db.query("logs", orderBy: "id DESC");

    setState(() {
      logs = result;
    });
  }

  @override
  void initState() {
    super.initState();
    loadLogs();
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(title: const Text("Call Logs")),

      body: ListView.builder(
        itemCount: logs.length,
        itemBuilder: (context, index) {

          final log = logs[index];

          return ListTile(
            leading: const Icon(Icons.call),
            title: Text(log["phone_number"] ?? "Unknown"),
            subtitle: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
            Text(log["timestamp"] ?? ""),
            Text(log["audio_path"] ?? "")
            ],
            ),
          );
    
        },
      ),
    );
  }
}