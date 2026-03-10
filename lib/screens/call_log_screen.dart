import 'package:flutter/material.dart';
import '../services/api_service.dart';

class CallLogScreen extends StatefulWidget {
  const CallLogScreen({super.key});
  @override
  State<CallLogScreen> createState() => _CallLogScreenState();
}

class _CallLogScreenState extends State<CallLogScreen> {
  List<Map> _calls = [];
  bool _loading = true;

  @override
  void initState() { super.initState(); _load(); }

  Future<void> _load() async {
    final calls = await ApiService.fetchCalls();
    setState(() { _calls = calls; _loading = false; });
  }

  String _fmtDuration(int secs) {
    final m = (secs ~/ 60).toString().padLeft(2, '0');
    final s = (secs % 60).toString().padLeft(2, '0');
    return '$m:$s';
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: const Text('Call Log'),
      actions: [IconButton(icon: const Icon(Icons.refresh), onPressed: _load)],
    ),
    body: _loading
      ? const Center(child: CircularProgressIndicator())
      : _calls.isEmpty
        ? const Center(child: Text('No calls yet', style: TextStyle(color: Colors.white38)))
        : RefreshIndicator(
            onRefresh: _load,
            child: ListView.separated(
              itemCount: _calls.length,
              separatorBuilder: (_, __) => const Divider(color: Colors.white12, height: 1),
              itemBuilder: (_, i) {
                final c = _calls[i];
                final hasDuration = (c['duration'] ?? 0) > 0;
                return ListTile(
                  leading: const CircleAvatar(
                    backgroundColor: Color(0xFF1A1A1A),
                    child: Icon(Icons.person, color: Colors.white54),
                  ),
                  title: Text(c['name'] ?? 'Unknown',
                    style: const TextStyle(fontWeight: FontWeight.bold)),
                  subtitle: Text(c['number'] ?? '',
                    style: const TextStyle(color: Colors.blue, fontSize: 12)),
                  trailing: Column(mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.end, children: [
                    Text(hasDuration ? _fmtDuration(c['duration']) : 'Missed',
                      style: TextStyle(
                        color: hasDuration ? Colors.green : Colors.red,
                        fontWeight: FontWeight.bold)),
                    Text('${c['date']}  ${c['time']}',
                      style: const TextStyle(color: Colors.white38, fontSize: 11)),
                  ]),
                  onTap: c['recording']?.isNotEmpty == true
                    ? () => showDialog(context: context, builder: (_) => AlertDialog(
                        backgroundColor: const Color(0xFF1A1A1A),
                        title: Text(c['number'] ?? ''),
                        content: Text('Recording:\n${c['recording']}',
                          style: const TextStyle(fontSize: 12, color: Colors.white54)),
                        actions: [TextButton(
                          onPressed: () => Navigator.pop(context),
                          child: const Text('Close'))],
                      ))
                    : null,
                );
              },
            ),
          ),
  );
}