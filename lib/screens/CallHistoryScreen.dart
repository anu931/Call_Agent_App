import 'dart:io';
import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';

class CallLogScreen extends StatefulWidget {
  const CallLogScreen({super.key});

  @override
  State<CallLogScreen> createState() => _CallLogScreenState();
}

class _CallLogScreenState extends State<CallLogScreen> {

  List<FileSystemEntity> recordings = [];

  @override
  void initState() {
    super.initState();
    loadRecordings();
  }

  Future<void> loadRecordings() async {

    Directory dir = Directory("/storage/emulated/0/CallAgent/recordings");

    if (await dir.exists()) {

      List<FileSystemEntity> files = dir.listSync();

      setState(() {
        recordings = files;
      });
    }
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: const Text("Call History"),
      ),

      body: recordings.isEmpty
          ? const Center(child: Text("No recordings found"))
          : ListView.builder(
              itemCount: recordings.length,
              itemBuilder: (context, index) {

                File file = File(recordings[index].path);

                String name = file.path.split("/").last;

                return Card(
                  margin: const EdgeInsets.all(10),

                  child: ListTile(
                    leading: const Icon(Icons.call),

                    title: Text(name),

                    subtitle: Text(file.path),

                    trailing: const Icon(Icons.play_arrow),

                    onTap: () {

                      // Later we will add audio player

                    },
                  ),
                );
              },
            ),
    );
  }
}