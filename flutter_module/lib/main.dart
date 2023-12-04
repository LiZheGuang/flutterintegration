import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'detail.dart' as Detail;

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      routes: {
        '/': (context) => const MyHomePage(title: '演示DEMO'),
        '/details': (context) => const Detail.WebViewExample(),
      },
      initialRoute: "/",
      title: 'Flutter Demo',
      theme: ThemeData(primarySwatch: Colors.blue),
      // home: const MyHomePage(title: '演示 MethodChannel'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('com.bqt.test/base_channel');
  String _batteryLevel = '';
  int _counter = 0;

  void _incrementCounter() {
    setState(() =>
        _counter++); // This call to setState causes rerun the build method below
    _getBatteryLevel().then((value) {
      setState(() => _batteryLevel = value);
    });
  }

  void _clickCallback() async {
    var response = await platform.invokeMethod('onCallGetFunction');
    debugPrint("安卓原生方法");
    debugPrint(response);
  }

  void _clickOnCallNeXtSwitchBackground() async {
    var response = await platform.invokeMethod('onCallNeXtSwitchBackground');
    debugPrint("安卓原生方法");
    debugPrint(response);
  }

  void _clickFunction(methodName) async {
    var response = await platform.invokeMethod(methodName);
    debugPrint("安卓原生方法");
    debugPrint(response);
  }

  Future<String> _getBatteryLevel() async {
    try {
      final int result = await platform.invokeMethod('getBatteryLevel');
      return '电量 $result % .}';
    } on PlatformException catch (e) {
      return "获取失败: '${e.message}'.";
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('这是标题')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Text('电量 $_batteryLevel'),
              Text('点击次数 $_counter'),
              ElevatedButton(
                onPressed: () {
                  _incrementCounter();
                  // 在这里编写按钮点击时要执行的代码
                },
                child: const Text('电池'),
              ),
              ElevatedButton(
                onPressed: () {
                  _clickCallback();
                },
                child: const Text('功能'),
              ),
              ElevatedButton(
                onPressed: () {
                  _clickOnCallNeXtSwitchBackground();
                },
                child: const Text('切换APP到后台'),
              ),
              ElevatedButton(
                onPressed: () {
                  _clickFunction('onCallNextAddRouter');
                },
                child: const Text('跳转到Android页面'),
              ),
              ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const Detail.WebViewExample()),
                  );
                },
                child: const Text('跳转到Flutter页面'),
              )
            ],
          ),
        ),
        // floatingActionButton:FloatingActionButton(onPressed: _incrementCounter),
      ),
    );
  }
}
